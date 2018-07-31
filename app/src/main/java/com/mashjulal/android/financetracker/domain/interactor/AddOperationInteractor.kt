package com.mashjulal.android.financetracker.domain.interactor

import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import io.reactivex.Observable

interface AddOperationInteractor {

    fun execute(operation: Operation): Observable<Long>
}

class AddOperationInteractorImpl(
        private val operationRepository: OperationRepository
) : AddOperationInteractor {

    override fun execute(operation: Operation): Observable<Long> =
            Observable.create<Long> {
                it.onNext(operationRepository.insert(operation))
            }
}