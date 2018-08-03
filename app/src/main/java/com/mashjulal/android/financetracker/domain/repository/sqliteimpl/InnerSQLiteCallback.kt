package com.mashjulal.android.financetracker.domain.repository.sqliteimpl


import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.SupportSQLiteOpenHelper
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.CategoryModel


class InnerSQLiteCallback constructor(version: Int) : SupportSQLiteOpenHelper.Callback(version) {
    override fun onCreate(db: SupportSQLiteDatabase?) {
        db?.execSQL(CategoryModel.CREATE_TABLE)
    }

    override fun onUpgrade(db: SupportSQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}