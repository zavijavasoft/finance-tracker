package com.mashjulal.android.financetracker.presentation.editoperation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.davidmiguel.numberkeyboard.NumberKeyboardListener
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.*
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import kotlinx.android.synthetic.main.activity_edit_operation.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val ARG_OPERATION_TYPE = "type"

private const val MAX_AMOUNT_DIGIT_COUNT = 12

class EditOperationActivity : AppCompatActivity(), EditOperationPresenter.View {

    @Inject
    lateinit var presenter: EditOperationPresenter
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_operation)

        App.appComponent.inject(this)
        parseArgs()
        initLayout()
        setResult(Activity.RESULT_CANCELED)
    }

    private fun parseArgs() {
        val type = intent.getStringExtra(ARG_OPERATION_TYPE)
        spinnerOperationType.setSelection(if (type == "incomings") 0 else 1)
    }

    private fun initLayout() {
        numberKeyboard.setListener(object : NumberKeyboardListener {
            override fun onNumberClicked(p0: Int) {
                val amount = etAmount.text.toString()

                if (amount == "0") {
                    if (p0 != 0) {
                        etAmount.setText(p0.toString())
                    }
                    return
                }

                val doteIndex = amount.indexOf(".")
                val noDot = doteIndex == -1
                val lessThanThreeDigitsAfterDot = doteIndex > amount.length - 3
                val amountBelowLimit = amount.length < MAX_AMOUNT_DIGIT_COUNT

                if ((noDot || lessThanThreeDigitsAfterDot) && amountBelowLimit) {
                    etAmount.text.append(p0.toString())
                }
            }

            override fun onLeftAuxButtonClicked() {
                if (!etAmount.text.contains(".")) {
                    etAmount.text.append(".")
                }
            }

            override fun onRightAuxButtonClicked() {
                val amountRepr = etAmount.text.toString()
                if (amountRepr.isNotEmpty()) {
                    etAmount.setText(amountRepr.substring(0, amountRepr.lastIndex))
                }
                if (etAmount.text.isEmpty()) {
                    etAmount.setText("0")
                }
            }
        })
        btnSelectDate.setOnClickListener {
            showDateDialog()
        }
        showSelectedDate()
    }

    private fun showDateDialog() {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val dateChangeCallback = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            calendar.set(y, m, d)
            showSelectedDate()
        }
        val dpk = DatePickerDialog(this, dateChangeCallback, year, month, day)
        dpk.show()
    }

    private fun showSelectedDate() {
        btnSelectDate.text = SimpleDateFormat.getDateInstance().format(calendar.time)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_edit_operation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemDone -> {
                finishEdit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun finishEdit() {
        val amount = BigDecimal(etAmount.text.toString())
        val currency = if (spinnerCurrency.selectedItem.toString() == "$") Currency.DOLLAR else Currency.RUBLE
        val date = calendar.time
        val operationType = spinnerOperationType.selectedItem.toString()
        val category = Category(spinnerCategory.selectedItem.toString(), R.drawable.ic_github)

        val operation: Operation = when (operationType) {
            getString(R.string.incomings) ->
                IncomingsOperation(Money(amount, currency), category, date, Account("John Smith"))
            getString(R.string.outgoings) ->
                OutgoingsOperation(Money(amount, currency), category, date, Account("John Smith"))
            else -> throw Exception("Wrong operation type $operationType")
        }
        presenter.saveOperation(operation)
    }

    override fun closeEditWindow() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    companion object {

        fun newIntent(context: Context, type: String) =
                with(Intent(context, EditOperationActivity::class.java)) {
                    putExtra(ARG_OPERATION_TYPE, type)
                }!!
    }
}
