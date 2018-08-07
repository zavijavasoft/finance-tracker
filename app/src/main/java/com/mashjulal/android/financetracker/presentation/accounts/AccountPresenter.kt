package com.mashjulal.android.financetracker.presentation.accounts

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import javax.inject.Inject

@InjectViewState
class AccountPresenter @Inject constructor()
    : MvpPresenter<AccountPresenter.View>() {


    interface View : MvpView
}