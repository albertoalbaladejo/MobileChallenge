package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetProductByIdUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetProductByIdUseCase

    private val code = "TSHIRT"

    @Before
    fun setUp() {
        repository = mock(ProductRepository::class.java)
        useCase = GetProductByIdUseCase(repository)
    }

    @Test
    fun `should return product entity`() = runTest {
        // Given
        val product = mock(ProductEntity::class.java)
        whenever(repository.getProductById(code)).thenReturn(product)

        // When
        val result = useCase(code)

        // Then
        assertEquals(product, result)
        verify(repository, times(1)).getProductById(code)
    }
}