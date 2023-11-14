package com.catnip.rizkyilmann_challange4.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.rizkyilmann_challange4.data.repository.ProductRepository
import com.catnip.rizkyilmann_challange4.model.Category
import com.catnip.rizkyilmann_challange4.model.DetailMenu
import com.catnip.rizkyilmann_challange4.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _categories = MutableLiveData<ResultWrapper<List<Category>>>()
    val categories: LiveData<ResultWrapper<List<Category>>>
        get() = _categories

    private val _products = MutableLiveData<ResultWrapper<List<DetailMenu>>>()
    val products: LiveData<ResultWrapper<List<DetailMenu>>>
        get() = _products

    private val _currentCategorySlug = MutableLiveData<String>()
    val currentCategorySlug: LiveData<String> get() = _currentCategorySlug

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories().collect {
                _categories.postValue(it)
            }
        }
    }

    fun getProducts(category: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProducts(if (category == "all") null else category?.lowercase()).collect {
                _products.postValue(it)
            }
        }
    }
}
