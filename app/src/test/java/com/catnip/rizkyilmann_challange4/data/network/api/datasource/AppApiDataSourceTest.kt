package com.catnip.rizkyilmann_challange4.data.network.api.datasource

import com.catnip.rizkyilmann_challange4.network.api.datasource.AppApiDataSource
import com.catnip.rizkyilmann_challange4.network.api.datasource.AppDataSource
import com.catnip.rizkyilmann_challange4.network.api.model.category.CategoriesResponse
import com.catnip.rizkyilmann_challange4.network.api.model.order.OrderRequest
import com.catnip.rizkyilmann_challange4.network.api.model.order.OrderResponse
import com.catnip.rizkyilmann_challange4.network.api.model.product.ProductsResponse
import com.catnip.rizkyilmann_challange4.network.api.service.AppApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AppApiDataSourceTest {

    @MockK
    lateinit var service: AppApiService

    private lateinit var dataSource: AppDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = AppApiDataSource(service)
    }

    @Test
    fun getProducts() {
        runTest {
            val mockResponse = mockk<ProductsResponse>()
            coEvery { service.getProducts(any()) } returns mockResponse
            val response = dataSource.getProducts("foods")
            coVerify { service.getProducts(any()) }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun getCategories() {
        runTest {
            val mockResponse = mockk<CategoriesResponse>(relaxed = true)
            coEvery { service.getCategories() } returns mockResponse
            val response = dataSource.getCategories()
            coVerify { service.getCategories() }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            coEvery { service.createOrder(any()) } returns mockResponse
            val response = dataSource.createOrder(mockRequest)
            coVerify { service.createOrder(any()) }
            assertEquals(response, mockResponse)
        }
    }
}
