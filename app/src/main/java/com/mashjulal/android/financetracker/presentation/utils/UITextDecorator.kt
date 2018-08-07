package com.mashjulal.android.financetracker.presentation.utils

import android.content.Context
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.AccountMapper
import com.mashjulal.android.financetracker.domain.repository.sqliteimpl.utils.CategoryMapper

class UITextDecorator {

    companion object {

        val specStringsId = listOf(
                R.string.predefined_other_incomings_category,
                R.string.predefined_other_outgoings_category,
                R.string.predefined_from_other_account_category,
                R.string.predefined_to_other_account_category,
                R.string.predefined_account
        )

        val specMap = mutableMapOf<String, Int>()
        val usableMap = mutableMapOf<Int, String>()


        fun mapSpecialToUsable(context: Context?, special: String): String {

            var id = CategoryMapper.specialToUsableId(special)
            if (id == R.string.predefined_wrong) {
                id = AccountMapper.specialToUsableId(special)
                if (id == R.string.predefined_wrong)
                    return special
            }

            if (!usableMap.containsKey(id)) {
                if (context != null)
                    usableMap[id] = context.resources.getString(id)
            }

            return usableMap[id]!!

        }

        fun mapUsableToSpecial(context: Context?, usable: String): String {

            if (!specMap.containsKey(usable)) {
                var found = false
                for (lineId in specStringsId) {
                    val line = context?.resources?.getString(lineId)
                    if (usable == line) {
                        specMap[usable] = lineId
                        found = true
                    }
                }
                if (!found)
                    return usable
            }

            var id = specMap[usable]!!
            if (CategoryMapper.hasItId(id))
                return CategoryMapper.usableIdToSpecial(id)
            if (AccountMapper.hasItId(id))
                return AccountMapper.usableIdToSpecial(id)
            return context?.resources?.getString(R.string.predefined_wrong) ?: usable


        }
    }

}