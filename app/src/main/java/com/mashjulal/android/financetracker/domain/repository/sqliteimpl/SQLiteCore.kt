package com.mashjulal.android.financetracker.domain.repository.sqliteimpl

import android.app.Application
import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.AccountMapper
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.CategoryMapper
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.AccountModel
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.CategoryModel
import com.squareup.sqlbrite3.BriteDatabase
import com.squareup.sqlbrite3.SqlBrite
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class SQLiteCore @Inject constructor(val app: Application,
                                     databaseName: String,
                                     databaseVersion: Int) {

    val sqlBrite = SqlBrite.Builder()
            .logger { message -> Log.d("Database", message) }
            .build()


    var database: BriteDatabase

    init {

        val sqlHelper = InnerSQLiteCallback(databaseVersion)
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(app)
                .name(databaseName)
                .callback(sqlHelper)
                .build()
        val factory = FrameworkSQLiteOpenHelperFactory()
        val helper = factory.create(configuration)

        database = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io())
        database.setLoggingEnabled(true)
    }


    private fun checkPredefinedAccount(): Completable {
        return Completable.fromCallable {
            val db = database.writableDatabase
            val insertAccount = AccountModel.InsertAccount(db)
            insertAccount.bind(AccountMapper.PREDEFINED_ACCOUNT, "RUB", 0.0, Date().time)
            try {
                insertAccount.executeInsert()
            } catch (e: Exception) {
                if (e !is SQLiteConstraintException)
                    throw e
                Log.d("Unique constraint ex", e.localizedMessage)
            } finally {

            }

        }
    }

    private fun checkPredefinedCategory(specialCategory: String, type: OperationType): Completable {
        return Completable.fromCallable {
            val db = database.writableDatabase
            val insertCategory = CategoryModel.InsertCategory(db)
            insertCategory.bind(specialCategory, specialCategory, type.toString())
            try {
                insertCategory.executeInsert()
            } catch (e: Exception) {
                if (e !is SQLiteConstraintException)
                    throw e
                Log.d("Unique constraint ex", e.localizedMessage)
            } finally {

            }

        }
    }


    fun check(): Completable {
        val poi = checkPredefinedCategory(CategoryMapper.PREDEFINED_OTHER_INCOMINGS, OperationType.INCOMINGS)
        val poo = checkPredefinedCategory(CategoryMapper.PREDEFINED_OTHER_OUTGOINGS, OperationType.OUTGOINGS)
        val pfa = checkPredefinedCategory(CategoryMapper.PREDEFINED_FROM_ACCOUNT, OperationType.INCOMINGS)
        val pto = checkPredefinedCategory(CategoryMapper.PREDEFINED_TO_ACCOUNT, OperationType.OUTGOINGS)
        val pa = checkPredefinedAccount()

        return poi.andThen(poo).andThen(pfa).andThen(pto).andThen(pa)
    }

}