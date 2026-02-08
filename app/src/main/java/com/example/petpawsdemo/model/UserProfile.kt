package com.example.petpawsdemo.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.petpawsdemo.ProductDatabase
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
    val userPfpReference: Int? = null,
    val password: String,
    val darkmode: Boolean = false,
    val firstTimeEntering: Boolean = true,
    val purchasedItemIds: List<Int> = emptyList()
)

class UserProfile(
    var userName: String,
    var password: String,
    var userPfpReference: Int?,
    darkmode: Boolean = true,
) {
    companion object {
        var loggedIn: Boolean = false

        fun checkProfileExistsByUsername(context: Context, username: String): Boolean {
            val file = File(context.filesDir, "user_profiles.json")
            if (!file.exists()) return false

            val profiles = json.decodeFromString(
                ListSerializer(UserProfileData.serializer()),
                file.readText()
            )

            return profiles.any { it.userName == username }
        }

        fun checkPasswordByUsername(context: Context, username: String, password: String): Boolean {
            val file = File(context.filesDir, "user_profiles.json")
            if (!file.exists()) return false

            val profiles = json.decodeFromString(
                ListSerializer(UserProfileData.serializer()),
                file.readText()
            )

            val user = profiles.find { it.userName == username } ?: return false

            return user.password == password
        }
    }

    private val purchasedItemIds = mutableListOf<Int>()

    private val productToIDMap: Map<Product, Int> =
        ProductDatabase.getProductIDMap()
    private val idToProductMap: Map<Int, Product> =
        productToIDMap.map{ (p, id) -> id to p }.toMap()

    var darkmode by mutableStateOf(darkmode)

    fun addPurchasedItemById(id: Int) {
        purchasedItemIds.add(id)
    }

    fun addPurchasedItem(p:Product) {
        var id: Int? = productToIDMap.get(p)
        if (id != null) purchasedItemIds.add(id)
    }

    fun getPurchasedItems(): List<Product> =
        purchasedItemIds.map{ idToProductMap[it] as Product }

    fun toData(): UserProfileData = UserProfileData(
        userName = userName,
        userPfpReference = userPfpReference,
        darkmode = darkmode,
        purchasedItemIds = purchasedItemIds.toList(),
        password = password
        )

    fun applyData(data: UserProfileData) {
        userName = data.userName
        userPfpReference = data.userPfpReference
        darkmode = data.darkmode
        password = data.password
        purchasedItemIds.clear()
        purchasedItemIds.addAll(data.purchasedItemIds)
    }

    private fun getProfileFile(context: Context): File =
        File(context.filesDir, "user_profiles.json")

    private fun readAllProfiles(context: Context): MutableList<UserProfileData> {
        val file = getProfileFile(context)

        if (!file.exists()) return mutableListOf()

        val text = file.readText()
        if (text.isBlank()) return mutableListOf()

        return json.decodeFromString(
            ListSerializer(UserProfileData.serializer()),
            text
        ).toMutableList()
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
