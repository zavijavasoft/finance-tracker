package com.mashjulal.android.financetracker.domain.interactor

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.financialcalculations.*
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.CurrencyRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import com.mashjulal.android.financetracker.presentation.main.recyclerview.balance.BalanceViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.IncomingsPreviewViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.OutgoingsPreviewViewModel
import io.reactivex.Observable
import java.lang.Math.min
import java.math.BigDecimal

interface RefreshMainScreenDataInteractor {
    fun execute(): Observable<List<IComparableItem>>
    fun execute(account: Account): Observable<List<IComparableItem>>
}

//TODO: remove hardcode
private const val FROM = "USD"
private const val TO = "RUB"

private const val MAX_ITEM_IN_LIST_COUNT = 5

class RefreshMainScreenDataInteractorImpl(
        private val balanceRepository: BalanceRepository,
        private val operationRepository: OperationRepository,
        private val currencyRepository: CurrencyRepository
) : RefreshMainScreenDataInteractor {

    override fun execute(): Observable<List<IComparableItem>> =
            Observable.fromCallable {
                val rate = currencyRepository.getRate(FROM, TO)
                val balances = balanceRepository.getLastByAll()
                val operations = operationRepository.getAfter(balances[0].date)
                createModelsFrom(rate, balances, operations)
            }

    override fun execute(account: Account): Observable<List<IComparableItem>> =
            Observable.fromCallable {
                val rate = currencyRepository.getRate(FROM, TO)
                val balances = listOf(balanceRepository.getLastByAccount(account))
                val operations = operationRepository.getByAccountAfter(account, balances[0].date)
                createModelsFrom(rate, balances, operations)
            }

    private fun createModelsFrom(rate: BigDecimal, balances: List<Balance>, operations: List<Operation>)
            : List<IComparableItem> {
        val balanceViewModel = createBalanceModel(balances, operations, rate)

        val (incomings, outgoings) = operations
                .partition { it.operationType == OperationType.INCOMINGS }
        val incomingsViewModel = createIncomingsModel(incomings, rate)
        val outgoingsViewModel = createOutgoingsModel(outgoings, rate)
        return listOf(balanceViewModel, incomingsViewModel, outgoingsViewModel)
    }

    private fun createBalanceModel(balances: List<Balance>,
                                   operations: List<Operation>, rate: BigDecimal): BalanceViewModel {
        val totalBalance = calculateBalance(balances, rate)
        val totalOperation = calculateTotal(operations, rate)
        val total = totalBalance + totalOperation
        return BalanceViewModel(total, total / rate)
    }

    private fun createIncomingsModel(operations: List<Operation>,
                                     rate: BigDecimal): IncomingsPreviewViewModel {
        val balance = calculateTotal(operations, rate)
        return IncomingsPreviewViewModel(balance,
                operations.subList(0, min(MAX_ITEM_IN_LIST_COUNT, operations.size)),
                operations.size > MAX_ITEM_IN_LIST_COUNT)
    }

    private fun createOutgoingsModel(operations: List<Operation>,
                                     rate: BigDecimal): OutgoingsPreviewViewModel {
        val balance = calculateTotal(operations, rate)
        return OutgoingsPreviewViewModel(balance,
                operations.subList(0, min(MAX_ITEM_IN_LIST_COUNT, operations.size)),
                operations.size > MAX_ITEM_IN_LIST_COUNT)
    }
}