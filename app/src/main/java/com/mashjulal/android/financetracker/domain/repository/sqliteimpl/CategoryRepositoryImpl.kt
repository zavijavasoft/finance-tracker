package com.mashjulal.android.financetracker.domain.repository.sqliteimpl

import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.CategoryRepository
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerCategory
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.CategoryMapper
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.CategoryModel
import com.squareup.sqldelight.SqlDelightQuery
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


class CategoryRepositoryImpl @Inject constructor(val core: SQLiteCore) : CategoryRepository {




    override fun getAll(): Single<List<Category>> {
        val statement: SqlDelightQuery = InnerCategory.FACTORY.SelectAll()
        return core.database.createQuery(CategoryModel.TABLE_NAME, statement.sql)
                .mapToList { it -> InnerCategory.ALL_CATEGORIES_MAPPER.map(it) }
                .map { it ->
                    it.map { it -> CategoryMapper.newCategory(it) }.toList()
                }.take(1).single(listOf())
    }

    override fun getByTitle(title: String): Single<List<Category>> {
        val statement: SqlDelightQuery = InnerCategory.FACTORY.SelectCategoryByCategory(title)
        return core.database.createQuery(CategoryModel.TABLE_NAME, statement.sql, title)
                .mapToList { it -> InnerCategory.SELECT_CATEGORY_BY_CATEGORY.map(it) }
                .map { it ->
                    it.map { it -> CategoryMapper.newCategory(it) }.toList()
                }.take(1).single(listOf())
    }


    override fun getByOperationType(operationType: OperationType): Single<List<Category>> {
        val statement = InnerCategory.FACTORY.SelectCategoryByType(operationType.toString())
        return core.database.createQuery(CategoryModel.TABLE_NAME, statement.sql)
                .mapToOne { it -> InnerCategory.SELECT_CATEGORY_BY_TYPE.map(it) }
                .map { it ->
                    CategoryMapper.newCategory(it)
                }
                .collect({ mutableListOf() }, { b: MutableList<Category>, t: Category -> b.add(t) })
                .map { it.toList() }
    }

    override fun create(category: Category): Completable {
        return Completable.create {
            val db = core.database.writableDatabase
            val insertCategory = CategoryModel.InsertCategory(db)
            insertCategory.bind(category.title,
                    CategoryMapper.getStringByRId(category.imageRes),
                    category.operationType.toString())
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