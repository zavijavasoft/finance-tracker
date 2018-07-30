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

interface RefreshMainScreenDataInteractor {
    fun execute(): Observable<List<IComparableItem>>
    fun execute(account: Account): Observable<List<IComparableItem>>
}

class RefreshMainScreenDataInteractorImpl(
        private val balanceRepository: BalanceRepository,
        private val operationRepository: OperationRepository
) : RefreshMainScreenDataInteractor {

    override fun execute(): Observable<List<IComparableItem>> =
            Observable.create {
                val balances = balanceRepository.getLastByAll()
                val operations = operationRepository.getAfter(balances[0].date)

                val balanceViewModel = createBalanceModel(balances, operations)

                val (incomings, outgoings) = operations
                        .partition { it.operationType == OperationType.INCOMINGS }
                val incomingsViewModel = createIncomingsModel(incomings)
                val outgoingsViewModel = createOutgoingsModel(outgoings)
                it.onNext(listOf(balanceViewModel, incomingsViewModel, outgoingsViewModel))
            }

    override fun execute(account: Account): Observable<List<IComparableItem>> = Observable.create {
        val balances = listOf(balanceRepository.getLastByAccount(account))
        val operations = operationRepository.getByAccountAfter(account, balances[0].date)

        val balanceViewModel = createBalanceModel(balances, operations)
        val incomingsViewModel = createIncomingsModel(
                operations.filter { it.operationType == OperationType.INCOMINGS })
        val outgoingsViewModel = createOutgoingsModel(
                operations.filter { it.operationType == OperationType.OUTGOINGS }
        )
        it.onNext(listOf(balanceViewModel, incomingsViewModel, outgoingsViewModel))
    }

    private fun createBalanceModel(balances: List<Balance>, operations: List<Operation>): BalanceViewModel {
        val balanceTotal = calculateBalance(balances)
        val operationTotal = calculateTotal(operations)

        return BalanceViewModel(balanceTotal + operationTotal)
    }

    private fun createIncomingsModel(operations: List<Operation>): IncomingsPreviewViewModel {
        val balance = calculateTotal(operations)
        return IncomingsPreviewViewModel(balance, operations.subList(0, min(5, operations.size)),
                operations.size > 5)
    }

    private fun createOutgoingsModel(operations: List<Operation>): OutgoingsPreviewViewModel {
        val balance = calculateTotal(operations)
        return OutgoingsPreviewViewModel(balance, operations.subList(0, min(5, operations.size)),
                operations.size > 5)
    }
}