package com.albertoalbaladejo.cabify.ui.basketshopping

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.databinding.FragmentBasketShoppingBinding
import com.albertoalbaladejo.cabify.databinding.LayoutLoaderBinding
import com.albertoalbaladejo.cabify.utils.BasketShoppingDataStatus
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasketShoppingFragment : Fragment() {

    private lateinit var binding: FragmentBasketShoppingBinding
    private lateinit var basketShoppingItemAdapter: BasketShoppingItemAdapter
    private lateinit var concatAdapter: ConcatAdapter

    private val basketShoppingViewModel: BasketShoppingViewModel by viewModels()

    private val resumePriceItemAdapter: ResumePriceItemAdapter by lazy {
        ResumePriceItemAdapter(
            requireContext(),
            basketShoppingViewModel.getItemsCount(),
            basketShoppingViewModel.getItemsPriceTotal(),
            basketShoppingViewModel.getItemsDiscountPrice()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketShoppingBinding.inflate(layoutInflater)

        setViews()
        setObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        basketShoppingViewModel.getBasketShopping()
    }

    private fun navigateToOrderSuccess() {
        val action =
            BasketShoppingFragmentDirections.actionBasketShoppingFragmentToOrderSuccessFragment()
        findNavController().navigate(action)
        basketShoppingViewModel.finalizeOrder()
    }

    private fun setViews() {
        with(binding) {
            loaderLayout.loaderFrameLayout.visibility = View.VISIBLE
            basketCheckOutBtn.setOnClickListener {
                navigateToOrderSuccess()
            }

            setBasketShoppingAdapter(basketShoppingViewModel.basketShoppingItem.value)

            val concatAdapter = ConcatAdapter(basketShoppingItemAdapter, resumePriceItemAdapter)
            binding.basketShoppingProductsRecyclerView.adapter = concatAdapter
        }
    }

    private fun setObservers() {
        basketShoppingViewModel.basketShoppingProduct.observe(viewLifecycleOwner) {
            setBasketShoppingAdapter(it)
            concatAdapter = ConcatAdapter(
                basketShoppingItemAdapter, ResumePriceItemAdapter(
                    requireContext(),
                    basketShoppingViewModel.getItemsCount(),
                    basketShoppingViewModel.getItemsPriceTotal(),
                    basketShoppingViewModel.getItemsDiscountPrice()
                )
            )
            binding.basketShoppingProductsRecyclerView.adapter = concatAdapter
        }

        basketShoppingViewModel.dataStatus.observe(viewLifecycleOwner) { status ->
            val isDataLoading = status == BasketShoppingDataStatus.LOADING
            with(binding) {
                basketShoppingProductsRecyclerView.visibility =
                    if (isDataLoading) View.GONE else View.VISIBLE
                basketCheckOutBtn.visibility = if (isDataLoading) View.GONE else View.VISIBLE
                basketShoppingGroup.visibility = if (isDataLoading) View.GONE else View.VISIBLE
                loaderLayout.loaderFrameLayout.visibility =
                    if (isDataLoading) View.VISIBLE else View.GONE
                if (isDataLoading) {
                    loaderLayout.circularLoader.showAnimationBehavior
                } else {
                    loaderLayout.circularLoader.hideAnimationBehavior
                }
            }

            val itemList = basketShoppingViewModel.basketShoppingProduct.value ?: emptyList()
            if (itemList.isNotEmpty()) {
                updateAdapter()
                showProductState(true)
            } else {
                with(binding) {
                    showProductState(false)
                    loaderLayout.loaderFrameLayout.visibility = View.GONE
                    loaderLayout.circularLoader.hideAnimationBehavior
                }
            }
        }

        basketShoppingViewModel.basketShoppingItem.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                updateAdapter()
            }
        }

        basketShoppingViewModel.priceList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                updateAdapter()
            }
        }

        basketShoppingViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.loaderLayout.loaderFrameLayout.isVisible = it
        }
    }

    private fun showProductState(isVisible: Boolean) {
        with(binding) {
            basketShoppingGroup.isVisible = !isVisible
            basketShoppingProductsRecyclerView.isVisible = isVisible
            basketCheckOutBtn.isVisible = isVisible
        }
    }

    /*private fun updateAdapter() {
        val basketList = basketShoppingViewModel.basketShoppingItem.value ?: emptyList()
        basketShoppingItemAdapter.submitList(basketList)
        concatAdapter.notifyDataSetChanged()
    }*/

    private fun updateAdapter() {
        val basketList = basketShoppingViewModel.basketShoppingItem.value ?: emptyList()
        basketShoppingItemAdapter.submitList(basketList)

        concatAdapter = ConcatAdapter(
            basketShoppingItemAdapter,
            ResumePriceItemAdapter(
                requireContext(),
                basketShoppingViewModel.getItemsCount(),
                basketShoppingViewModel.getItemsPriceTotal(),
                basketShoppingViewModel.getItemsDiscountPrice()
            )
        )
        binding.basketShoppingProductsRecyclerView.adapter = concatAdapter
        binding.basketShoppingProductsRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setBasketShoppingAdapter(basketShopping: List<BasketShoppingEntity>?) {
        basketShoppingItemAdapter =
            BasketShoppingItemAdapter(requireContext(), createBasketClickListener())
        basketShoppingItemAdapter.submitList(basketShopping)
    }

    private fun createBasketClickListener() =
        object : BasketShoppingItemAdapter.OnClickListener {
            override fun onDeleteClick(itemId: String, itemBinding: LayoutLoaderBinding) {
                showDeleteDialog(itemId, itemBinding)
            }

            override fun onPlusClick(itemId: String) {
                basketShoppingViewModel.setQuantityOfItem(itemId, INCREASE)
            }

            override fun onMinusClick(
                itemId: String,
                currQuantity: Int,
                itemBinding: LayoutLoaderBinding
            ) {
                if (currQuantity == 1) {
                    showDeleteDialog(itemId, itemBinding)
                } else {
                    basketShoppingViewModel.setQuantityOfItem(itemId, DIMINISH)
                }
            }
        }

    private fun showDeleteDialog(code: String, itemBinding: LayoutLoaderBinding) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.delete_dialog_title_text)
                .setMessage(R.string.delete_cart_item_message_text)
                .setNegativeButton(R.string.basket_shopping_dialog_cancel_btn, null)
                .setPositiveButton(R.string.delete_dialog_delete_btn_text) { dialog, _ ->
                    basketShoppingViewModel.deleteItemFromCart(code)
                    dialog.dismiss()
                }
                .setOnCancelListener { itemBinding.loaderFrameLayout.visibility = View.GONE }
                .show()
                .apply {
                    getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                        dismiss()
                        itemBinding.loaderFrameLayout.visibility = View.GONE
                    }
                }
        }
    }

    companion object {
        const val INCREASE = 1
        const val DIMINISH = -1
    }
}