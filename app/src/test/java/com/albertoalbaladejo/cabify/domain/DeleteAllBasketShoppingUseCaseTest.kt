package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.utils.Result.Error
import com.albertoalbaladejo.cabify.utils.Result.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DeleteAllBasketShoppingUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: DeleteAllBasketShoppingUseCase

    @Before
    fun setUp() {
        repository = mock(ProductRepository::class.java)
        useCase = DeleteAllBasketShoppingUseCase(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoke should return Success when repository clears basket successfully`() = runTest {
        // Given
        whenever(repository.clearBasketShopping()).thenReturn(Unit)

        // When
        val result = useCase.invoke()

        // Then
        assertEquals(Success(true), result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoke should return Error when repository throws unexpected exception`() = runTest {
        // Given
        val unexpectedException = RuntimeException("Unexpected exception")
        whenever(repository.clearBasketShopping()).thenThrow(unexpectedException)

        // When
        val result = useCase.invoke()

        // Then
        assertEquals(Error(unexpectedException), result)
    }
}