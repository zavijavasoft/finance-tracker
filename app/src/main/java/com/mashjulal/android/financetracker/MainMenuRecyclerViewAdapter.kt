package com.mashjulal.android.financetracker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mashjulal.android.financetracker.financialcalculations.convertRublesToDollars
import kotlinx.android.synthetic.main.item_menu_balance.view.*
import java.math.BigDecimal

// TODO: remove hardcode
private val RUBLES = BigDecimal(1000.1)

// Number of possible action and views on main page
private const val MENU_SIZE = 1

// ViewHolder possible types
private const val TYPE_BALANCE = 0

/**
 * A [RecyclerView.Adapter] subclass.
 * Contains list of different finance actions at main page:
 * balance.
 */
class MainMenuRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            TYPE_BALANCE -> {
                val v = inflater.inflate(R.layout.item_menu_balance, parent, false)
                return BalanceViewHolder(v)
            }
            else -> {
                throw NotImplementedError("Unexpected type $viewType of menu item.")
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
            when (position) {
                0 -> TYPE_BALANCE
                else -> super.getItemViewType(position)
            }

    override fun getItemCount(): Int = MENU_SIZE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BalanceViewHolder -> {
                holder.balanceRubles.text = formatCurrency(RUBLES, "RU")
                holder.balanceDollars.text = formatCurrency(convertRublesToDollars(RUBLES), "US")
            }
        }
    }

    companion object {

        /**
         * A [RecyclerView.ViewHolder] subclass.
         * Represents a view with user's current balance in rubles and dollars.
         */
        class BalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val balanceRubles: TextView = itemView.tvBalanceRubles
            val balanceDollars: TextView = itemView.tvBalanceDollars
        }
    }
}