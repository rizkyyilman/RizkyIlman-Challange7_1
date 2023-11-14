package com.catnip.rizkyilmann_challange4.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.catnip.rizkyilmann_challange4.databinding.FragmentHomeBinding
import com.catnip.rizkyilmann_challange4.ui.detailactivity.DetailActivity
import com.catnip.rizkyilmann_challange4.ui.home.adapter.category.CategoryAdapter
import com.catnip.rizkyilmann_challange4.ui.home.adapter.product.ProductAdapter
import com.catnip.rizkyilmann_challange4.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter { selectedCategory ->
            viewModel.getProducts(selectedCategory.slug)
        }
    }

    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter { product ->
            DetailActivity.startActivity(requireContext(), product)
        }
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        getData()
    }

    private fun getData() {
        viewModel.getCategories()
        viewModel.getProducts()
    }

    private fun observeData() {
        viewModel.categories.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                binding.layoutStateCategory.root.isVisible = false
                binding.layoutStateCategory.pbLoading.isVisible = false
                binding.layoutStateCategory.tvError.isVisible = false
                binding.rvCategory.apply {
                    isVisible = true
                    adapter = categoryAdapter
                }
                it.payload?.let { data -> categoryAdapter.submitData(data) }
            }, doOnLoading = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = true
                    binding.layoutStateCategory.tvError.isVisible = false
                    binding.rvCategory.isVisible = false
                }, doOnError = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutStateCategory.tvError.isVisible = true
                    binding.layoutStateCategory.tvError.text = it.exception?.message.orEmpty()
                    binding.rvCategory.isVisible = false
                })
        }
        viewModel.products.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                binding.layoutStateProduct.root.isVisible = false
                binding.layoutStateProduct.pbLoading.isVisible = false
                binding.layoutStateProduct.tvError.isVisible = false
                binding.rvMenu.apply {
                    isVisible = true
                    adapter = productAdapter
                }
                it.payload?.let {
                        data ->
                    productAdapter.submitData(data)
                }
            }, doOnLoading = {
                    binding.layoutStateProduct.root.isVisible = true
                    binding.layoutStateProduct.pbLoading.isVisible = true
                    binding.layoutStateProduct.tvError.isVisible = false
                    binding.rvMenu.isVisible = false
                }, doOnError = {
                    binding.layoutStateProduct.root.isVisible = true
                    binding.layoutStateProduct.pbLoading.isVisible = false
                    binding.layoutStateProduct.tvError.isVisible = true
                    binding.layoutStateProduct.tvError.text = it.exception?.message.orEmpty()
                    binding.rvMenu.isVisible = false
                }, doOnEmpty = {
                    binding.layoutStateProduct.root.isVisible = true
                    binding.layoutStateProduct.pbLoading.isVisible = false
                    binding.layoutStateProduct.tvError.isVisible = true
                    binding.layoutStateProduct.tvError.text = "Product not found"
                    binding.rvMenu.isVisible = false
                })
        }
    }
}
