package com.mashjulal.android.financetracker.domain.interactor

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.financialcalculations.calculateBalance
import com.mashjulal.android.financetracker.domain.financialcalculations.calculateTotal
import com.mashjulal.android.financetracker.domain.repository.BalanceRepository
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import com.mashjulal.android.financetracker.presentation.main.recyclerview.balance.BalanceViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.IncomingsPreviewViewModel
import com.mashjulal.android.financetracker.presentation.main.recyclerview.operation.OutgoingsPreviewViewModel
import io.reactivex.Observable
import java.lang.Math.min

interface RefreshMainScreenDataInteractor {
    fun execute(): Observable<List<IComparableItem>>
}

class RefreshMainScreenDataInteractorImpl(
        private val balanceRepository: BalanceRepository,
        private val operationRepository: OperationRepository
) : RefreshMainScreenDataInteractor {

    override fun execute(): Observable<List<IComparableItem>> =
            Observable.create {
                val balanceViewModel = createBalanceModel()
                val incomingsViewModel = createIncomingsModel()
                val outgoingsViewModel = createOutgoingsModel()
                it.onNext(listOf(balanceViewModel, incomingsViewModel, outgoingsViewModel))
            }

    private fun createBalanceModel(): BalanceViewModel {
        val balances = balanceRepository.getLastByAll()
        val operationsAfter = operationRepository.getAfter(balances[0].date)
        val balanceTotal = calculateBalance(balances)
        val operationTotal = calculateTotal(operationsAfter)

        return BalanceViewModel(balanceTotal + operationTotal)
    }

    private fun createIncomingsModel(): IncomingsPreviewViewModel {
        val operations = operationRepository.getByType(OperationType.INCOMINGS)
        val balance = calculateTotal(operations)

        return IncomingsPreviewViewModel(balance, operations.subList(0, min(5, operations.size)),
                operations.size > 5)
    }

    private fun createOutgoingsModel(): OutgoingsPreviewViewModel {
        val operations = operationRepository.getByType(OperationType.OUTGOINGS)
        val balance = calculateTotal(operations)

        return OutgoingsPreviewViewModel(balance, operations.subList(0, min(5, operations.size)),
                operations.size > 5)
    }
}