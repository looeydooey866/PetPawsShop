package com.example.petpawsdemo.model

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import java.io.File

const val GUEST_USERNAME = "Guest"
const val GUEST_PASSWORD = "=LNVQ4RfWnhK~V*TA!28K=P^YJ8.jfhcUuU~=gcr^KNUhCg*4+NF~0T+k%aeu]45oz+9jpRERWjE]K_o57GiCxtRB>jfUGqrv^*B"
const val DEFAULT_PFP = "https://imgur.com/a/gjPAUwF"

object UserProfileObject {
    private val userProfile = UserProfile(
        userName = GUEST_USERNAME,
        userPfpReference = DEFAULT_PFP,
        password = GUEST_PASSWORD,
        darkmode = false,
    )

    var userName: String
        get() = userProfile.userName
        set(value) { userProfile.userName = value }

    var password: String
        get() = userProfile.password
        set(value) { userProfile.password = value }

    var userPfpReference: String
        get() = userProfile.userPfpReference
        set(value) { userProfile.userPfpReference = value }

    var darkmode: Boolean
        get() = userProfile.darkmode
        set(value) { userProfile.darkmode = value }

    fun addPurchasedItem(id: Int) {
        userProfile.addPurchasedItemById(id)
    }

    fun getPurchasedItems(): List<Product> =
        userProfile.getPurchasedItems()

    /*
    suspend fun addReviewToProduct(context: Context, product: Product, rating: Double, reviewText: String) {
        val newReview = Review(
            username = userName,
            profilePicture = userPfpReference,
            rating = rating,
            review = reviewText
        )
        
        product.reviews.add(newReview)
        product.updateRating(rating)

        ProductDatabase.updateProduct(product)
        
        // No longer saving to JSON as reviews are transient
        // userProfile.updatePurchasedProduct(product)
        // saveUserProfile(context)
    }
     */

    /*
    suspend fun removeReviewFromProduct(context: Context, product: Product, review: Review) {
        product.reviews.remove(review)

        ProductDatabase.updateProduct(product)

        // No longer saving to JSON as reviews are transient
        // userProfile.updatePurchasedProduct(product)
        // saveUserProfile(context)
    }
     */

    suspend fun loadUserProfile(context: Context, username: String): Boolean {
        val success = userProfile.loadUserProfile(context, username)
        if (!success) {
            userProfile.loadGuestProfile(context)
            return true
        } else
            return false
    }

    suspend fun saveUserProfile(context: Context) {
        userProfile.saveUserProfile(context)
    }

    suspend fun saveCurrentUser(context: Context) {
        val session = AppSession(userName)

        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, "app_session.json")
            file.writeText(json.encodeToString(session))
        }
    }

    suspend fun loadCurrentUser(context: Context): String? {
        return withContext(Dispatchers.IO) {
            val file = File(context.filesDir, "app_session.json")
            if (!file.exists()) return@withContext null

            val text = file.readText()
            if (text.isBlank()) return@withContext null

            try {
                val session = json.decodeFromString<AppSession>(text)
                if (session.currentUser.isBlank()) {
                    GUEST_USERNAME
                } else {
                    session.currentUser
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}