package com.albertoalbaladejo.cabify.strategy.product

import android.view.View
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.strategy.DiscountStrategy

// Implementación de DiscountStrategy para el producto TSHIRT
class TshirtDiscountStrategy : DiscountStrategy {

    companion object {
        private const val DISCOUNT_AMOUNT_TSHIRT = 1.0
        private const val POLICY_TSHIRT_QUANTITY = 3
    }

    // Esta función indica el literal del descuento que se aplica al producto.
    override fun getLiteralDiscount(): Int = R.string.product_discount_bulk_info

    // Esta función indica si se tiene que mostrar el literal que muestra el descuento.
    override fun showLiteralDiscount(): Int = View.VISIBLE

    // Esta función indica si se tiene que mostrar el precio original tachado.
    override fun showStrikethroughPrice(quantity: Int): Int {
        return if (quantity >= POLICY_TSHIRT_QUANTITY) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    // Esta función devuelve la imagen del producto.
    override fun getProductDrawable(): Int = R.drawable.t_shirt

    // Esta función indica el precio original del producto.
    override fun setDiscountText(originalPrice: Double, quantity: Int): Double {
        return if (canApplyDiscount(quantity)) {
            originalPrice - DISCOUNT_AMOUNT_TSHIRT
        } else {
            originalPrice
        }
    }

    // Esta función indica el precio del producto con el descuento aplicado.
    override fun setDiscount(originalPrice: Double, quantity: Int): Double {
        return if (canApplyDiscount(quantity)) {
            discountTotalAmountTshirt(quantity)
        } else {
            0.0
        }
    }

    // Esta función devuelve el valor sobre si se puede aplicar
    // el descuento en base a la cantidad del producto.
    private fun canApplyDiscount(quantity: Int): Boolean {
        return quantity >= POLICY_TSHIRT_QUANTITY
    }

    // Esta función devuelve el valor total del descuento que se va a aplicar
    // sobre la cantidad de productos.
    private fun discountTotalAmountTshirt(quantity: Int): Double {
        return quantity * DISCOUNT_AMOUNT_TSHIRT
    }
}