package com.albertoalbaladejo.cabify.strategy

// Interfaz que define las funciones que se implementarán
// en las clases de estrategias de los descuentos.
interface DiscountStrategy {

    // Esta función indica el literal del descuento que se aplica al producto.
    fun getLiteralDiscount(): Int

    // Esta función indica si se tiene que mostrar el literal que muestra el descuento.
    fun showLiteralDiscount(): Int

    // Esta función indica si se tiene que mostrar el precio original tachado.
    fun showStrikethroughPrice(quantity: Int): Int

    // Esta función devuelve la imagen del producto.
    fun getProductDrawable(): Int

    // Esta función indica el precio original del producto.
    fun setDiscountText(originalPrice: Double, quantity: Int): Double

    // Esta función indica el precio del producto con el descuento aplicado.
    fun setDiscount(originalPrice: Double, quantity: Int): Double
}