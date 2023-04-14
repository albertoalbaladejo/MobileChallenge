package com.albertoalbaladejo.cabify.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.albertoalbaladejo.cabify.databinding.FragmentOrdersBinding
import com.albertoalbaladejo.cabify.utils.BasketShoppingDataStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var ordersAdapter: OrdersAdapter

    private val ordersViewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(layoutInflater)

        setViews()
        setObservers()

        return binding.root
    }

    private fun setViews() {
        binding.loaderLayout.loaderFrameLayout.visibility = View.GONE
        binding.ordersGroup.visibility = View.GONE
        ordersAdapter = OrdersAdapter(requireContext())
        binding.orderAllOrdersRecyclerView.adapter = ordersAdapter
    }

    private fun setObservers() {
        ordersViewModel.getAllOrders()

        ordersViewModel.dataStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                BasketShoppingDataStatus.LOADING -> {
                    binding.orderAllOrdersRecyclerView.visibility = View.GONE
                    binding.ordersGroup.visibility = View.GONE
                    binding.loaderLayout.loaderFrameLayout.visibility = View.VISIBLE
                    binding.loaderLayout.circularLoader.showAnimationBehavior
                }
                else -> {
                    binding.loaderLayout.circularLoader.hideAnimationBehavior
                    binding.loaderLayout.loaderFrameLayout.visibility = View.GONE
                }
            }

            if (status != BasketShoppingDataStatus.LOADING) {
                ordersViewModel.ordersPlaced.observe(viewLifecycleOwner) { orders ->
                    if (orders.isNullOrEmpty()) {
                        binding.loaderLayout.circularLoader.hideAnimationBehavior
                        binding.loaderLayout.loaderFrameLayout.visibility = View.GONE
                        binding.ordersGroup.visibility = View.VISIBLE
                    } else {
                        ordersAdapter.submitList(orders.sortedByDescending { it.orderDate })
                        binding.orderAllOrdersRecyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}