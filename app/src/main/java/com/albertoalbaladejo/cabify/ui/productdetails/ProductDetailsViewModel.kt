package com.albertoalbaladejo.cabify.ui.productdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import com.albertoalbaladejo.cabify.domain.GetBasketShoppingByIdInProductUseCase
import com.albertoalbaladejo.cabify.domain.GetProductByIdUseCase
import com.albertoalbaladejo.cabify.domain.SaveProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getBasketShoppingByIdUseCase: GetBasketShoppingByIdInProductUseCase,
    private val saveProductUseCase: SaveProductUseCase
) : ViewModel() {

    private val _products = MutableLiveData<ProductEntity>()
    val products: LiveData<ProductEntity> get() = _products

    private val _isItemInCart = MutableLiveData<Boolean>()
    val isItemInCart: LiveData<Boolean> get() = _isItemInCart

    private val _isSuccessfullySaved = MutableLiveData<Boolean>()
    val isSuccessfullySaved: LiveData<Boolean> get() = _isSuccessfullySaved

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getProductsById(code: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getProductByIdUseCase(code)
            _products.value = result
            _isLoading.value = false
        }
    }

    fun saveProduct(product: BasketShoppingEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    saveProductUseCase(product)
                }
            }
            _isSuccessfullySaved.value = result.isSuccess
            _isLoading.value = false
        }
    }

    fun checkIfInCart(code: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getBasketShoppingByIdUseCase.invoke(code)
            _isItemInCart.value = result?.code == code
            _isLoading.value = false
        }
    }
}