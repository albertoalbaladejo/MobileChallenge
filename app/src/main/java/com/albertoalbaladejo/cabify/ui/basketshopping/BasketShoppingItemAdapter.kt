package com.albertoalbaladejo.cabify.ui.basketshopping

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.databinding.BasketShoppingListItemBinding
import com.albertoalbaladejo.cabify.databinding.LayoutLoaderBinding
import com.albertoalbaladejo.cabify.strategy.DiscountStrategyUtils.getDiscountStrategy

class BasketShoppingItemAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener
) : ListAdapter<BasketShoppingEntity, BasketShoppingItemAdapter.ViewHolder>(
    BasketShoppingEntityDiffCallback()
) {

    inner class ViewHolder(private val binding: BasketShoppingListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(basketData: BasketShoppingEntity) {
            with(binding) {
                val discountStrategy = getDiscountStrategy(basketData.code)
                loaderLayout.loaderFrameLayout.visibility = View.GONE

                basketProductTitleTv.text = basketData.name
                basketProductPriceTv.text = HtmlCompat.fromHtml(
                    context.getString(
                        R.string.product_details_price_value,
                        discountStrategy.setDiscountText(basketData.price, basketData.quantity)
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

                // Lógica del precio descontado y mostrar texto
                basketProductDiscountPromotionTv.apply {
                    text = discountStrategy.getLiteralDiscount()
                        .let { context.getString(it) }
                    visibility = discountStrategy.showLiteralDiscount()
                }

                basketProductPriceDiscountTv.apply {
                    visibility = discountStrategy.showStrikethroughPrice(basketData.quantity)
                    text = HtmlCompat.fromHtml(
                        context.getString(
                            R.string.basket_product_price_discount_text,
                            basketData.price
                        ),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }

                basketProductQuantityTextView.text = basketData.quantity.toString()

                basketProductImageView.load(discountStrategy.getProductDrawable()) {
                    crossfade(true)
                }

                basketProductDeleteBtn.setOnClickListener {
                    loaderLayout.loaderFrameLayout.visibility = View.VISIBLE
                    onClickListener.onDeleteClick(basketData.code, loaderLayout)
                }
                basketProductPlusBtn.setOnClickListener {
                    loaderLayout.loaderFrameLayout.visibility = View.VISIBLE
                    onClickListener.onPlusClick(basketData.code)
                }
                basketProductMinusBtn.setOnClickListener {
                    loaderLayout.loaderFrameLayout.visibility = View.VISIBLE
                    onClickListener.onMinusClick(
                        basketData.code,
                        basketData.quantity,
                        loaderLayout
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BasketShoppingListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<BasketShoppingEntity>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    interface OnClickListener {
        fun onDeleteClick(itemId: String, itemBinding: LayoutLoaderBinding)
        fun onPlusClick(itemId: String)
        fun onMinusClick(
            itemId: String,
            currQuantity: Int,
            itemBinding: LayoutLoaderBinding
        )
    }

    class BasketShoppingEntityDiffCallback : DiffUtil.ItemCallback<BasketShoppingEntity>() {
        override fun areItemsTheSame(
            oldItem: BasketShoppingEntity,
            newItem: BasketShoppingEntity
        ): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(
            oldItem: BasketShoppingEntity,
            newItem: BasketShoppingEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}