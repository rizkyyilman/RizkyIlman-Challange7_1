package com.catnip.rizkyilmann_challange4.network.api.model.product

import androidx.annotation.Keep
import com.catnip.rizkyilmann_challange4.model.DetailMenu
import com.google.gson.annotations.SerializedName

@Keep
data class ProductItemResponse(
    @SerializedName("detail")
    val desc: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("position")
    val position: Int?,
    @SerializedName("nama")
    val name: String?,
    @SerializedName("harga")
    val price: Double?,
    @SerializedName("image_url")
    val imgUrl: String?
)

fun ProductItemResponse.toProduct() =
    DetailMenu(
        id = this.id,
        name = this.name.orEmpty(),
        harga = this.price ?: 0.0,
        desc = this.desc.orEmpty(),
        imgUrl = this.imgUrl.orEmpty()
    )

fun Collection<ProductItemResponse>.toProductList() = this.map { it.toProduct() }
