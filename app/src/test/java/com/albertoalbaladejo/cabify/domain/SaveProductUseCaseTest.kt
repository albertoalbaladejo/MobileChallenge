package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class SaveProductUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: ProductRepository
    
    private lateinit var useCase: SaveProductUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = SaveProductUseCase(repository)
    }

    @Test
    fun `invoke with valid data should return true`() = runBlocking {
        // Given
        val product = mock(BasketShoppingEntity::class.java)
        coEvery { repository.insertProduct(product) } just runs

        // When
        val result = useCase(product)

        // Then
        coVerify(exactly = 1) { repository.insertProduct(product) }
        assertTrue(result)
    }

    @Test
    fun `invoke with invalid data should return false`() = runBlocking {
        // Given
        val product = mockk<BasketShoppingEntity>()
        val exception = RuntimeException("Error inserting product")
        coEvery { repository.insertProduct(product) } throws exception

        // When
        val result = useCase(product)

        // Then
        coVerify(exactly = 1) { repository.insertProduct(product) }
        assertFalse(result)
    }
}