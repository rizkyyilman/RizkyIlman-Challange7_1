package com.catnip.rizkyilmann_challange4.network.api.model.order

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderRequest(
    @SerializedName("orders")
    val orders: List<OrderItemRequest>?
)
