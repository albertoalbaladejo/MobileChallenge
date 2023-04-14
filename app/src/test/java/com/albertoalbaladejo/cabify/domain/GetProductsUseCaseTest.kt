package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import com.albertoalbaladejo.cabify.data.model.ProductItem
import com.albertoalbaladejo.cabify.data.model.Products
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: ProductRepository

    lateinit var useCase: GetProductsUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        useCase = GetProductsUseCase(repository)
    }

    @Test
    fun `when the api doesnt return anything then get values from database`() = runBlocking {
        // Given
        coEvery { repository.getAllProductsFromApi() } returns Products(emptyList())

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.getAllProductsFromDatabase() }
        coVerify(exactly = 0) { repository.clearProducts() }
        coVerify(exactly = 0) { repository.insertProducts(any()) }
    }

    @Test
    fun `when the api return something then get values from api`() = runBlocking {
        // Given
        val myProductList = listOf(ProductItem("code", "name", 20.0))
        coEvery { repository.getAllProductsFromApi() } returns Products(myProductList)

        // When
        val response = useCase()
        val listProductsEntity: MutableList<ProductEntity> = mutableListOf()
        myProductList.forEach {
            listProductsEntity.add(
                ProductEntity(
                    it.code,
                    it.name,
                    it.price
                )
            )
        }

        // Then
        coVerify(exactly = 1) { repository.clearProducts() }
        coVerify(exactly = 1) { repository.insertProducts(any()) }
        coVerify(exactly = 0) { repository.getAllProductsFromDatabase() }
        assert(listProductsEntity == response)
    }
}