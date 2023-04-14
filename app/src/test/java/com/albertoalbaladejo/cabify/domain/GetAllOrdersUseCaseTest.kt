package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetAllOrdersUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetAllOrdersUseCase

    @Before
    fun setUp() {
        repository = mock(ProductRepository::class.java)
        useCase = GetAllOrdersUseCase(repository)
    }

    @Test
    fun `invoke should return list of orders entity`() = runTest {
        // Given
        val orders = listOf(mock(OrdersEntity::class.java))
        whenever(repository.getAllOrdersProduct()).thenReturn(orders)

        // When
        val result = useCase()

        // Then
        assertEquals(orders, result)
    }
}