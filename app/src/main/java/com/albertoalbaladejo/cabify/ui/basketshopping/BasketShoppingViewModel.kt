package com.albertoalbaladejo.cabify.ui.basketshopping

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import com.albertoalbaladejo.cabify.domain.*
import com.albertoalbaladejo.cabify.strategy.DiscountStrategyUtils
import com.albertoalbaladejo.cabify.utils.BasketShoppingDataStatus
import com.albertoalbaladejo.cabify.utils.Result.Error
import com.albertoalbaladejo.cabify.utils.Result.Success
import com.albertoalbaladejo.cabify.utils.getRandomString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

private const val TAG = "BasketShoppingViewModel"

@HiltViewModel
class BasketShoppingViewModel @Inject constructor(
    private val getAllBasketShoppingUseCase: GetAllBasketShoppingUseCase,
    private val getBasketShoppingByIdUseCase: GetBasketShoppingByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductByIdUseCase: DeleteProductByIdUseCase,
    private val deleteAllBasketShoppingUseCase: DeleteAllBasketShoppingUseCase,
    private val insertOrderUseCase: InsertOrderUseCase
) : ViewModel() {

    private var _basketShoppingProduct = MutableLiveData<List<BasketShoppingEntity>>()
    val basketShoppingProduct: LiveData<List<BasketShoppingEntity>> get() = _basketShoppingProduct

    private val _basketShoppingItem = MutableLiveData<List<BasketShoppingEntity>>()
    val basketShoppingItem: LiveData<List<BasketShoppingEntity>> get() = _basketShoppingItem

    private val _priceList = MutableLiveData<Map<String, Double>>()
    val priceList: LiveData<Map<String, Double>> get() = _priceList

    private val newOrderData = MutableLiveData<OrdersEntity>()

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _dataStatus = MutableLiveData<BasketShoppingDataStatus>()
    val dataStatus: LiveData<BasketShoppingDataStatus> get() = _dataStatus

    fun getBasketShopping() {
        _dataStatus.value = BasketShoppingDataStatus.LOADING
        viewModelScope.launch {
            val result = getAllBasketShoppingUseCase()

            _basketShoppingItem.value = result.ifEmpty {
                emptyList()
            }
            getAllProductsInCart()

            if (result.isNotEmpty()) {
                _dataStatus.value = BasketShoppingDataStatus.DONE
            } else {
                _dataStatus.value = BasketShoppingDataStatus.ERROR
            }
        }
    }

    private suspend fun getAllProductsInCart() {
        viewModelScope.launch {
            val priceMap = mutableMapOf<String, Double>()
            val productList = mutableListOf<BasketShoppingEntity>()
            var res = true
            _basketShoppingItem.value?.let { itemList ->
                itemList.forEach label@{ item ->
                    val productDeferredRes = async {
                        getBasketShoppingByIdUseCase.invoke(item.code)
                    }
                    val productRes = productDeferredRes.await()
                    if (productRes is Success) {
                        val proData = productRes.data
                        productList.add(proData)
                        priceMap[item.code] = proData.price
                    } else {
                        res = false
                        return@label
                    }
                }
            }
            _priceList.value = priceMap
            _basketShoppingProduct.value = productList
            if (res) {
                _dataStatus.value = BasketShoppingDataStatus.DONE
            } else {
                _dataStatus.value = BasketShoppingDataStatus.ERROR
            }
        }
    }

    fun setQuantityOfItem(code: String, value: Int) {
        viewModelScope.launch {
            _basketShoppingItem.value?.mapIndexed { index, item ->
                if (item.code == code) {
                    item.quantity += value
                    val result = withContext(Dispatchers.IO) {
                        updateProductUseCase(item)
                    }
                    if (result) {
                        val basketList = _basketShoppingItem.value?.toMutableList() ?: mutableListOf()
                        basketList[index] = item
                        _basketShoppingItem.value = basketList
                        _basketShoppingProduct.value = basketList
                        _dataStatus.value = BasketShoppingDataStatus.DONE
                    } else {
                        _dataStatus.value = BasketShoppingDataStatus.ERROR
                    }
                }
            }
        }
    }

    fun deleteItemFromCart(code: String) {
        viewModelScope.launch {
            _dataStatus.value = BasketShoppingDataStatus.LOADING
            _basketShoppingProduct.value?.let { items ->
                val basketShoppingList = items.mapNotNull { if (it.code != code) it else null }
                val deferredRes = async { deleteProductByIdUseCase(code) }
                val res = deferredRes.await()
                if (res is Success) {
                    _basketShoppingItem.value = basketShoppingList
                    _basketShoppingProduct.value = basketShoppingList
                    val priceRes = async { getAllProductsInCart() }
                    priceRes.await()
                } else {
                    _dataStatus.value = BasketShoppingDataStatus.ERROR
                    if (res is Error)
                        Log.d(TAG, "onUpdateQuantity: Error Occurred: ${res.exception.message}")
                }
            }
        }
    }

    fun getItemsCount(): Int {
        return _basketShoppingItem.value?.sumOf { it.quantity } ?: 0
    }

    fun getItemsPriceTotal(): Double {
        var totalPrice = 0.0
        _priceList.value?.forEach { (itemId, price) ->
            totalPrice += price * (_basketShoppingItem.value?.find { it.code == itemId }?.quantity
                ?: 1)
        }
        return totalPrice
    }

    fun getItemsDiscountPrice(): Double {
        return _priceList.value?.entries?.fold(0.0) { acc, (itemId, price) ->
            val discountStrategy = DiscountStrategyUtils.getDiscountStrategy(itemId)
            acc + discountStrategy.setDiscount(
                price,
                (_basketShoppingItem.value?.find { it.code == itemId }?.quantity ?: 1)
            )
        } ?: 0.0
    }

    fun finalizeOrder() {
        val currDate = Date()
        val orderId = getRandomString(6, currDate.time.toString(), 1)
        val items = _basketShoppingItem.value
        val itemPrices = _priceList.value

        if (items.isNullOrEmpty() || itemPrices.isNullOrEmpty()) {
            return
        }

        newOrderData.value = OrdersEntity(orderId, items, itemPrices, currDate)
        insertOrder()
    }

    private fun insertOrder() {
        viewModelScope.launch {
            newOrderData.value?.let { order ->
                runCatching { insertOrderUseCase(order) }
                    .onSuccess {
                        deleteAllBasketShopping()
                    }
                    .onFailure {
                        // Manejar la excepción aquí si es necesario
                    }
            }
        }
    }

    private fun deleteAllBasketShopping() {
        viewModelScope.launch {
            val res = deleteAllBasketShoppingUseCase()
            if (res is Success) {
                _basketShoppingItem.value = emptyList()
                _basketShoppingProduct.value = emptyList()
                _priceList.value = emptyMap()
            }
        }
    }
}