package com.example.petpawsdemo.ProductClasses

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.petpawsdemo.ProductDatabase

@Composable
fun ProductScreen(
    id: Int,
    onBack: () -> Unit,
){
    BackHandler {
        onBack()
    }
    val product = ProductDatabase.getProduct(id)!!
    var index by remember { mutableIntStateOf(0) }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
            Text(text = product.name, fontSize = 36.sp)
            Row(
                modifier = Modifier.fillMaxWidth(1.0f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "$${product.price / 100}.${String.format("%02d", product.price % 100)}",
                    fontSize = 36.sp
                )
                Text(
                    text = "${product.stock} in stock",
                    fontSize = 30.sp,
                )
            }
        }
    }
}
