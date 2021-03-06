package com.mashjulal.android.financetracker.stub.repository

import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.CategoryRepository
import io.reactivex.Completable
import io.reactivex.Single

class CategoryRepositoryStub : CategoryRepository {
    override fun getByTitle(title: String): Single<List<Category>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val data: HashMap<Long, Category> = hashMapOf(
            1L to Category(OperationType.INCOMINGS, "Salary", R.drawable.ic_salary_green_24dp),
            2L to Category(OperationType.INCOMINGS, "Cash back", R.drawable.ic_cash_back_green_24dp),
            3L to Category(OperationType.INCOMINGS, "Gift", R.drawable.ic_card_giftcard_green_24dp),
            4L to Category(OperationType.OUTGOINGS, "Bills", R.drawable.ic_bills_red_24dp),
            5L to Category(OperationType.OUTGOINGS, "Entrainment", R.drawable.ic_videogame_red_24dp),
            6L to Category(OperationType.OUTGOINGS, "Transport", R.drawable.ic_car_red_24dp),
            6L to Category(OperationType.OUTGOINGS, "Shopping", R.drawable.ic_shopping_red_24dp)
    )

    override fun getAll(): Single<List<Category>> {
        return Single.fromCallable { data.map { it.value } }
    }

    override fun getByOperationType(operationType: OperationType): Single<List<Category>> {
        return Single.fromCallable {
            data.map { it.value }.filter { it.operationType == operationType }
        }
    }

    override fun create(category: Category): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(category: Category): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}