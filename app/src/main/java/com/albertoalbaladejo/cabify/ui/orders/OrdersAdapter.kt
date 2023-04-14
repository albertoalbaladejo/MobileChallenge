package com.albertoalbaladejo.cabify.ui.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import com.albertoalbaladejo.cabify.databinding.LayoutOrderSummaryCardBinding
import java.time.Month
import java.util.*

class OrdersAdapter(private val context: Context) :
    ListAdapter<OrdersEntity, OrdersAdapter.ViewHolder>(OrdersEntityDiffCallback()) {

    inner class ViewHolder(private val binding: LayoutOrderSummaryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderData: OrdersEntity) {
            binding.orderSummaryIdTv.text = orderData.orderId

            val calendar = Calendar.getInstance()
            calendar.time = orderData.orderDate
            binding.orderSummaryDateTv.text =
                context.getString(
                    R.string.order_date_text,
                    calendar.get(Calendar.DAY_OF_MONTH).toString(),
                    Month.values()[(calendar.get(Calendar.MONTH))].name,
                    calendar.get(Calendar.YEAR).toString()
                )
            binding.orderSummaryStatusValueTv.text = orderData.status

            val totalItems = orderData.items.sumOf { it.quantity }
            binding.orderSummaryItemsCountTv.text =
                context.getString(R.string.order_items_count_text, totalItems.toString())

            var totalAmount = 0.0
            orderData.itemsPrices.forEach { (itemId, price) ->
                totalAmount += price * (orderData.items.find { it.code == itemId }?.quantity ?: 1)
            }
            binding.orderSummaryTotalAmountTv.text =
                context.getString(R.string.price_text, totalAmount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutOrderSummaryCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class OrdersEntityDiffCallback : DiffUtil.ItemCallback<OrdersEntity>() {
    override fun areItemsTheSame(oldItem: OrdersEntity, newItem: OrdersEntity): Boolean {
        return oldItem.orderId == newItem.orderId
    }

    override fun areContentsTheSame(oldItem: OrdersEntity, newItem: OrdersEntity): Boolean {
        return oldItem == newItem
    }
}