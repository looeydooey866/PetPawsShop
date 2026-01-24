package com.example.petpawsdemo.ProductClasses

import android.widget.Toast
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
import com.example.petpawsdemo.ui.theme.Grey_Separator

@Composable
fun ProductContainer(products: List<Product>, innerPadding: PaddingValues){
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
            ){
                Toast.makeText(context, "Clicked product $product", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

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
        Row(
            modifier = Modifier.fillMaxWidth(1.0f).height(5.dp).clip(RoundedCornerShape(2.dp)).background(Grey_Separator)
        ){

        }
    }
}

@Composable
fun ProductContainer(type: String, products: Map<String, List<Product>>, innerPadding: PaddingValues){
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
                        ProductCard(chunk[0], Modifier.weight(1.0f)){
                            Toast.makeText(context, "Clicked product ${chunk[0]}", Toast.LENGTH_SHORT).show()
                        }
                        ProductCard(chunk[1], Modifier.weight(1.0f)){
                            Toast.makeText(context, "Clicked product ${chunk[1]}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        ProductCard(chunk[0], Modifier) {
                            Toast.makeText(context, "Clicked product ${chunk[0]}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
