package com.mashjulal.android.financetracker.domain.interactor

import android.support.annotation.DrawableRes
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.CategoryRepository
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.CategoryMapper
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

interface DispatchCategoryInteractor {
    fun execute(): Single<List<Category>>

    fun updateCategory(title: String, operationType: String, @DrawableRes imageRes: Int): Completable

}

class DispatchCategoryInteractorImpl @Inject constructor(private val categoryRepository: CategoryRepository)
    : DispatchCategoryInteractor {

    override fun execute(): Single<List<Category>> = categoryRepository.getAll()
            .map {
                it.filter { it.title != CategoryMapper.PREDEFINED_FROM_ACCOUNT }
                        .filter { it.title != CategoryMapper.PREDEFINED_TO_ACCOUNT }
            }

    override fun updateCategory(title: String, operationType: String, @DrawableRes imageRes: Int): Completable {
        return categoryRepository.getByTitle(title)
                .flatMapCompletable {
                    if (it.isEmpty()) {
                        categoryRepository.create(
                                Category(operationType = OperationType.fromString(operationType),
                                        title = title,
                                        imageRes = imageRes))
                    } else {
                        Completable.complete()
                    }
                }

    }


}