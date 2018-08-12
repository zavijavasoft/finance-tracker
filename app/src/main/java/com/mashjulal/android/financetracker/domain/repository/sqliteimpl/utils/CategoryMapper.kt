package com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils

import android.support.annotation.DrawableRes
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.Category
import com.mashjulal.android.financetracker.domain.financialcalculations.OperationType
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model.InnerCategory

class CategoryMapper {
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

        val mapSpecialToUsable = hashMapOf(
                PREDEFINED_OTHER_INCOMINGS to R.string.predefined_other_incomings_category,
                PREDEFINED_OTHER_OUTGOINGS to R.string.predefined_other_outgoings_category,
                PREDEFINED_FROM_ACCOUNT to R.string.predefined_from_other_account_category,
                PREDEFINED_TO_ACCOUNT to R.string.predefined_to_other_account_category
        )

        val mapUsableToSpecial = hashMapOf(
                R.string.predefined_other_incomings_category to PREDEFINED_OTHER_INCOMINGS,
                R.string.predefined_other_outgoings_category to PREDEFINED_OTHER_OUTGOINGS,
                R.string.predefined_from_other_account_category to PREDEFINED_FROM_ACCOUNT,
                R.string.predefined_to_other_account_category to PREDEFINED_TO_ACCOUNT
        )

        fun specialToUsableId(special: String) = mapSpecialToUsable[special]
                ?: R.string.predefined_wrong

        fun usableIdToSpecial(@DrawableRes id: Int) = mapUsableToSpecial[id]
                ?: PREDEFINED_OTHER_OUTGOINGS

        fun hasItId(@DrawableRes id: Int) = mapUsableToSpecial.keys.contains(id)


        fun getStringByRId(@DrawableRes key: Int) = mapResourceIDtoString[key] ?: "OtherOutgoings"
        fun getRIdByString(key: String) = mapStringToResourceID[key]
                ?: R.drawable.ic_launcher_background

        fun isSpecialTransferCategory(category: Category): Boolean =
                category.title in listOf(PREDEFINED_FROM_ACCOUNT, PREDEFINED_TO_ACCOUNT)

        fun isUndeletable(special: String) =
                special in listOf(PREDEFINED_OTHER_INCOMINGS,
                        PREDEFINED_OTHER_OUTGOINGS,
                        PREDEFINED_FROM_ACCOUNT,
                        PREDEFINED_TO_ACCOUNT)


        fun newCategory(innerCategory: InnerCategory): Category {
            return Category(
                    OperationType.fromString(innerCategory.type()),
                    innerCategory.category(),
                    getRIdByString(innerCategory.subcategory()))
        }


    }
}