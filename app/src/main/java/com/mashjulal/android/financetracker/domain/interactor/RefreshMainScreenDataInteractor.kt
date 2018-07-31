package com.mashjulal.android.financetracker.domain.interactor

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.financialcalculations.*
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import com.mashjulal.android.financetracker.presentation.main.recyclerview.balance.BalanceViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.IncomingsPreviewViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.OutgoingsPreviewViewModel
import io.reactivex.Observable
import java.lang.Math.min
import javax.inject.Inject

interface RefreshMainScreenDataInteractor {
    fun execute(): Observable<List<IComparableItem>>
    fun execute(account: Account): Observable<List<IComparableItem>>
}


private const val MAX_ITEM_IN_LIST_COUNT = 5

class RefreshMainScreenDataInteractorImpl @Inject constructor(
        private val balanceRepository: BalanceRepository,
        private val operationRepository: OperationRepository
) : RefreshMainScreenDataInteractor {

    override fun execute(): Observable<List<IComparableItem>> =
            Observable.fromCallable {
                val balances = balanceRepository.getLastByAll()
                val operations = operationRepository.getAfter(balances[0].date)
                createModelsFrom(balances, operations)
            }

    override fun execute(account: Account): Observable<List<IComparableItem>> =
            Observable.fromCallable {
                val balances = listOf(balanceRepository.getLastByAccount(account))
                val operations = operationRepository.getByAccountAfter(account, balances[0].date)
                createModelsFrom(balances, operations)
            }

    private fun createModelsFrom(balances: List<Balance>, operations: List<Operation>)
            : List<IComparableItem> {
        val balanceViewModel = createBalanceModel(balances, operations)

        val (incomings, outgoings) = operations
                .partition { it.operationType == OperationType.INCOMINGS }
        val incomingsViewModel = createIncomingsModel(incomings)
        val outgoingsViewModel = createOutgoingsModel(outgoings)
        return listOf(balanceViewModel, incomingsViewModel, outgoingsViewModel)
    }

    private fun createBalanceModel(balances: List<Balance>,
                                   operations: List<Operation>): BalanceViewModel {
        val balancesGroupedByCurrency = balances
                .groupBy { it.amount.currency }
                .filter { it.key in listOf(Currency.RUBLE, Currency.DOLLAR) }
        val balanceInRubles = calculateBalance(balancesGroupedByCurrency[Currency.RUBLE]
                ?: listOf())
        val operationTotalInRubles = calculateTotal(operations.filter { it.amount.currency == Currency.RUBLE })

        val balanceInDollars = calculateBalance(balancesGroupedByCurrency[Currency.DOLLAR]
                ?: listOf())
        val operationTotalInDollars = calculateTotal(operations.filter { it.amount.currency == Currency.DOLLAR })

        val totalRubles = balanceInRubles + operationTotalInRubles
        val totalDollars = balanceInDollars + operationTotalInDollars
        return BalanceViewModel(totalRubles, totalDollars)
    }

    private fun createIncomingsModel(operations: List<Operation>): IncomingsPreviewViewModel {
        val balance = calculateTotal(operations)
        return IncomingsPreviewViewModel(balance,
                operations.subList(0, min(MAX_ITEM_IN_LIST_COUNT, operations.size)),
                operations.size > MAX_ITEM_IN_LIST_COUNT)
    }

    private fun createOutgoingsModel(operations: List<Operation>): OutgoingsPreviewViewModel {
        val balance = calculateTotal(operations)
        return OutgoingsPreviewViewModel(balance,
                operations.subList(0, min(MAX_ITEM_IN_LIST_COUNT, operations.size)),
                operations.size > MAX_ITEM_IN_LIST_COUNT)
    }
}