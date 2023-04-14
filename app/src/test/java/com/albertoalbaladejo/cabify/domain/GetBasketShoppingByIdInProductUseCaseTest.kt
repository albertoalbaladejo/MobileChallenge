package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetBasketShoppingByIdInProductUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetBasketShoppingByIdInProductUseCase

    @Before
    fun setup() {
        repository = mock(ProductRepository::class.java)
        useCase = GetBasketShoppingByIdInProductUseCase(repository)
    }

    @Test
    fun `invoke should return BasketShoppingEntity`() = runTest {
        // Given
        val basketShoppingEntity = BasketShoppingEntity("TSHIRT", "product", 10.0, 2)
        whenever(repository.getBasketShoppingProductById("TSHIRT")).thenReturn(basketShoppingEntity)

        // When
        val result = useCase.invoke("TSHIRT")

        // Then
        assertEquals(basketShoppingEntity, result)
    }
}