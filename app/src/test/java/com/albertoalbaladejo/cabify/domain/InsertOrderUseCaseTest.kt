package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class InsertOrderUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: ProductRepository

    private lateinit var useCase: InsertOrderUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = InsertOrderUseCase(repository)
    }

    @Test
    fun `insertOrder() returns true when repository insertOrder() succeeds`() = runBlocking {
        // Given
        val order = mock(OrdersEntity::class.java)
        coEvery { repository.insertOrder(order) } just runs

        // When
        val result = useCase(order)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { repository.insertOrder(order) }
    }

    @Test
    fun `insertOrder() returns false when repository insertOrder() throws an exception`() =
        runBlocking {
            // Given
            val order = mock(OrdersEntity::class.java)
            coEvery { repository.insertOrder(order) } throws Exception("Insertion failed")

            // When
            val result = useCase(order)

            // Then
            assertFalse(result)
            coVerify(exactly = 1) { repository.insertOrder(order) }
        }
}