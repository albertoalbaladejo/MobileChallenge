package com.albertoalbaladejo.cabify.ui.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import com.albertoalbaladejo.cabify.domain.GetProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ProductViewModelTest {

    private val getProductsUseCase: GetProductsUseCase = mock(GetProductsUseCase::class.java)
    private lateinit var productViewModel: ProductViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        productViewModel = ProductViewModel(getProductsUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get all products success`() =
        runTest {
            // Given
            val productsList = listOf(mock(ProductEntity::class.java))
            whenever(getProductsUseCase.invoke()).thenReturn(productsList)

            // When
            productViewModel.getAllProducts()

            // Then
            assert(productViewModel.products.value == productsList)
        }

}