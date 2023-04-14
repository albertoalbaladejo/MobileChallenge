package com.albertoalbaladejo.cabify.ui.basketshopping

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.databinding.LayoutPriceCardBinding

class ResumePriceItemAdapter(
    private val context: Context,
    private val itemsCount: Int,
    private val itemsPriceTotal: Double,
    private val itemsDiscountPrice: Double
) : RecyclerView.Adapter<ResumePriceItemAdapter.ViewHolder>() {

    inner class ViewHolder(private val priceCardBinding: LayoutPriceCardBinding) :
        RecyclerView.ViewHolder(priceCardBinding.root) {

        fun bind() {
            val itemsLabelText =
                context.resources.getString(R.string.price_card_items_string, itemsCount)
            val itemsAmountText = context.resources.getString(R.string.price_text, itemsPriceTotal)
            val discountAmountText =
                context.resources.getString(R.string.price_text, itemsDiscountPrice)
            val totalAmountText = context.resources.getString(
                R.string.price_text,
                itemsPriceTotal - itemsDiscountPrice
            )

            with(priceCardBinding) {
                priceItemsLabelTv.text = itemsLabelText
                priceItemsAmountTv.text = itemsAmountText
                priceDiscountAmountTv.text = discountAmountText
                priceTotalAmountTv.text = totalAmountText
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutPriceCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = 1
}