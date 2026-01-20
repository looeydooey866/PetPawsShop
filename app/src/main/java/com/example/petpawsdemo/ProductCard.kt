package com.example.petpawsdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petpawsdemo.ui.theme.LightRed_Stock

@Composable
fun ProductCard(product: Product, modifier: Modifier, onClick: () -> Unit){
    Column (
        modifier = modifier.clickable{onClick()}
        //.aspectRatio(6f / 7)
        .background(Color.Transparent)
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
    ){
        ProductImage(product)
        ProductName(product)
        ProductInfo(product)
        ProductRating(product)
    }
}

@Composable
fun ProductImage(product: Product){
    Row(
        modifier = Modifier.fillMaxWidth(1f)
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = product.images.first(),
            contentDescription = "Image with url ${product.images.first()}",
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth(1f)
        )
    }
}

@Composable
fun ProductName(product: Product){
    Text(
        text = product.name,
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth(1.0f),
        fontWeight = FontWeight.SemiBold,
        minLines = 2
    )
}

@Composable
fun ProductInfo(product: Product){
    Row(
        modifier = Modifier.fillMaxWidth(1.0f),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "$${product.price / 100}.${String.format("%02d", product.price % 100)}",
            fontSize = 20.sp
        )
        Text(
            text = "${product.stock} in stock",
            fontSize = 13.sp,
        )
    }
}

@Composable
fun ProductRating(product: Product){

}