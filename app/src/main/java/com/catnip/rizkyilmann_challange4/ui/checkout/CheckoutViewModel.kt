package com.catnip.rizkyilmann_challange4.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.catnip.rizkyilmann_challange4.data.repository.CartRepository
import com.catnip.rizkyilmann_challange4.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutViewModel(private val cartRepository: CartRepository) : ViewModel() {
    val cartList = cartRepository.getUserCardData().asLiveData(Dispatchers.IO)

    private val _checkoutResult = MutableLiveData<ResultWrapper<Boolean>>()
    val checkoutResult: LiveData<ResultWrapper<Boolean>>
        get() = _checkoutResult

    fun order() {
        viewModelScope.launch(Dispatchers.IO) {
            val carts = cartList.value?.payload?.first ?: return@launch
            // tidak akan membuat order ketika list of cart nya null
            cartRepository.order(carts).collect {
                _checkoutResult.postValue(it)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) {
        }
    }
}
