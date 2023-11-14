import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.rizkyilmann_challange4.data.repository.CartRepository
import com.catnip.rizkyilmann_challange4.model.DetailMenu
import com.catnip.rizkyilmann_challange4.ui.detailactivity.DetailActivity
import com.catnip.rizkyilmann_challange4.utils.ResultWrapper
import kotlinx.coroutines.launch

class DetailViewModel(
    private val extras: Bundle?,
    private val cartRepository: CartRepository
) : ViewModel() {
    val product = extras?.getParcelable<DetailMenu>(DetailActivity.EXTRA_PRODUCT)

    val priceLiveData = MutableLiveData<Double>().apply {
        postValue(0.0)
    }

    val productCountLiveData = MutableLiveData<Int>().apply {
        postValue(0)
    }

    private val _addToCartResult = MutableLiveData<ResultWrapper<Boolean>>()
    val addToCartResult: LiveData<ResultWrapper<Boolean>>
        get() = _addToCartResult

    fun add() {
        val count = (productCountLiveData.value ?: 0) + 1
        productCountLiveData.postValue(count)
        priceLiveData.postValue(product?.harga?.times(count) ?: 0.0)
    }

    fun minus() {
        if ((productCountLiveData.value ?: 0) > 0) {
            val count = (productCountLiveData.value ?: 0) - 1
            productCountLiveData.postValue(count)
            priceLiveData.postValue(product?.harga?.times(count) ?: 0.0)
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val productQuantity =
                if ((productCountLiveData.value ?: 0) <= 0) 1 else productCountLiveData.value ?: 0
            product?.let {
                cartRepository.createCart(it, productQuantity).collect { result ->
                    _addToCartResult.postValue(result)
                }
            }
        }
    }
}
