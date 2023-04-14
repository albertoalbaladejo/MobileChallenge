package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetAllBasketShoppingUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetAllBasketShoppingUseCase

    @Before
    fun setUp() {
        repository = Mockito.mock(ProductRepository::class.java)
        useCase = GetAllBasketShoppingUseCase(repository)
    }

    @Test
    fun `invoke should return a list of BasketShoppingEntity`() = runTest {
        // Given
        val basketShoppingList = listOf(BasketShoppingEntity("TSHIRT", "Product 1", 10.0, 2))
        whenever(repository.getAllBasketShoppingProduct()).thenReturn(basketShoppingList)

        // When
        val result = useCase()

        // Then
        assertEquals(basketShoppingList, result)
        verify(repository).getAllBasketShoppingProduct()
    }
}