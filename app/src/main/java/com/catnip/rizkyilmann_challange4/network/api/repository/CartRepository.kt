package com.catnip.rizkyilmann_challange4.network.api.repository

import com.catnip.rizkyilmann_challange4.data.database.datasource.CartDataSource
import com.catnip.rizkyilmann_challange4.data.database.entity.CartEntity
import com.catnip.rizkyilmann_challange4.data.mapper.toCartEntity
import com.catnip.rizkyilmann_challange4.data.mapper.toCartList
import com.catnip.rizkyilmann_challange4.model.Cart
import com.catnip.rizkyilmann_challange4.model.DetailMenu
import com.catnip.rizkyilmann_challange4.network.api.datasource.AppDataSource
import com.catnip.rizkyilmann_challange4.network.api.model.order.OrderItemRequest
import com.catnip.rizkyilmann_challange4.network.api.model.order.OrderRequest
import com.catnip.rizkyilmann_challange4.utils.ResultWrapper
import com.catnip.rizkyilmann_challange4.utils.proceed
import com.catnip.rizkyilmann_challange4.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface CartRepository {
    fun getUserCardData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>>
    suspend fun createCart(product: DetailMenu, totalQuantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>>
    suspend fun deleteAll()
}

class CartRepositoryImpl(
    private val dataSource: CartDataSource,
    private val appCh5DataSource: AppDataSource
) : CartRepository {

    override suspend fun deleteAll() {
        dataSource.deleteAll()
    }

    override fun getUserCardData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>> {
        return dataSource.getAllCarts()
            .map {
                proceed {
                    val result = it.toCartList()
                    val totalPrice = result.sumOf {
                        val pricePerItem = it.productPrice
                        val quantity = it.itemQuantity
                        pricePerItem * quantity
                    }
                    Pair(result, totalPrice)
                }
            }.map {
                if (it.payload?.first?.isEmpty() == true) {
                    ResultWrapper.Empty(it.payload)
                } else {
                    it
                }
            }
            .onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override suspend fun createCart(
        product: DetailMenu,
        totalQuantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return product.id?.let { productId ->
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(
                        productId = productId,
                        itemQuantity = totalQuantity,
                        productImgUrl = product.imgUrl,
                        productName = product.name,
                        productPrice = product.harga
                    )
                )
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Product ID not found")))
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { dataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity += 1
        }
        return proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItems = items.map {
                OrderItemRequest(it.itemNotes, it.productId, it.itemQuantity)
            } // xxx -> ppp
            val orderRequest = OrderRequest(orderItems)
            appCh5DataSource.createOrder(orderRequest).status == true
        }
    }
}
