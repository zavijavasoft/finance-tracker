package com.mashjulal.android.financetracker.domain.repository.sqliteimpl

import android.support.annotation.DrawableRes
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.CategoryRepository
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerCategory
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.CategoryModel
import com.squareup.sqldelight.SqlDelightQuery
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


class CategoryRepositoryImpl @Inject constructor(val core: SQLiteCore) : CategoryRepository {

    companion object {

        const val PREDEFINED_OTHER_INCOMINGS = "OtherIncomings"
        const val PREDEFINED_OTHER_OUTGOINGS = "OtherOutgoings"
        const val PREDEFINED_FROM_ACCOUNT = "FromAccount"
        const val PREDEFINED_TO_ACCOUNT = "ToAccount"


        val mapResourceIDtoString = hashMapOf(
                R.drawable.ic_cash_back_green_24dp to PREDEFINED_OTHER_INCOMINGS,
                R.drawable.ic_salary_green_24dp to "Salary",
                R.drawable.ic_card_giftcard_green_24dp to "Gift",
                R.drawable.ic_bills_red_24dp to PREDEFINED_OTHER_OUTGOINGS,
                R.drawable.ic_videogame_red_24dp to "Entertainments",
                R.drawable.ic_car_red_24dp to "Transport",
                R.drawable.ic_shopping_red_24dp to "Shopping"
        )

        val mapStringToResourceID = hashMapOf(
                "OtherIncomings" to R.drawable.ic_cash_back_green_24dp,
                "Salary" to R.drawable.ic_salary_green_24dp,
                "Gift" to R.drawable.ic_card_giftcard_green_24dp,
                "OtherOutgoings" to R.drawable.ic_bills_red_24dp,
                "Entertainments" to R.drawable.ic_videogame_red_24dp,
                "Transport" to R.drawable.ic_car_red_24dp,
                "Shopping" to R.drawable.ic_shopping_red_24dp
        )

        fun getStringByRId(@DrawableRes key: Int) = mapResourceIDtoString[key] ?: "OtherOutgoings"
        fun getRIdByString(@DrawableRes key: String) = mapStringToResourceID[key]
                ?: R.drawable.ic_bills_red_24dp
    }


    override fun getAll(): Single<List<Category>> {
        val statement: SqlDelightQuery = InnerCategory.FACTORY.SelectAll()
        return core.database.createQuery(CategoryModel.TABLE_NAME, statement.sql)
                .mapToList { it -> InnerCategory.ALL_CATEGORIES_MAPPER.map(it) }
                .map { it ->
                    it.map { it ->
                        Category(OperationType.getTypeByString(it.type()), it.category(),
                                getRIdByString(it.subcategory()))
                    }.toList()
                }.take(1).single(listOf())
    }

    override fun getByOperationType(operationType: OperationType): Single<List<Category>> {
        val statement = InnerCategory.FACTORY.SelectCategoryByType(operationType.toString())
        return core.database.createQuery(CategoryModel.TABLE_NAME, statement.sql)
                .mapToOne { it -> InnerCategory.SELECT_CATEGORY_BY_TYPE.map(it) }
                .map { it ->
                    Category(OperationType.getTypeByString(it.type()), it.category(),
                            getRIdByString(it.subcategory()))
                }
                .collect({ mutableListOf() }, { b: MutableList<Category>, t: Category -> b.add(t) })
                .map { it.toList() }
    }

    override fun create(category: Category): Completable {
        return Completable.create {
            val db = core.database.writableDatabase
            val insertCategory = CategoryModel.InsertCategory(db)
            insertCategory.bind(category.title, category.operationType.toString(),
                    getStringByRId(category.imageRes))
            insertCategory.executeInsert()
        }
    }

    override fun remove(category: Category): Completable {
        return Completable.create {
            val db = core.database.writableDatabase
            val deleteCategory = CategoryModel.DeleteCategory(db)
            deleteCategory.bind(category.title)
            deleteCategory.executeInsert()
        }
    }

}