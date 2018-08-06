package com.mashjulal.android.financetracker.domain.interactor

import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.SQLiteCore
import io.reactivex.Completable
import javax.inject.Inject

interface StorageConsistencyInteractor {
    fun check(): Completable
}

class StorageConsistanceInteracrorImpl @Inject constructor(private val sqlCore: SQLiteCore)
    : StorageConsistencyInteractor {
    override fun check(): Completable {
        return sqlCore.check()
    }

}