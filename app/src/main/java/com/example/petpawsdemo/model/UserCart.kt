package com.example.petpawsdemo.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petpawsdemo.model.CartObject

class CartViewModel(application: Application) : AndroidViewModel(application) {

    val products = mutableStateListOf<CartObject>()

    var cartSize by mutableStateOf(0)
        private set

    private fun updateCartSize() {
        cartSize = products.sumOf { it.quantity }
    }

    fun addProduct(id: Int, quantity: Int) {
        val existing = products.find { it.id == id }
        if (existing != null) {
            existing.quantity += quantity
        } else {
            products.add(CartObject(id, quantity))
        }
        updateCartSize()
    }

    fun removeProduct(id: Int) {
        products.removeAll { it.id == id }
        updateCartSize()
    }

    fun changeCount(id: Int, quantity: Int) {
        products.find { it.id == id }?.let { it.quantity = quantity }
        updateCartSize()
    }

    fun contains(id: Int): Boolean =
        products.any { it.id == id }

    fun getCount(id: Int): Int =
        products.find { it.id == id }?.quantity ?: 0

    fun clear() {
        products.clear()
        updateCartSize()
    }
}

class CartViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}