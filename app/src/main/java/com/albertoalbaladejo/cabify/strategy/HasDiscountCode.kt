package com.albertoalbaladejo.cabify.strategy

// Esta interfaz define el método getCode().
interface HasDiscountCode {

    // Esta función devuelve el código de descuento correspondiente al objeto.
    fun getCode(): String
}