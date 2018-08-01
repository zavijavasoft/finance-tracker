package com.mashjulal.android.financetracker.domain.financialcalculations

import android.support.annotation.DrawableRes

data class Category(var operationType: OperationType, val title: String, @DrawableRes val imageRes: Int)