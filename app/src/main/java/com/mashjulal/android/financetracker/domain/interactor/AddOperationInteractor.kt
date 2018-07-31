package com.mashjulal.android.financetracker.domain.interactor

import com.mashjulal.android.financetracker.domain.financialcalculations.Operation
import com.mashjulal.android.financetracker.domain.repository.OperationRepository
import io.reactivex.Completable

interface AddOperationInteractor {

    fun execute(operation: Operation): Completable
}

class AddOperationInteractorImpl(
        private var operationRepository: OperationRepository
) : AddOperationInteractor {

    override fun execute(operation: Operation): Completable =
            Completable.fromAction {
                operationRepository.insert(operation)
            }
}