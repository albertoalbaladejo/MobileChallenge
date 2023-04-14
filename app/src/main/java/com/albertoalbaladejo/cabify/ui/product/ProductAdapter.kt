package com.albertoalbaladejo.cabify.ui.product

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import com.albertoalbaladejo.cabify.databinding.ProductsItemBinding
import com.albertoalbaladejo.cabify.strategy.DiscountStrategyUtils

class ProductAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener
) : ListAdapter<ProductEntity, ProductAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ProductsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productData: ProductEntity) {
            with(binding) {
                val discountStrategy = DiscountStrategyUtils.getDiscountStrategy(productData.code)

                productCard.setOnClickListener {
                    onClickListener.onClick(productData)
                }
                productNameTv.text = productData.name
                productPriceTv.text =
                    context.getString(
                        R.string.product_details_price_value,
                        productData.price
                    )
                productActualPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                productOfferValueTv.apply {
                    text = discountStrategy.getLiteralDiscount()
                        .let { context.getString(it) }
                    visibility = discountStrategy.showLiteralDiscount()
                }

                productImageView.load(discountStrategy.getProductDrawable()) {
                    crossfade(true)
                }
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem == newItem
        }
    }

    interface OnClickListener {
        fun onClick(productData: ProductEntity)
    }
}