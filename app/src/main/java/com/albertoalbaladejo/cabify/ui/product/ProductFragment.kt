package com.albertoalbaladejo.cabify.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import com.albertoalbaladejo.cabify.databinding.FragmentProductBinding
import com.albertoalbaladejo.cabify.databinding.ItemSliderImageBinding
import com.albertoalbaladejo.cabify.ui.RecyclerViewPaddingItemDecoration
import com.albertoalbaladejo.cabify.utils.DataSet
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var productAdapter: ProductAdapter

    private val productViewModel: ProductViewModel by viewModels()
    private val gridLayoutManager by lazy {
        GridLayoutManager(requireContext(), 2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel.getAllProducts()
        setObservers()
        setViews()
        setSliderImage()
    }


    private fun setObservers() {
        productViewModel.products.observe(viewLifecycleOwner) {
            setProductsAdapter(it)
        }

        productViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.loaderLayout.loaderFrameLayout.isVisible = it
        }
    }

    private fun setProductsAdapter(productsList: List<ProductEntity>?) {
        productAdapter = ProductAdapter(requireContext(), createProductClickListener())
        productAdapter.submitList(productsList)
        binding.productsRecyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = productAdapter
            val itemDecoration = RecyclerViewPaddingItemDecoration()
            if (itemDecorationCount == 0) {
                addItemDecoration(itemDecoration)
            }
        }
    }

    private fun createProductClickListener() = object : ProductAdapter.OnClickListener {
        override fun onClick(productData: ProductEntity) {
            val action =
                ProductFragmentDirections.actionProductFragmentToProductDetailsFragment(
                    productData.code
                )
            val navOptions = navOptions {
                popUpTo(R.id.productDetailsFragment) {
                    inclusive = true
                }
            }
            findNavController().navigate(action, navOptions)
        }
    }

    private fun setViews() {
        binding.loaderLayout.loaderFrameLayout.visibility = View.VISIBLE
        binding.loaderLayout.circularLoader.showAnimationBehavior
    }

    private fun setSliderImage() {
        with(binding) {
            carouselImage.registerLifecycle(lifecycle)

            // Custom view
            carouselImage.carouselListener = object : CarouselListener {
                override fun onCreateViewHolder(
                    layoutInflater: LayoutInflater,
                    parent: ViewGroup
                ): ViewBinding {
                    return ItemSliderImageBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                }

                override fun onBindViewHolder(
                    binding: ViewBinding,
                    item: CarouselItem,
                    position: Int
                ) {
                    val currentBinding = binding as ItemSliderImageBinding

                    currentBinding.imageView.apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP

                        setImage(item, R.drawable.ic_image_loading_slider)
                    }
                }
            }

            carouselImage.setData(DataSet.images.map { CarouselItem(it) })

            // Custom indicator
            carouselImage.setIndicator(customIndicator)
        }
    }
}