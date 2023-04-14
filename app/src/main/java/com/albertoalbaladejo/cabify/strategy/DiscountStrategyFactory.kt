package com.albertoalbaladejo.cabify.strategy

import com.albertoalbaladejo.cabify.strategy.product.MugDiscountStrategy
import com.albertoalbaladejo.cabify.strategy.product.NoDiscountStrategy
import com.albertoalbaladejo.cabify.strategy.product.TshirtDiscountStrategy
import com.albertoalbaladejo.cabify.strategy.product.VoucherDiscountStrategy

// Objeto Singleton que se utiliza para crear instancias de las diferentes
// estrategias de descuento para los diferentes productos.
object DiscountStrategyFactory {
    fun getDiscountStrategy(code: String): DiscountStrategy {
        return when (code) {
            "VOUCHER" -> VoucherDiscountStrategy()
            "TSHIRT" -> TshirtDiscountStrategy()
            "MUG" -> MugDiscountStrategy()
            else -> NoDiscountStrategy()
        }
    }
}