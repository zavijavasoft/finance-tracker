package com.mashjulal.android.financetracker.route

import android.content.res.Configuration
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainRouter @Inject constructor() {

    data class Command(val command: String,
                       val param1: String = "",
                       val param2: String = "")

    companion object {

        const val ACCOUNT_REPLACED = "Account Replaced"
        const val MOVE_BACK = "Move Back"
        const val SHUTDOWN = "Close App"
        const val NOTIFY_ORIENTATION = "Orientation Notification"


        const val TO_SINGLE_BALANCE = "To Single Balance"
        const val TO_SINGLE_ACCOUNT_LIST = "To Single Account List"
        const val TO_SINGLE_CATEGORY_LIST = "To Single Category List"
        const val TO_TWIN_BALANCE = "To Twin Balance"
        const val TO_TWIN_ACCOUNTS_CATEGORIES = "To Twin Accounts & Categories"

        const val TO_SINGLE_ADD_OPERATION = "TO_SINGLE_ADD_OPERATION"
        const val TO_SINGLE_ABOUT = "TO_SINGLE_ABOUT"
        const val TO_SINGLE_SETTINGS = "TO_SINGLE_SETTINGS"
        const val TO_SINGLE_CHART = "TO_SINGLE_CHART"
        const val TO_SINGLE_ADD_ACCOUNT = "TO_SINGLE_ADD_ACCOUNT"
        const val TO_SINGLE_ADD_CATEGORY = "TO_SINGLE_ADD_CATEGORY"
        const val TO_TWIN_ADD_OPERATION = "TO_TWIN_ADD_OPERATION"
        const val TO_TWIN_ADD_ACCOUNT = "TO_TWIN_ADD_ACCOUNT"
        const val TO_TWIN_ADD_CATEGORY = "TO_TWIN_ADD_CATEGORY"

    }


    enum class State {
        SINGLE_BALANCE,
        SINGLE_ADD_OPERATION,
        SINGLE_ABOUT,
        SINGLE_SETTINGS,
        SINGLE_CHART,
        SINGLE_CATEGORIES,
        SINGLE_ACCOUNTS,
        SINGLE_ADD_ACCOUNT,
        SINGLE_ADD_CATEGORY,
        TWIN_BALANCE,
        TWIN_ADD_OPERATION,
        TWIN_ACCOUNT_CATEGORY,
        TWIN_ADD_ACCOUNT,
        TWIN_ADD_CATEGORY
    }

    private val mapTransitionsToState = mapOf(
            State.SINGLE_BALANCE to TO_SINGLE_BALANCE,
            State.SINGLE_ADD_OPERATION to TO_SINGLE_ADD_OPERATION,
            State.SINGLE_ABOUT to TO_SINGLE_ABOUT,
            State.SINGLE_SETTINGS to TO_SINGLE_SETTINGS,
            State.SINGLE_CHART to TO_SINGLE_CHART,
            State.SINGLE_CATEGORIES to TO_SINGLE_CATEGORY_LIST,
            State.SINGLE_ACCOUNTS to TO_SINGLE_ACCOUNT_LIST,
            State.SINGLE_ADD_ACCOUNT to TO_SINGLE_ADD_ACCOUNT,
            State.SINGLE_ADD_CATEGORY to TO_SINGLE_ADD_CATEGORY,
            State.TWIN_BALANCE to TO_TWIN_BALANCE,
            State.TWIN_ADD_OPERATION to TO_TWIN_ADD_OPERATION,
            State.TWIN_ACCOUNT_CATEGORY to TO_TWIN_ACCOUNTS_CATEGORIES,
            State.TWIN_ADD_ACCOUNT to TO_TWIN_ADD_ACCOUNT,
            State.TWIN_ADD_CATEGORY to TO_TWIN_ADD_CATEGORY
    )

    enum class ExState {
        PRESENTATION,
        CHILDREN_PRESENTATION,
        SINGLE_EDITION,
        DUAL_CATEGORY_EDITION,
        DUAL_ACCOUNT_EDITION
    }

    var state = State.SINGLE_BALANCE
    private var orientation = Configuration.ORIENTATION_PORTRAIT

    val bus: BehaviorSubject<Command> = BehaviorSubject.create()


    fun notifyOrientation(orientation: Int) {
        this.orientation = orientation
    }


    private fun redirectMoveBack(naviToken: Command) {
        val redirect: Command = when (state) {
            State.TWIN_BALANCE,
            State.SINGLE_BALANCE -> {
                state = State.SINGLE_BALANCE
                naviToken.copy(command = SHUTDOWN)
            }
            State.SINGLE_ADD_OPERATION,
            State.SINGLE_ABOUT,
            State.SINGLE_SETTINGS,
            State.SINGLE_CHART,
            State.SINGLE_CATEGORIES,
            State.SINGLE_ACCOUNTS -> {
                state = State.SINGLE_BALANCE
                naviToken.copy(command = TO_SINGLE_BALANCE)
            }
            State.SINGLE_ADD_ACCOUNT -> {
                state = State.SINGLE_ACCOUNTS
                naviToken.copy(command = TO_SINGLE_ACCOUNT_LIST)
            }
            State.SINGLE_ADD_CATEGORY -> {
                state = State.SINGLE_CATEGORIES
                naviToken.copy(command = TO_SINGLE_CATEGORY_LIST)
            }
            State.TWIN_ADD_OPERATION,
            State.TWIN_ACCOUNT_CATEGORY -> {
                state = State.TWIN_BALANCE
                naviToken.copy(command = TO_TWIN_BALANCE)
            }

            State.TWIN_ADD_ACCOUNT,
            State.TWIN_ADD_CATEGORY -> {
                state = State.TWIN_ACCOUNT_CATEGORY
                naviToken.copy(command = TO_TWIN_ACCOUNTS_CATEGORIES)
            }
        }
        bus.onNext(redirect)
    }


    private fun redirectOrientationChanged(cmd: Command): Command {


        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            when (cmd.command) {
                TO_TWIN_BALANCE -> cmd.copy(command = TO_SINGLE_BALANCE)
                TO_TWIN_ACCOUNTS_CATEGORIES -> cmd.copy(command = TO_SINGLE_ACCOUNT_LIST)
                TO_TWIN_ADD_OPERATION -> cmd.copy(command = TO_SINGLE_ADD_OPERATION)
                TO_TWIN_ADD_ACCOUNT -> cmd.copy(command = TO_SINGLE_ADD_ACCOUNT)
                TO_TWIN_ADD_CATEGORY -> cmd.copy(command = TO_SINGLE_ADD_CATEGORY)
                else -> cmd
            }
        } else {
            when (cmd.command) {
                TO_SINGLE_BALANCE -> cmd.copy(command = TO_TWIN_BALANCE)
                TO_SINGLE_ACCOUNT_LIST -> cmd.copy(command = TO_TWIN_ACCOUNTS_CATEGORIES)
                TO_SINGLE_CATEGORY_LIST -> cmd.copy(command = TO_TWIN_ACCOUNTS_CATEGORIES)
                TO_SINGLE_ADD_OPERATION -> cmd.copy(command = TO_SINGLE_ADD_OPERATION)
                TO_SINGLE_ABOUT -> cmd.copy(command = TO_TWIN_BALANCE)
                TO_SINGLE_SETTINGS -> cmd.copy(command = TO_TWIN_BALANCE)
                TO_SINGLE_CHART -> cmd.copy(command = TO_TWIN_BALANCE)
                TO_SINGLE_ADD_ACCOUNT -> cmd.copy(command = TO_TWIN_ADD_ACCOUNT)
                TO_SINGLE_ADD_CATEGORY -> cmd.copy(command = TO_TWIN_ADD_CATEGORY)
                else -> cmd
            }
        }

    }

    fun navigate(cmdOrigin: Command) {

        val cmd = redirectOrientationChanged(cmdOrigin)

        when (cmd.command) {
            MOVE_BACK -> {
                redirectMoveBack(cmd)
                return
            }

            TO_SINGLE_BALANCE -> state = State.SINGLE_BALANCE
            TO_SINGLE_ACCOUNT_LIST -> state = State.SINGLE_ACCOUNTS
            TO_SINGLE_CATEGORY_LIST -> state = State.SINGLE_CATEGORIES
            TO_TWIN_BALANCE -> state = State.TWIN_BALANCE
            TO_TWIN_ACCOUNTS_CATEGORIES -> state = State.TWIN_ACCOUNT_CATEGORY
            TO_SINGLE_ADD_OPERATION -> state = State.SINGLE_ADD_OPERATION
            TO_SINGLE_ABOUT -> state = State.SINGLE_ABOUT
            TO_SINGLE_SETTINGS -> state = State.SINGLE_SETTINGS
            TO_SINGLE_CHART -> state = State.SINGLE_CHART
            TO_SINGLE_ADD_ACCOUNT -> state = State.SINGLE_ADD_ACCOUNT
            TO_SINGLE_ADD_CATEGORY -> state = State.SINGLE_ADD_CATEGORY
            TO_TWIN_ADD_OPERATION -> state = State.TWIN_ADD_OPERATION
            TO_TWIN_ADD_ACCOUNT -> state = State.TWIN_ADD_ACCOUNT
            TO_TWIN_ADD_CATEGORY -> state = State.TWIN_ADD_CATEGORY

        }

        bus.onNext(cmd)

    }
}