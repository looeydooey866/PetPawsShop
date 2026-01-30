package com.example.petpawsdemo.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.ProductDatabase
import kotlin.math.floor
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    id: Int,
    quantity: Int,
    onChangeQuantity: (Int) -> Unit,
    onBack: () -> Unit,
    onBuy: () -> Unit,
){
    BackHandler {
        onBack()
    }
    val product = ProductDatabase.getProduct(id)!!
    var index by remember { mutableIntStateOf(0) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar (
                title = {
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Hello!"
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(100.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxSize()
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.5f)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable{
                            onChangeQuantity(Random.nextInt(100))
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text("Change Quantity", fontSize = 20.sp, color = Color.White/*, fontWeight =  FontWeight.SemiBold*/)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable{
                            onBuy()
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text("Add $quantity To Cart", fontSize = 20.sp, color = Color.White/*, fontWeight =  FontWeight.SemiBold*/)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding).fillMaxSize(),
        ){
            Box(modifier = Modifier.aspectRatio(1f).fillMaxWidth()){
                AsyncImage(
                    model = product.images[index],
                    contentDescription = "Image with url ${product.images[index]}",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth(1f)
                )
                Box(
                    modifier = Modifier.fillMaxSize().padding(20.dp)
                ) {
                    if (index != 0) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Box(
                                modifier = Modifier.size(30.dp).clip(RoundedCornerShape(50))
                                    .background(Color.White)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowLeft,
                                    contentDescription = null,
                                    modifier = Modifier.requiredSize(40.dp).clickable {
                                        index--
                                    }
                                )
                            }
                        }
                    }
                    if (index != product.images.size - 1) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Box(
                                modifier = Modifier.size(30.dp).clip(RoundedCornerShape(50))
                                    .background(Color.White)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowRight,
                                    contentDescription = null,
                                    modifier = Modifier.requiredSize(40.dp).clickable {
                                        index++
                                    }
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Text("...")
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(5.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = product.name, fontSize = 26.sp
                )
                Text(
                    text = "$${product.price / 100}.${
                        String.format(
                            "%02d",
                            product.price % 100
                        )
                    }",
                    fontSize = 28.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(1.0f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductRatingScreen(product)
                    Text(
                        text = "${product.stock} in stock",
                        fontSize = 16.sp,

                        )
                }
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
                Box(
                    modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xffd3d3d3))
                )
            }
        }
    }
}

@Composable
fun ProductRatingScreen(product: Product){
    val golden: Color = Color(0xFFDAA520)
    val iconModifier: Modifier = Modifier
        .size(30.dp);

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val rating = product.rating;
        var fullStars: Int = floor(rating).toInt()
        if (rating - fullStars.toDouble() > 0.8) fullStars++;
        val hasHalfStar: Boolean = ((rating - fullStars.toDouble()) > 0.3) && ((rating - fullStars.toDouble()) < 0.7)
        val emptyStars: Int = 5 - fullStars - (if (hasHalfStar) 1 else 0);

        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Filled star",
                tint = golden,
                modifier = iconModifier
            )
        }

        if (hasHalfStar) {
            Box (contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.StarHalf,
                    contentDescription = "Half Filled star",
                    tint = golden,
                    modifier = iconModifier
                )
            }
        }

        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = "Empty star",
                tint = golden,
                modifier = iconModifier
            )
        }
        Text(
            text = "" + product.rating,
            fontSize = 20.sp
        )
    }
}
