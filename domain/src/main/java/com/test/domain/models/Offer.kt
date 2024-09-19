package com.test.domain.models


data class Offer(
    val id: String? = null,
    val title: String,
    val link: String,
    val button: Button? = null
)