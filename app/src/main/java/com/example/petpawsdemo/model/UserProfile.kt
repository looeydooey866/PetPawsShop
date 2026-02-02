package com.example.petpawsdemo.model

import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.R

object UserProfile { //object for 1 hardcoded profile
    var userName: String = "yangban_kimjongun"
    var userPfpReference: Int? = R.drawable.pfp
    val purchasedItems: Map<Int, Product> = ProductDatabase.getProducts()
    var darkmode: Boolean = false;
    var firstTimeEntering: Boolean = true;

    fun getPurchasedItems(): List<Product> {
        return purchasedItems.values.toList()
    }
}