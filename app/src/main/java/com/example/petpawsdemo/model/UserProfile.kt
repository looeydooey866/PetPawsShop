package com.example.petpawsdemo.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.R

object UserProfile { //object for 1 hardcoded profile
    var userName: String = "yangban_kimjongun"
    var userPfpReference: Int? = R.drawable.pfp
    val purchasedItems: Map<Int, Product> = ProductDatabase.getProducts()
    var darkmode by mutableStateOf(false)
    var firstTimeEntering: Boolean = true;

    fun getPurchasedItems(): List<Product> {
        return purchasedItems.values.toList()
    }
}