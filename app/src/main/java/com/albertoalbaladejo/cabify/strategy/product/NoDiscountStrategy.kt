package com.albertoalbaladejo.cabify.strategy.product

import android.view.View
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.strategy.DiscountStrategy

// Implementación de DiscountStrategy para productos sin descuento
class NoDiscountStrategy : DiscountStrategy {

    // Esta función indica el literal del descuento que se aplica al producto.
    override fun getLiteralDiscount(): Int = R.string.product_none

    // Esta función indica si se tiene que mostrar el literal que muestra el descuento.
    override fun showLiteralDiscount(): Int = View.GONE

    // Esta función indica si se tiene que mostrar el precio original tachado.
    override fun showStrikethroughPrice(quantity: Int): Int = View.GONE

    // Esta función devuelve la imagen del producto.
    override fun getProductDrawable(): Int = R.drawable.ic_image_loading_slider

    // Esta función indica el precio original del producto.
    override fun setDiscountText(originalPrice: Double, quantity: Int): Double = originalPrice

    // Esta función indica el precio del producto con el descuento aplicado.
    override fun setDiscount(originalPrice: Double, quantity: Int): Double = 0.0
}