package com.mashjulal.android.financetracker.presentation

import android.support.annotation.DrawableRes
import com.mashjulal.android.financetracker.R

class SubcategoryMapper {
    companion object {

        val mapResourceIDtoString = hashMapOf(
                R.drawable.ic_cash_back_green_24dp to "OtherIncomings",
                R.drawable.ic_salary_green_24dp to "Salary",
                R.drawable.ic_card_giftcard_green_24dp to "Gift",
                R.drawable.ic_bills_red_24dp to "OtherOutgoings",
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
}