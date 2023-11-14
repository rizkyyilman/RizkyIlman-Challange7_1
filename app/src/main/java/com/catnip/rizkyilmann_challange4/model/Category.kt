package com.catnip.rizkyilmann_challange4.model

import java.util.UUID

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val categoryImgUrl: String,
    val name: String?,
    val slug: String
)
