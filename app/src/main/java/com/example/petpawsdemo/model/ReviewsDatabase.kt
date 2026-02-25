package com.example.petpawsdemo.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class ExampleReviews {
    companion object {
        val PetPawsDogCageMaxied = Review(
            "Maxied",
            "https://cdn.discordapp.com/avatars/1086855106764476416/287fe77c49ed23c0146c46266ce98555.webp?size=100",
            4.8,
            "I think that this dog cage is splendid! It perfectly suits my dog, and he is very happy living in it for most of the day. He especially enjoys the spaciousness of it. Overall, a fantastic product."
        )
        val PetPawsDogCageMrBurg = Review(
            "Mr. Burg",
            "https://images.themodernproper.com/production/posts/2016/ClassicCheeseBurger_9.jpg?w=1200&h=1200&q=60&fm=jpg&fit=crop&dm=1749310239&s=463b18fc3bb51dc5d96e866c848527c4",
            2.5,
            "I did not really enjoy this product, since the cage is not very aesthetically pleasing. If you want to find a good-looking cage, you might want to look for other cages instead."
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