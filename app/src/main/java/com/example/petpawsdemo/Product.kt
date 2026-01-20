package com.example.petpawsdemo

import androidx.compose.runtime.Composable

data class Product(
    val name: String,
    val tags: List<String>,
    val stock: Int,
    val price: Int,
    val images: List<String>,
    val producer: String,
    val description: String,
    val discount: Double = 0.0,
)