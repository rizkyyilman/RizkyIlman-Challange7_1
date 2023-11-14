package com.catnip.rizkyilmann_challange4.network.api.datasource

import com.catnip.rizkyilmann_challange4.network.api.model.category.CategoriesResponse
import com.catnip.rizkyilmann_challange4.network.api.model.order.OrderRequest
import com.catnip.rizkyilmann_challange4.network.api.model.order.OrderResponse
import com.catnip.rizkyilmann_challange4.network.api.model.product.ProductsResponse
import com.catnip.rizkyilmann_challange4.network.api.service.AppApiService

interface AppDataSource {
    suspend fun getProducts(category: String? = null): ProductsResponse
    suspend fun getCategories(): CategoriesResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class AppApiDataSource(private val service: AppApiService) : AppDataSource {
    override suspend fun getProducts(category: String?): ProductsResponse {
        return service.getProducts(category)
    }

    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.createOrder(orderRequest)
    }
}
