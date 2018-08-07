package com.mashjulal.android.financetracker.presentation.editoperation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.financetracker.App
import com.mashjulal.android.financetracker.R
import com.mashjulal.android.financetracker.domain.financialcalculations.*
import com.mashjulal.android.financetracker.domain.financialcalculations.Currency
import com.mashjulal.android.financetracker.presentation.utils.UITextDecorator
import kotlinx.android.synthetic.main.activity_edit_operation.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val ARG_OPERATION_TYPE = "type"
private const val ARG_OPERATION_ACCOUNT = "account"

private const val MAX_AMOUNT_DIGIT_COUNT = 12

class EditOperationActivity : MvpAppCompatActivity(), EditOperationPresenter.View {

    @Inject
    @InjectPresenter
    lateinit var presenter: EditOperationPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var accountName: String
    private lateinit var operationType: String
    private lateinit var categories: Map<OperationType, List<Category>>
    private lateinit var accounts: List<Account>

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_operation)

        initLayout()
        parseArgs()
        setResult(Activity.RESULT_CANCELED)
    }

    private fun initLayout() {
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

        accountName = UITextDecorator.mapSpecialToUsable(applicationContext, intent.getStringExtra(ARG_OPERATION_ACCOUNT))
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
            if (amount == BigDecimal.ZERO) return@let
            val currency = if (spinnerCurrency.selectedItem.toString() == "$") Currency.DOLLAR else Currency.RUBLE
            val date = calendar.time
            val operationTypeRepr = spinnerOperationType.selectedItem as String
            val operationType = if (operationTypeRepr == getString(R.string.incomings)) OperationType.INCOMINGS
            else OperationType.OUTGOINGS
            val account = Account(UITextDecorator.mapUsableToSpecial(applicationContext, spinnerAccount.selectedItem as String))
            val specialCategoryName = UITextDecorator.mapUsableToSpecial(applicationContext, spinnerCategory.selectedItem as String)
            val category = categories[operationType]
                    ?.find { it.title == specialCategoryName }
                    ?: throw Exception("Category can't be null")
            val operation = Operation(date.time, Money(amount, currency), category, date, account)
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
                android.R.layout.simple_spinner_dropdown_item,
                accounts.map { UITextDecorator.mapSpecialToUsable(applicationContext, it.title) })
        spinnerAccount.adapter = adapter
        val position = adapter.getPosition(accountName)
        spinnerAccount.setSelection(position)
    }

    private fun setCategories(categories: Map<OperationType, List<Category>>) {
        this.categories = categories
        val operation = OperationType.valueOf(operationType)
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, categories[operation].orEmpty()
                .map { UITextDecorator.mapSpecialToUsable(applicationContext, it.title) })
        spinnerCategory.adapter = adapter
        spinnerCategory.setSelection(0)
    }

    override fun closeEditWindow() {
        setResult(Activity.RESULT_OK)
        finish()
    }


    companion object {

        fun newIntent(context: Context, type: OperationType, account: String) =
                with(Intent(context, EditOperationActivity::class.java)) {
                    putExtra(ARG_OPERATION_TYPE, type.name)
                    putExtra(ARG_OPERATION_ACCOUNT, account)
                }!!
    }
}
