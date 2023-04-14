package com.albertoalbaladejo.cabify.ui.productdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.databinding.FragmentProductDetailsBinding
import com.albertoalbaladejo.cabify.strategy.DiscountStrategyUtils
import com.albertoalbaladejo.cabify.utils.Extensions.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

private const val QUANTITY = 1

@AndroidEntryPoint
class ProductDetailsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var productSelectedCode: String

    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val navArgs by navArgs<ProductDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater)

        setObservers()
        setViews()

        return binding.root
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    private fun setObservers() {
        productDetailsViewModel.checkIfInCart(navArgs.code)
        productDetailsViewModel.getProductsById(navArgs.code)

        productDetailsViewModel.isItemInCart.observe(viewLifecycleOwner) { isItemInCart ->
            binding.productDetailsAddCartBtn.text = getString(
                if (isItemInCart) R.string.product_details_button_text_go_to_cart
                else R.string.product_details_button_text_add_to_cart
            )
        }

        productDetailsViewModel.products.observe(viewLifecycleOwner) { productSelected ->
            with(binding) {
                val discountStrategy =
                    DiscountStrategyUtils.getDiscountStrategy(productSelected.code)
                productDetailsTitleTv.text = productSelected.name
                productDetailsOfferValueTv.apply {
                    text = discountStrategy.getLiteralDiscount()
                        .let { context.getString(it) }
                    visibility = discountStrategy.showLiteralDiscount()
                }
                productDetailsPriceTv.text = requireContext().getString(
                    R.string.product_details_price_tv_text,
                    productSelected.price
                )

                productDetailsImageView.load(discountStrategy.getProductDrawable()) {
                    crossfade(true)
                    placeholder(R.drawable.ic_image_loading_slider)
                }

                productDetailsAddCartBtn.setOnClickListener {
                    val isItemInCart = productDetailsViewModel.isItemInCart.value
                    if (isItemInCart == true) {
                        findNavController().navigate(
                            ProductDetailsFragmentDirections.actionProductDetailsFragmentToBasketShoppingFragment()
                        )
                    } else {
                        productSelectedCode = productSelected.code
                        productDetailsViewModel.saveProduct(
                            BasketShoppingEntity(
                                productSelected.code,
                                productSelected.name,
                                productSelected.price,
                                QUANTITY
                            )
                        )
                    }
                }
            }
        }

        productDetailsViewModel.isSuccessfullySaved.observe(viewLifecycleOwner) { isSuccessfullySaved ->
            val messageResId = if (isSuccessfullySaved) {
                productDetailsViewModel.checkIfInCart(productSelectedCode)
                R.string.toast_message_add_product_basket_shopping
            } else {
                R.string.toast_message_add_product_basket_shopping_error
            }
            toast(getString(messageResId))
            dismiss()
        }

        productDetailsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loaderLayout.circularLoader.showAnimationBehavior
            binding.loaderLayout.loaderFrameLayout.isVisible = isLoading
        }
    }

    private fun setViews() {
        binding.loaderLayout.loaderFrameLayout.visibility = View.VISIBLE
    }
}