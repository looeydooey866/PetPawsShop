package com.example.petpawsdemo.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import java.io.File

val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

@Serializable
data class AppSession(
    val currentUser: String
)

@Serializable
data class UserProfileData(
    val userName: String,
    val userPfpReference: String = "",
    val password: String,
    val darkmode: Boolean = false,
    val firstTimeEntering: Boolean = true,
    val purchasedItems: List<Product> = emptyList()
)

class UserProfile(
    var userName: String,
    var password: String,
    var userPfpReference: String,
    darkmode: Boolean = true,
) {
    companion object {
        var loggedIn: Boolean = false

        fun checkProfileExistsByUsername(context: Context, username: String): Boolean {
            val file = File(context.filesDir, "user_profiles.json")
            if (!file.exists()) return false

            val profiles = try {
                json.decodeFromString(
                    ListSerializer(UserProfileData.serializer()),
                    file.readText()
                )
            } catch (e: Exception) {
                emptyList<UserProfileData>()
            }

            return profiles.any { it.userName == username }
        }

        fun checkPasswordByUsername(context: Context, username: String, password: String): Boolean {
            val file = File(context.filesDir, "user_profiles.json")
            if (!file.exists()) return false

            val profiles = try {
                json.decodeFromString(
                    ListSerializer(UserProfileData.serializer()),
                    file.readText()
                )
            } catch (e: Exception) {
                emptyList<UserProfileData>()
            }

            val user = profiles.find { it.userName == username } ?: return false

            return user.password == password
        }
    }

    private val purchasedItems = mutableListOf<Product>()

    var darkmode by mutableStateOf(darkmode)

    fun addPurchasedItemById(id: Int) {
        val product = ProductDatabase.getProduct(id)
        if (product != null && !purchasedItems.any { it.name == product.name }) {
            purchasedItems.add(product)
        }
    }

    fun addPurchasedItem(p: Product) {
        if (!purchasedItems.any { it.name == p.name }) {
            purchasedItems.add(p)
        }
    }

    fun getPurchasedItems(): List<Product> = purchasedItems.toList()

    fun updatePurchasedProduct(updatedProduct: Product) {
        val index = purchasedItems.indexOfFirst { it.name == updatedProduct.name }
        if (index != -1) {
            purchasedItems[index] = updatedProduct
        }
    }

    fun toData(): UserProfileData = UserProfileData(
        userName = userName,
        userPfpReference = userPfpReference,
        darkmode = darkmode,
        purchasedItems = purchasedItems.toList(),
        password = password
    )

    fun applyData(data: UserProfileData) {
        userName = data.userName
        userPfpReference = data.userPfpReference
        darkmode = data.darkmode
        password = data.password
        purchasedItems.clear()
        purchasedItems.addAll(data.purchasedItems)
    }

    private fun getProfileFile(context: Context): File =
        File(context.filesDir, "user_profiles.json")

    private fun readAllProfiles(context: Context): MutableList<UserProfileData> {
        val file = getProfileFile(context)

        if (!file.exists()) return mutableListOf()

        val text = file.readText()
        if (text.isBlank()) return mutableListOf()

        return try {
            json.decodeFromString(
                ListSerializer(UserProfileData.serializer()),
                text
            ).toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    private fun writeAllProfiles(
        context: Context,
        profiles: List<UserProfileData>
    ) {
        val file = getProfileFile(context)

        val text = json.encodeToString(
            ListSerializer(UserProfileData.serializer()),
            profiles
        )

        file.writeText(text)
    }

    suspend fun saveUserProfile(context: Context) = withContext(Dispatchers.IO) {
        val profiles = readAllProfiles(context)
        val existingIndex =
            profiles.indexOfFirst { it.userName == userName }

        if (existingIndex >= 0) {
            profiles[existingIndex] = toData()
        } else {
            profiles.add(toData())
        }

        writeAllProfiles(context, profiles)
    }

    suspend fun loadUserProfile(context: Context, username: String): Boolean {
        val data = withContext(Dispatchers.IO) {
            val profiles = readAllProfiles(context)
            profiles.find { it.userName == username }
        }
        if (data != null) {
            applyData(data)
            return true
        }
        else return false
    }
    suspend fun loadGuestProfile(context: Context) {
        val guest = withContext(Dispatchers.IO) {
            val profiles = readAllProfiles(context)
            profiles.find { it.userName == GUEST_USERNAME }
        }

        if (guest != null) {
            applyData(guest)
        } else {
            applyData(
                UserProfileData(
                    userName = GUEST_USERNAME,
                    password = GUEST_PASSWORD
                )
            )
        }
    }
}
