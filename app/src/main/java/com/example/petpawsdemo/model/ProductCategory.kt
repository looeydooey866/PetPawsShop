package com.example.petpawsdemo.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductCategory (
    val type: String,
    val subtype: String,
)