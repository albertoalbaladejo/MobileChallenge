package com.albertoalbaladejo.cabify.utils

enum class BasketShoppingDataStatus { LOADING, ERROR, DONE }

internal fun getRandomString(length: Int, uNum: String, endLength: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    fun getStr(l: Int): String = (1..l).map { allowedChars.random() }.joinToString("")
    return getStr(length) + uNum + getStr(endLength)
}