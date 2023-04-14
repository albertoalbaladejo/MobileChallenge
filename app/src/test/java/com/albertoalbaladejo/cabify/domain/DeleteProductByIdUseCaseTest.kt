package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.utils.Result.Error
import com.albertoalbaladejo.cabify.utils.Result.Success
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class DeleteProductByIdUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: DeleteProductByIdUseCase

    @Before
    fun setUp() {
        repository = Mockito.mock(ProductRepository::class.java)
        useCase = DeleteProductByIdUseCase(repository)
    }

    @Test
    fun `invoke should return success when deleteProductById is successful`() = runTest {
        // Given
        val code = "TSHIRT"
        whenever(repository.deleteProductById(code)).thenReturn(null)

        // When
        val result = useCase(code)

        // Then
        verify(repository).deleteProductById(code)
        assertTrue(result is Success)
        assertEquals(true, (result as Success).data)
    }

    @Test
    fun `invoke should return error when deleteProductById throws an exception`() = runTest {
        // Given
        val code = "TSHIRT"
        val exception = RuntimeException("Something went wrong")
        whenever(repository.deleteProductById(code)).thenThrow(exception)

        // When
        val result = useCase(code)

        // Then
        verify(repository).deleteProductById(code)
        assertTrue(result is Error)
        assertEquals(exception, (result as Error).exception)
    }
}