package com.albertoalbaladejo.cabify.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import com.albertoalbaladejo.cabify.domain.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private var _products = MutableLiveData<List<ProductEntity>>()
    val products: LiveData<List<ProductEntity>> get() = _products

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getAllProducts() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = getProductsUseCase()

            _products.postValue(result)
            _isLoading.postValue(false)
        }
    }
}