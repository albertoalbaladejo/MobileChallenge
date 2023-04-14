package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.utils.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetBasketShoppingByIdUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetBasketShoppingByIdUseCase

    @Before
    fun setUp() {
        repository = mock(ProductRepository::class.java)
        useCase = GetBasketShoppingByIdUseCase(repository)
    }

    @Test
    fun `invoke should return a BasketShoppingEntity if repository returns a Success`() = runTest {
        // Given
        val code = "TSHIRT"
        val basketShoppingEntity = BasketShoppingEntity(code, "Product 1", 10.0, 2)
        whenever(useCase.invoke(code)).thenReturn(
            Result.Success(
                basketShoppingEntity
            )
        )

        // When
        val result = useCase(code)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(basketShoppingEntity, (result as Result.Success).data)
    }
}