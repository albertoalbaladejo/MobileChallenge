package com.albertoalbaladejo.cabify.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import com.albertoalbaladejo.cabify.domain.GetAllOrdersUseCase
import com.albertoalbaladejo.cabify.utils.BasketShoppingDataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getAllOrdersUseCase: GetAllOrdersUseCase
) : ViewModel() {

    private val _ordersPlaced = MutableLiveData<List<OrdersEntity>?>()
    val ordersPlaced: LiveData<List<OrdersEntity>?> get() = _ordersPlaced

    private val _dataStatus = MutableLiveData<BasketShoppingDataStatus>()
    val dataStatus: LiveData<BasketShoppingDataStatus> get() = _dataStatus

    fun getAllOrders() {
        viewModelScope.launch {
            _dataStatus.value = BasketShoppingDataStatus.LOADING
            viewModelScope.launch {
                val result = getAllOrdersUseCase()

                if (result.isNotEmpty()) {
                    _ordersPlaced.value = result
                    _dataStatus.value = BasketShoppingDataStatus.DONE
                } else {
                    _ordersPlaced.value = emptyList()
                    _dataStatus.value = BasketShoppingDataStatus.ERROR
                }
            }
        }
    }
}