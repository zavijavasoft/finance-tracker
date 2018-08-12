package com.mashjulal.android.financetracker.presentation.utils

import android.content.Context
import android.graphics.Color
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

        fun rgb(hex: String): Int {
            val color = java.lang.Long.parseLong(hex.replace("#", ""), 16).toInt()
            val r = color shr 16 and 0xFF
            val g = color shr 8 and 0xFF
            val b = color shr 0 and 0xFF
            return Color.rgb(r, g, b)
        }


        val LIBERTY_COLORS = intArrayOf(Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187), Color.rgb(118, 174, 175), Color.rgb(42, 109, 130))
        val JOYFUL_COLORS = intArrayOf(Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120), Color.rgb(106, 167, 134), Color.rgb(53, 194, 209))
        val PASTEL_COLORS = intArrayOf(Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162), Color.rgb(191, 134, 134), Color.rgb(179, 48, 80))
        val COLORFUL_COLORS = intArrayOf(Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31), Color.rgb(179, 100, 53))
        val VORDIPLOM_COLORS = intArrayOf(Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140), Color.rgb(140, 234, 255), Color.rgb(255, 140, 157))
        val MATERIAL_COLORS = intArrayOf(rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db"))


        val specMap = mutableMapOf<String, Int>()
        val usableMap = mutableMapOf<Int, String>()

        fun formActionBarTitle(context: Context?, special: String, addAppName: Boolean = false): String {
            var usable = if (special.isEmpty()) {
                context?.resources!!.getString(R.string.all_accounts)
            } else {
                mapSpecialToUsable(context, special)
            }

            val appName = if (addAppName) {
                val name = context?.resources!!.getString(R.string.app_name)
                "$name::"
            } else {
                ""
            }
            return "$appName$usable"
        }

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