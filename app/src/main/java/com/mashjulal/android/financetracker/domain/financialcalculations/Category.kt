package com.mashjulal.android.financetracker.domain.financialcalculations

import android.support.annotation.DrawableRes

data class Category(var operationType: OperationType = OperationType.OUTGOINGS,
                    val title: String = "Dummy Category",
                    @DrawableRes val imageRes: Int = 0)