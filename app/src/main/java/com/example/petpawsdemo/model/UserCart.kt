package com.example.petpawsdemo.model

object UserCart{ //object for 1 hardcoded profile
    private val products: MutableSet<CartObject> = mutableSetOf()

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
}