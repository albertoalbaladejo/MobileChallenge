package com.albertoalbaladejo.cabify.strategy

object DiscountStrategyUtils {

    /**
     * Esta función recibe el código de descuento y devuelve la clase de la estrategia correspondiente.
     *
     * @param code código de la estrategia.
     * @return la clase de la estrategia correspondiente.
     */
    fun getDiscountStrategy(code: String): DiscountStrategy =
        DiscountStrategyFactory.getDiscountStrategy(code)
}