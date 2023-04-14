package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class UpdateProductUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: ProductRepository

    private lateinit var updateProductUseCase: UpdateProductUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateProductUseCase = UpdateProductUseCase(repository)
    }

    @Test
    fun `invoke with valid product should return true`() = runTest {
        // Given
        val product = mock(BasketShoppingEntity::class.java)
        coEvery { repository.updateProduct(product) } just runs

        // When
        val result = updateProductUseCase(product)

        // Then
        assertTrue(result)
        coVerify { repository.updateProduct(product) }
    }

    @Test
    fun `invoke with invalid product should return false`() = runTest {
        // Given
        val product = mock(BasketShoppingEntity::class.java)
        val exception = mock(Exception::class.java)
        coEvery { repository.updateProduct(product) } throws exception

        // When
        val result = updateProductUseCase(product)

        // Then
        assertFalse(result)
        coVerify { repository.updateProduct(product) }
    }
}