package com.mashjulal.android.financetracker.route

import android.content.res.Configuration
import io.reactivex.subjects.PublishSubject


class MainRouter {

    data class Command(val command: String,
                       val param1: String = "",
                       val param2: String = "")

    companion object {

        const val BACK_PRESSED = "Back Pressed"
        const val SHUTDOWN = "Close App"
        const val REQUEST_ADD_INCOMING_OPERATION = "Request Add Incoming Operation"
        const val REQUEST_ADD_OUTGOING_OPERATION = "Request Add Outgoing Operation"
        const val REQUEST_ADD_ACCOUNT = "Request Add Account"
        const val REQUEST_ADD_CATEGORY = "Request Add Category"
        const val CANCEL_OPERATION = "Cancel Operation"
        const val ACCEPT_OPERATION = "Accept Operation"
        const val ACCOUNT_REPLACED = "Account Replaced"
        const val CATEGORY_REPLACED = "Category Replaced"
        const val BALANCE_ACCOUNT_CHANGED = "Balance Account Changed"

    }

    enum class State {
        PRESENTATION,
        EDITION
    }

    var state = State.PRESENTATION
    var orientation = Configuration.ORIENTATION_PORTRAIT

    val bus: PublishSubject<Command> = PublishSubject.create()


    fun navigate(naviToken: Command) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            when (naviToken.command) {
                BACK_PRESSED -> {
                    val redirect = if (state == State.EDITION) {
                        state = State.PRESENTATION
                        naviToken.copy(command = CANCEL_OPERATION)
                    } else {
                        Command(SHUTDOWN)
                    }
                    bus.onNext(redirect)
                }
                REQUEST_ADD_INCOMING_OPERATION,
                REQUEST_ADD_OUTGOING_OPERATION,
                REQUEST_ADD_ACCOUNT,
                REQUEST_ADD_CATEGORY -> {
                    state = State.EDITION
                    bus.onNext(naviToken)
                }
                else -> bus.onNext(naviToken)
            }
        } else {

        }

    }
}