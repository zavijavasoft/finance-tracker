package com.mashjulal.android.financetracker.domain.repository.sqliteimpl


import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.SupportSQLiteOpenHelper
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.AccountModel
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.CategoryModel
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.OperationModel


class InnerSQLiteCallback constructor(version: Int) : SupportSQLiteOpenHelper.Callback(version) {
    override fun onCreate(db: SupportSQLiteDatabase?) {
        db?.execSQL(CategoryModel.CREATE_TABLE)
        db?.execSQL(AccountModel.CREATE_TABLE)
        db?.execSQL(OperationModel.CREATE_TABLE)
    }

    override fun onUpgrade(db: SupportSQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE Category")
    }
}