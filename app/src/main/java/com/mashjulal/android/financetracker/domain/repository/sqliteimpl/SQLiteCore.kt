package com.mashjulal.android.financetracker.domain.repository.sqliteimpl

import android.app.Application
import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.util.Log
import com.squareup.sqlbrite3.BriteDatabase
import com.squareup.sqlbrite3.SqlBrite
import io.reactivex.schedulers.Schedulers
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


}