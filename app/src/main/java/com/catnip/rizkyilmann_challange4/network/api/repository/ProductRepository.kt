package com.catnip.rizkyilmann_challange4.network.api.repository

import com.catnip.rizkyilmann_challange4.model.Category
import com.catnip.rizkyilmann_challange4.model.DetailMenu
import com.catnip.rizkyilmann_challange4.network.api.datasource.AppDataSource
import com.catnip.rizkyilmann_challange4.network.api.model.category.toCategoryList
import com.catnip.rizkyilmann_challange4.network.api.model.product.toProductList
import com.catnip.rizkyilmann_challange4.utils.ResultWrapper
import com.catnip.rizkyilmann_challange4.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
    fun getProducts(category: String? = null): Flow<ResultWrapper<List<DetailMenu>>>
}
class ProductRepositoryImpl2(
    private val apiDataSource: AppDataSource
) : ProductRepository {

    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            apiDataSource.getCategories().data?.toCategoryList() ?: emptyList()
        }
    }

    override fun getProducts(category: String?): Flow<ResultWrapper<List<DetailMenu>>> {
        return proceedFlow {
            (apiDataSource.getProducts(category).data?.toProductList() ?: emptyList()) as List<DetailMenu>
        }
    }
}
