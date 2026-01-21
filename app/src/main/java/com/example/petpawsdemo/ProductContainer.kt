package com.example.petpawsdemo

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ProductContainer(products: List<Product>, innerPadding: PaddingValues){
    val context = LocalContext.current
    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(innerPadding)
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ){
        val mod = Modifier
        items(products){ product ->
            ProductCard(
                product = product,
                modifier = mod
            ){
                Toast.makeText(context, "Clicked product $product", Toast.LENGTH_SHORT).show()
            }
        }
    }
}