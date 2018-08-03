package com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model

import com.google.auto.value.AutoValue
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.CategoryModel
import com.squareup.sqldelight.RowMapper


@AutoValue
abstract class InnerCategory : CategoryModel {
    companion object {

        val FACTORY: CategoryModel.Factory<InnerCategory> =
                CategoryModel.Factory(CategoryModel.Creator<InnerCategory> { id, category, subcategory, type ->
                    AutoValue_InnerCategory(id, category, subcategory, type)
                })

        val ALL_CATEGORIES_MAPPER: RowMapper<InnerCategory> = FACTORY.selectAllMapper()
        val SELECT_CATEGORY_BY_ID: RowMapper<InnerCategory> = FACTORY.selectCategoryByIdMapper()
        val SELECT_CATEGORY_BY_TYPE: RowMapper<InnerCategory> = FACTORY.selectCategoryByTypeMapper()
    }


}