package com.example.petpawsdemo.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class ExampleReviews {
    companion object {
        val PetPawsDogCageMaxied = Review(
            "Maxied",
            "https://cdn.discordapp.com/avatars/1086855106764476416/287fe77c49ed23c0146c46266ce98555.webp?size=100",
            5.0,
            "I think that this dog cage is splendid! It perfectly suits my dog, and he is very happy living in it for 25 hours a day. He especially enjoys the spaciousness of it, as described under the imaginary 'product size' description. Overall, a fantastic product."
        )
        val PetPawsDogCageMrBurg = Review(
            "Mr. Burg",
            "https://images.themodernproper.com/production/posts/2016/ClassicCheeseBurger_9.jpg?w=1200&h=1200&q=60&fm=jpg&fit=crop&dm=1749310239&s=463b18fc3bb51dc5d96e866c848527c4",
            2.5,
            "My dog doesn't fit inside. Why would you sell a 1x1x2cm dog cage???"
        )
    }
}

object ReviewsDatabase{
    private val reviews = mutableStateMapOf<Int, SnapshotStateList<Review>>()

    fun getReviewsByProductId(id: Int): List<Review>? = reviews[id]

    fun getRatingForProduct(id: Int): Double {
        val productReviews = reviews[id]
        if (productReviews.isNullOrEmpty()) {
            return 0.0
        }

        var sum = 0.0
        for (i in productReviews) sum += i.rating
        return sum / productReviews.size
    }

    fun addReview(id: Int, review: Review) {
        if (!reviews.containsKey(id)) {
            reviews[id] = mutableStateListOf()
        }
        reviews[id]!!.add(review)
    }

    fun removeReview(id: Int, review: Review){
        reviews[id]?.remove(review)
    }

    init {
        val exampleData = listOf(
            ProductDatabase.getID(ExampleProducts.PetPawsDogCage) to ExampleReviews.PetPawsDogCageMaxied,
            ProductDatabase.getID(ExampleProducts.PetPawsDogCage) to ExampleReviews.PetPawsDogCageMrBurg
        )

        for ((id, review) in exampleData) {
            id?.let { addReview(it, review) }
        }
    }
}