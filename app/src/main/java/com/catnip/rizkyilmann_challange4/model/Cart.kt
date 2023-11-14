package com.catnip.rizkyilmann_challange4.model

data class Cart(
    var id: Int? = null,
    var productId: Int? = null,
    var itemQuantity: Int = 0,
    val productName: String,
    val productPrice: Double,
    val productImgUrl: String,
    var itemNotes: String? = null
)
