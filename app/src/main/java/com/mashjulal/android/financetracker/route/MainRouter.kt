package com.mashjulal.android.financetracker.route

import android.content.res.Configuration
import io.reactivex.subjects.PublishSubject


class MainRouter {

    companion object {


        const val REQUEST_ADD_INCOMING_OPERATION = "Request Add Incoming Operation"
        const val REQUEST_ADD_OUTGOING_OPERATION = "Request Add Outgoing Operation"
        const val CANCEL_OPERATION = "Cancel Operation"
        const val ACCEPT_OPERATION = "Accept Operation"

    }

    var orientation = Configuration.ORIENTATION_PORTRAIT

    val bus: PublishSubject<String> = PublishSubject.create()


    fun navigate(naviToken: String) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            when (naviToken) {
                CANCEL_OPERATION -> bus.onNext(naviToken)
                ACCEPT_OPERATION -> bus.onNext(naviToken)
                else -> bus.onNext(naviToken)
            }
        } else {

        }

    }
}