package com.mashjulal.android.financetracker.presentation.editoperation


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatFragment
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

class AddOperationFragment : MvpAppCompatFragment(), EditOperationPresenter.View {

    companion object {

        const val FRAGMENT_TAG = "ADD_OPERATION_FRAGMENT_TAG"

        private const val OPERATION_TYPE_PARAM = "OPERATION_TYPE_PARAM"
        private const val ACCOUNT_PARAM = "ACCOUNT_TYPE_PARAM"

        @JvmStatic
        fun newInstance(account: String, operationType: String) =
                AddOperationFragment().apply {
                    arguments = Bundle().apply {
                        putString(OPERATION_TYPE_PARAM, operationType)
                        putString(ACCOUNT_PARAM, account)
                    }
                }
    }


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
        super.onCreate(savedInstanceState)
        arguments?.let {
            operationType = it.getString(OPERATION_TYPE_PARAM)
            accountName = it.getString(ACCOUNT_PARAM)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initLayout()
    }

    private fun initLayout() {
        btnSelectDate.setOnClickListener {
            showDateDialog()
        }
        showSelectedDate()
        presenter.requestData()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_activity_edit_operation, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemDone -> {
                finishEdit()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showDateDialog() {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val dateChangeCallback = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            calendar.set(y, m, d)
            showSelectedDate()
        }
        val dpk = DatePickerDialog(activity?.applicationContext, dateChangeCallback, year, month, day)
        dpk.show()
    }

    private fun showSelectedDate() {
        btnSelectDate.text = SimpleDateFormat.getDateInstance().format(calendar.time)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_edit_operation, container, false)

    }


    override fun onAttach(context: Context?) {
        App.appComponent.inject(this)
        super.onAttach(context)
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
            val account = Account(UITextDecorator.mapUsableToSpecial(activity?.applicationContext, spinnerAccount.selectedItem as String))
            val specialCategoryName = UITextDecorator.mapUsableToSpecial(activity?.applicationContext, spinnerCategory.selectedItem as String)
            val category = categories[operationType]
                    ?.find { it.title == specialCategoryName }
                    ?: throw Exception("Category can't be null")
            val operation = Operation(date.time, Money(amount, currency), category, date, account)
            presenter.saveOperation(operation)
        }
    }

    override fun closeEditWindow() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

                spinnerCategory.adapter = ArrayAdapter(activity?.applicationContext,
                        android.R.layout.simple_spinner_dropdown_item,
                        categories[operationType]?.map { it.title })
                spinnerCategory.setSelection(0)
            }

        }
    }

    private fun setAccounts(data: List<Account>) {
        this.accounts = data
        val adapter = ArrayAdapter(activity?.applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                accounts.map { UITextDecorator.mapSpecialToUsable(activity?.applicationContext, it.title) })
        spinnerAccount.adapter = adapter
        val position = adapter.getPosition(accountName)
        spinnerAccount.setSelection(position)
    }

    private fun setCategories(categories: Map<OperationType, List<Category>>) {
        this.categories = categories
        val operation = OperationType.valueOf(operationType)
        val adapter = ArrayAdapter(activity?.applicationContext,
                android.R.layout.simple_spinner_dropdown_item, categories[operation].orEmpty()
                .map { UITextDecorator.mapSpecialToUsable(activity?.applicationContext, it.title) })
        spinnerCategory.adapter = adapter
        spinnerCategory.setSelection(0)
    }

}
