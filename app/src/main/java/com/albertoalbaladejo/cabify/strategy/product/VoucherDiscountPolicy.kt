package com.albertoalbaladejo.cabify.strategy.product

import android.view.View
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.strategy.DiscountStrategy

// Implementación de DiscountStrategy para el producto VOUCHER
class VoucherDiscountStrategy : DiscountStrategy {

    // Esta función indica el literal del descuento que se aplica al producto.
    override fun getLiteralDiscount(): Int = R.string.product_discount_2_1_info

    // Esta función indica si se tiene que mostrar el literal que muestra el descuento.
    override fun showLiteralDiscount(): Int = View.VISIBLE

    // Esta función indica si se tiene que mostrar el precio original tachado.
    override fun showStrikethroughPrice(quantity: Int): Int = View.GONE

    // Esta función devuelve la imagen del producto.
    override fun getProductDrawable(): Int = R.drawable.voucher

    // Esta función indica el precio original del producto.
    override fun setDiscountText(originalPrice: Double, quantity: Int): Double = originalPrice

    // Esta función indica el precio del producto con el descuento aplicado.
    override fun setDiscount(originalPrice: Double, quantity: Int): Double {
        return if (canApplyDiscount(quantity)) {
            discountAmountVoucher(originalPrice, quantity, true)
        } else {
            discountAmountVoucher(originalPrice, quantity, false)
        }
    }

    // Esta función devuelve el valor sobre si se puede aplicar
    // el descuento en base a la cantidad del producto.
    private fun canApplyDiscount(quantity: Int): Boolean {
        return quantity % 2 == 0
    }

    // Esta función calcula el descuento total que se aplicaría a una compra según el precio original
    // de cada producto, la cantidad de artículos comprados y el tipo de descuento que se aplica.
    private fun discountAmountVoucher(
        originalPrice: Double,
        quantity: Int,
        isDiscount: Boolean
    ): Double {
        return if (isDiscount) {
            originalPrice * quantity / 2
        } else {
            (originalPrice * (quantity - 1) / 2)
        }
    }
}