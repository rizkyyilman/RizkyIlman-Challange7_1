package com.catnip.rizkyilmann_challange4.network.api.service

import com.catnip.rizkyilmann_challange4.BuildConfig
import com.catnip.rizkyilmann_challange4.network.api.model.category.CategoriesResponse
import com.catnip.rizkyilmann_challange4.network.api.model.order.OrderRequest
import com.catnip.rizkyilmann_challange4.network.api.model.order.OrderResponse
import com.catnip.rizkyilmann_challange4.network.api.model.product.ProductsResponse
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface AppApiService {

    @GET("listmenu")
    suspend fun getProducts(@Query("c") category: String? = null): ProductsResponse

    @GET("category")
    suspend fun getCategories(): CategoriesResponse

    @GET("order")
    suspend fun createOrder(@Body orderRequest: OrderRequest): OrderResponse

    companion object {
        @JvmStatic
        operator fun invoke(chucker: ChuckerInterceptor): AppApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(chucker)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(AppApiService::class.java)
        }
    }
}
