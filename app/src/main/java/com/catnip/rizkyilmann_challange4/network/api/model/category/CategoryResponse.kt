package com.catnip.rizkyilmann_challange4.network.api.model.category

import androidx.annotation.Keep
import com.catnip.rizkyilmann_challange4.model.Category
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("image_url")
    val imgUrl: String?,
    @SerializedName("nama")
    val name: String?,
    @SerializedName("slug")
    val slug: String?
)

fun CategoryResponse.toCategory() = Category(
    id = this.id.orEmpty(),
    categoryImgUrl = this.imgUrl.orEmpty(),
    name = this.name.orEmpty(),
    slug = this.slug.orEmpty()
)

fun Collection<CategoryResponse>.toCategoryList() = this.map {
    it.toCategory()
}
