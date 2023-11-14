package com.catnip.rizkyilmann_challange4.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailMenu(
    val id: Int? = null,
    val name: String,
    val harga: Double,
    val imgUrl: String,
    val desc: String
) : Parcelable
