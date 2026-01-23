package com.example.petpawsdemo.ProductClasses

data class Review(
    val rating: Int,
    val review: String,
)
/*
data class Product(
    val name: String,
    val type: String,
    val subtype: String,
    val tags: List<String>,
    val stock: Int,
    val price: Int,
    val rating: Double,
    val rates: Int,
    val reviews: List<Review>,
    val images: List<String>,
    val producer: String,
    val description: String,
    val discount: Double = 0.0
)

 */
data class Product(
    val name: String,
    val productCategory: ProductCategory,
    val tags: List<String>,
    val stock: Int,
    val price: Int,
    val rating: Double,
    val rates: Int,
    val reviews: List<Review>,
    val images: List<String>,
    val producer: String,
    val description: String,
    val discount: Double = 0.0
)