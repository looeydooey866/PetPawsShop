package com.example.petpawsdemo.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.model.CartObject

object UserCart{
    val products = mutableStateListOf<CartObject>()

    fun getProducts() = products.toList()

    fun addProduct(id: Int, quantity: Int) = products.add(CartObject(id, quantity))

    fun contains(id: Int) = products.any{
        it.id == id
    }

    private fun findProduct(id: Int) = products.find{it.id == id}!!

    fun removeProduct(id: Int) = products.removeIf{it.id == id}

    fun changeCount(id: Int, quantity: Int) = findProduct(id).apply{
        this.quantity = quantity
    }

    fun getCount(id: Int) = findProduct(id).quantity

    fun clear() = products.clear()

    fun getSubtotal() = products.sumOf { ProductDatabase.getProduct(it.id)!!.price * it.quantity }
}
