package com.mashjulal.android.financetracker.domain.interactor

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.financialcalculations.*
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import com.mashjulal.android.financetracker.presentation.main.recyclerview.balance.BalanceViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.IncomingsPreviewViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.OutgoingsPreviewViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.lang.Math.min
import java.util.*
import javax.inject.Inject

interface RefreshMainScreenDataInteractor {
    fun execute(): Observable<List<IComparableItem>>
    fun execute(account: Account): Observable<List<IComparableItem>>
}


private const val MAX_ITEM_IN_LIST_COUNT = 5

class RefreshMainScreenDataInteractorImpl @Inject constructor(
        private val currencyRepository: CurrencyRepository,
        private val balanceRepository: BalanceRepository,
        private val operationRepository: OperationRepository
) : RefreshMainScreenDataInteractor {

    override fun execute(): Observable<List<IComparableItem>> {
        val balances = balanceRepository.getLastByAll()
        val operations = operationRepository.getAll()
        return Single.zip(balances, operations, BiFunction { balanceList: List<Balance>,
                                                             operationList: List<Operation> ->
            createModelsFrom(balanceList, operationList)
        }).toObservable()
    }

    override fun execute(account: Account): Observable<List<IComparableItem>> {
        val balances = balanceRepository.getLastByAccount(account)
        val operations = operationRepository.getByAccountAfter(account, Date())
        return Single.zip(balances, operations, BiFunction { balanceFound: List<Balance>,
                                                             operationList: List<Operation> ->
            createModelsFrom(balanceFound, operationList)
        }).toObservable()
    }

    private fun createModelsFrom(balances: List<Balance>, operations: List<Operation>)
            : List<IComparableItem> {
        val balanceViewModel = createBalanceModel(balances, operations)

        val (incomings, outgoings) = operations
                .partition { it.category.operationType == OperationType.INCOMINGS }
        val incomingsViewModel = createIncomingsModel(incomings)
        val outgoingsViewModel = createOutgoingsModel(outgoings)
        return listOf(balanceViewModel, incomingsViewModel, outgoingsViewModel)
    }

    private fun createBalanceModel(balances: List<Balance>,
                                   operations: List<Operation>): BalanceViewModel {

        val realOps = if (balances.size != 1) operations else operations.filter { it.account == balances[0].account }

        val rub = calculateTotalEx(realOps, Currency.RUBLE) { from, to ->
            currencyRepository.getRate(from, to)
        }.blockingGet() // Да, я знаю, это харам, но позже соображу, как сделать этот кусок тоже синглом
        val usd = calculateTotalEx(realOps, Currency.DOLLAR) { from, to ->
            currencyRepository.getRate(from, to)
        }.blockingGet()

        return BalanceViewModel(Money(rub, Currency.RUBLE), Money(usd, Currency.DOLLAR))
    }

    private fun createIncomingsModel(operations: List<Operation>): IncomingsPreviewViewModel {
        val currencyCount = operations.asSequence().groupBy { it.account.money.currency }.count()

        val balance = calculateTotalEx(operations, if (currencyCount == 1) operations[0].account.money.currency
        else Currency.RUBLE) { from, to ->
            currencyRepository.getRate(from, to)

        }.blockingGet()
        return IncomingsPreviewViewModel(balance,
                operations.subList(0, min(MAX_ITEM_IN_LIST_COUNT, operations.size)),
                operations.size > MAX_ITEM_IN_LIST_COUNT)
    }

    private fun createOutgoingsModel(operations: List<Operation>): OutgoingsPreviewViewModel {
        val currencyCount = operations.asSequence().groupBy { it.account.money.currency }.count()

        val balance = calculateTotalEx(operations, if (currencyCount == 1) operations[0].account.money.currency
        else Currency.RUBLE) { from, to ->
            currencyRepository.getRate(from, to)

        }.blockingGet() //
        return OutgoingsPreviewViewModel(Money(-balance.amount, balance.currency),
                operations.subList(0, min(MAX_ITEM_IN_LIST_COUNT, operations.size)),
                operations.size > MAX_ITEM_IN_LIST_COUNT)
    }
}