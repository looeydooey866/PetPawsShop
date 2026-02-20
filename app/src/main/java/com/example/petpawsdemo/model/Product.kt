package com.example.petpawsdemo.model

import kotlinx.serialization.Serializable
import kotlin.math.round

@Serializable
data class Review(
    val username: String,
    val profilePicture: String,
    val rating: Double,
    val review: String,
)

@Serializable
data class Product(
    val name: String,
    val productCategory: ProductCategory,
    val tags: List<String>,
    var stock: Int,
    val price: Int,
    private var baseRating: Double = 0.0,
    var rates: Int = 0,
    //val reviews: MutableList<Review> = mutableListOf(),
    val images: List<String>,
    val brand: String,
    val size: Float,
    val description: String,
    val discount: Double = 0.0,
    var overallRating: Double = 0.0
) {
    init {
        require(name.length <= 50) {"The name of the product cannot be more than 100 characters."}
    }

    val rating: Double
        get() {
            val id = ProductDatabase.getID(this) ?: return baseRating
            val reviews = ReviewsDatabase.getReviewsByProductId(id)
            if (reviews.isNullOrEmpty()) return baseRating
            val avg = reviews.map { it.rating }.average()
            return round(avg * 10) / 10.0
        }
}