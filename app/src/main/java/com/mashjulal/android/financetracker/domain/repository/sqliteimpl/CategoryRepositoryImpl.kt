package com.mashjulal.android.financetracker.domain.repository.sqliteimpl

import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.CategoryRepository
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerCategory
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.CategoryModel
import com.mashjulal.android.financetracker.presentation.SubcategoryMapper
import com.squareup.sqldelight.SqlDelightQuery
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject


class CategoryRepositoryImpl @Inject constructor(val core: SQLiteCore) : CategoryRepository {

    override fun getAll(): Single<List<Category>> {
        val statement: SqlDelightQuery = InnerCategory.FACTORY.SelectAll()
        return core.database.createQuery(CategoryModel.TABLE_NAME, statement.sql)
                .mapToOne { it -> InnerCategory.ALL_CATEGORIES_MAPPER.map(it) }
                .map { it ->
                    Category(OperationType.getTypeByString(it.type()), it.category(),
                            SubcategoryMapper.getRIdByString(it.subcategory()))
                }
                .collect({ mutableListOf() }, { b: MutableList<Category>, t: Category -> b.add(t) })
                .map { it.toList() }
    }

    override fun getByOperationType(operationType: OperationType): Single<List<Category>> {
        val statement = InnerCategory.FACTORY.SelectCategoryByType(operationType.toString())
        return core.database.createQuery(CategoryModel.TABLE_NAME, statement.sql)
                .mapToOne { it -> InnerCategory.SELECT_CATEGORY_BY_TYPE.map(it) }
                .map { it ->
                    Category(OperationType.getTypeByString(it.type()), it.category(),
                            SubcategoryMapper.getRIdByString(it.subcategory()))
                }
                .collect({ mutableListOf() }, { b: MutableList<Category>, t: Category -> b.add(t) })
                .map { it.toList() }
    }

    override fun create(category: Category): Completable {
        return Completable.create {
            val db = core.database.writableDatabase
            val insertCategory = CategoryModel.InsertCategory(db)
            insertCategory.bind(category.title, category.operationType.toString(),
                    SubcategoryMapper.getStringByRId(category.imageRes))
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