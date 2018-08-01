package com.mashjulal.android.financetracker.presentation.editoperation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
private const val ARG_OPERATION_ACCOUNT = "account"

private const val MAX_AMOUNT_DIGIT_COUNT = 12

class EditOperationActivity : AppCompatActivity(), EditOperationPresenter.View {

    @Inject
    lateinit var presenter: EditOperationPresenter
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var accountName: String
    private lateinit var operationType: String
    private lateinit var categories: Map<OperationType, List<Category>>
    private lateinit var accounts: List<Account>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_operation)

        App.appComponent.inject(this)
        initLayout()
        parseArgs()
        setResult(Activity.RESULT_CANCELED)
    }

    private fun initLayout() {
        numberKeyboard.setListener(object : NumberKeyboardListener {
            override fun onNumberClicked(number: Int) {
                val amount = etAmount.text.toString()

                if (amount == "0") {
                    if (number != 0) {
                        etAmount.setText(number.toString())
                    }
                    return
                }

                val doteIndex = amount.indexOf(".")
                val noDot = doteIndex == -1
                val lessThanThreeDigitsAfterDot = doteIndex > amount.length - 3
                val amountBelowLimit = amount.length < MAX_AMOUNT_DIGIT_COUNT

                if ((noDot || lessThanThreeDigitsAfterDot) && amountBelowLimit) {
                    etAmount.text.append(number.toString())
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
        presenter.requestData()
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

    private fun parseArgs() {
        operationType = intent.getStringExtra(ARG_OPERATION_TYPE)
        spinnerOperationType.setSelection(if (operationType == OperationType.INCOMINGS.name) 0 else 1)

        accountName = intent.getStringExtra(ARG_OPERATION_ACCOUNT)
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
        let {
            val amount = BigDecimal(etAmount.text.toString())
            val currency = if (spinnerCurrency.selectedItem.toString() == "$") Currency.DOLLAR else Currency.RUBLE
            val date = calendar.time
            val operationTypeRepr = spinnerOperationType.selectedItem as String
            val operationType = if (operationTypeRepr == getString(R.string.incomings)) OperationType.INCOMINGS
            else OperationType.OUTGOINGS
            val account = Account(spinnerAccount.selectedItem as String)
            val category = categories[operationType]
                    ?.find { it.title == spinnerCategory.selectedItem as String }
                    ?: throw Exception("Category can't be null")
            val operation = when (operationType) {
                OperationType.INCOMINGS -> IncomingsOperation(Money(amount, currency), category, date, account)
                OperationType.OUTGOINGS -> OutgoingsOperation(Money(amount, currency), category, date, account)
            }
            presenter.saveOperation(operation)
        }
    }

    override fun setData(accounts: List<Account>, categories: Map<OperationType, List<Category>>) {
        setOperationTypes()
        setAccounts(accounts)
        setCategories(categories)
    }

    private fun setOperationTypes() {
        spinnerOperationType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?,
                                        position: Int, id: Long) {
                val operation = spinnerOperationType.selectedItem
                val operationType = if (operation == getString(R.string.incomings)) OperationType.INCOMINGS
                else OperationType.OUTGOINGS

                spinnerCategory.adapter = ArrayAdapter(this@EditOperationActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        categories[operationType]?.map { it.title })
                spinnerCategory.setSelection(0)
            }

        }
    }

    private fun setAccounts(data: List<Account>) {
        this.accounts = data
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, accounts.map { it.title })
        spinnerAccount.adapter = adapter
        val position = adapter.getPosition(accountName)
        spinnerAccount.setSelection(position)
    }

    private fun setCategories(categories: Map<OperationType, List<Category>>) {
        this.categories = categories
        val operation = OperationType.valueOf(operationType)
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, categories[operation].orEmpty()
                .map { it.title })
        spinnerCategory.adapter = adapter
        spinnerCategory.setSelection(0)
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

        fun newIntent(context: Context, type: OperationType, account: String) =
                with(Intent(context, EditOperationActivity::class.java)) {
                    putExtra(ARG_OPERATION_TYPE, type.name)
                    putExtra(ARG_OPERATION_ACCOUNT, account)
                }!!
    }
}
