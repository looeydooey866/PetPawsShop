package com.example.petpawsdemo.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class CartObject(val id: Int, quantity: Int){
    var quantity by mutableIntStateOf(quantity)
}