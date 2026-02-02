package com.example.petpawsdemo.model

data class Review(
    val username: String,
    val profilePicture: String,
    val rating: Double,
    val review: String,
)

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
) {
    init {
        require(name.length <= 50) {"The name of the product cannot be more than 100 characters."}
    }
}