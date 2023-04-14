package com.albertoalbaladejo.cabify.ui.orders

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import com.albertoalbaladejo.cabify.domain.GetAllOrdersUseCase
import com.albertoalbaladejo.cabify.utils.BasketShoppingDataStatus
import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class OrdersViewModelTest {

    private val getAllOrdersUseCase: GetAllOrdersUseCase =
        mock(GetAllOrdersUseCase::class.java)

    private lateinit var viewModel: OrdersViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        viewModel = OrdersViewModel(getAllOrdersUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getAllOrdersUseCase returns non-empty list, then dataStatus should be DONE`() =
        runTest {
            // Given
            val orders = listOf(mock(OrdersEntity::class.java))
            whenever(getAllOrdersUseCase.invoke()).thenReturn(orders)

            // When
            viewModel.getAllOrders()

            // Then
            assertEquals(BasketShoppingDataStatus.DONE, viewModel.dataStatus.value)
            assertEquals(orders, viewModel.ordersPlaced.value)
        }

    @Test
    fun `when getAllOrdersUseCase returns empty list, then dataStatus should be ERROR`() = runTest {
        // Given
        whenever(getAllOrdersUseCase.invoke()).thenReturn(emptyList())

        // When
        viewModel.getAllOrders()

        // Then
        assertEquals(BasketShoppingDataStatus.ERROR, viewModel.dataStatus.value)
        assertEquals(emptyList<OrdersEntity>(), viewModel.ordersPlaced.value)
    }
}