package com.example.petpawsdemo.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.view.ui.theme.Grey_Separator


@Composable
fun CategorySeparator(type: String, subtype: String){
    Column(
        modifier = Modifier.fillMaxWidth(1.0f).padding(top = 25.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier,
            fontSize = 25.sp,
            text = "${type.replaceFirstChar{it.uppercase()}} ${subtype.replaceFirstChar{it.uppercase()}}"
        )
        val separate = false
        if (separate) {
            Row(
                modifier = Modifier.fillMaxWidth(1.0f).height(5.dp).clip(RoundedCornerShape(2.dp))
                    .background(Grey_Separator)
            ) {

            }
        }
    }
}

@Composable
fun ProductContainer(products: List<Product>, innerPadding: PaddingValues, onClick: (Int) -> Unit){
    val context = LocalContext.current
    LazyVerticalGrid (
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(innerPadding)
            .fillMaxWidth(1.0f),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ){
        val mod = Modifier
        items(products){ product ->
            ProductCard(
                product = product,
                modifier = mod
            ) {
                onClick(ProductDatabase.getID(product)!!)
            }
        }
    }
}

@Composable
fun ProductContainer(type: String, products: Map<String, List<Product>>, innerPadding: PaddingValues, onClick: (Int) -> Unit){
    val context = LocalContext.current
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f).verticalScroll(scroll).padding(innerPadding),
        horizontalAlignment = Alignment.Start,
    ){
        products.forEach{(subtype, products) ->
            CategorySeparator(type, subtype)
            products.chunked(2).forEach{chunk ->
                if (chunk.size == 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(1.0f)
                    ){
                        ProductCard(chunk[0], Modifier.weight(1.0f)) {
                            onClick(ProductDatabase.getID(chunk[0])!!)
                        }
                        ProductCard(chunk[1], Modifier.weight(1.0f)) {
                            onClick(ProductDatabase.getID(chunk[1])!!)
                        }
                    }
                }
                else {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        ProductCard(chunk[0], Modifier) {
                            onClick(ProductDatabase.getID(chunk[0])!!)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductContainer(products: Map<String, Map<String,List<Product>>>, innerPadding: PaddingValues, onClick: (Int) -> Unit){
    val context = LocalContext.current
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f).verticalScroll(scroll).padding(innerPadding),
        horizontalAlignment = Alignment.Start,
    ){
        products.forEach{(type, entry) ->
            CategorySeparator(type, "items")
            entry.forEach{(subtype, products) ->
                CategorySeparator(type, subtype)
                products.chunked(2).forEach{chunk ->
                    if (chunk.size == 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(1.0f)
                        ){
                            ProductCard(chunk[0], Modifier.weight(1.0f)) {
                                onClick(ProductDatabase.getID(chunk[0])!!)
                            }
                            ProductCard(chunk[1], Modifier.weight(1.0f)) {
                                onClick(ProductDatabase.getID(chunk[1])!!)
                            }
                        }
                    }
                    else {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            ProductCard(chunk[0], Modifier) {
                                onClick(ProductDatabase.getID(chunk[0])!!)
                            }
                        }
                    }
                }
            }
        }
    }
}
