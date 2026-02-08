package com.example.petpawsdemo.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.model.UserProfileObject
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import kotlin.math.floor


@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f)

    val infiniteTransition = rememberInfiniteTransition()
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    PetPawsDemoTheme (darkTheme = UserProfileObject.darkmode) {
        val surface = MaterialTheme.colorScheme.surface
        val onPrimary = MaterialTheme.colorScheme.surface
        val onSurface = MaterialTheme.colorScheme.onSurface
        val cardBackground = Color(0xFFF5F5F5)

        Column(
            modifier = modifier
                .width(180.dp)
                .scale(scale)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            pressed = true
                            tryAwaitRelease()
                            pressed = false
                        },
                        onTap = {
                            onClick()
                        }
                    )
                }
                .shadow(8.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                ///*
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(onPrimary, onPrimary)
                        //colors = listOf(Color.White, Color.White)
                    )
                )
                //*/
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    //.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f))
                    .offset(y = floatOffset.dp),
                contentAlignment = Alignment.Center
            ) {
                ProductImage(product)
            }

            //height changes, max 3 lines 50 chars
            ProductName(product)

            Spacer(modifier = Modifier.height(4.dp))

            //heights are fixed
            ProductInfo(product, height = 28.dp)
            Box(modifier = Modifier.height(28.dp)) {
                ProductRating(product)
            }
        }
    }
}

@Composable
private fun ProductName(product: Product) {
    Text(
        text = product.name,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 3,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProductInfo(product: Product, height: Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$${product.price / 100}.${String.format("%02d", product.price % 100)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${product.stock} in stock",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ProductRating(product: Product) {
    val golden = Color(0xFFFFC107)
    val rating = product.rating

    val fullStars = floor(rating).toInt()
    val fraction = rating - fullStars
    val hasHalfStar = fraction >= 0.25 && fraction < 0.75
    val extraFullStar = fraction >= 0.75
    val starsShown = fullStars + if (extraFullStar) 1 else 0
    val emptyStars = 5 - starsShown - if (hasHalfStar) 1 else 0

    val infiniteTransition = rememberInfiniteTransition()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        repeat(starsShown) { index ->
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        800 + index * 100,
                        easing = LinearOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = golden,
                modifier = Modifier
                    .size(20.dp)
                    .scale(scale)
            )
        }

        if (hasHalfStar) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = null,
                tint = golden,
                modifier = Modifier
                    .size(20.dp)
            )
        }

        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = golden,
                modifier = Modifier
                    .size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = String.format("%.1f", rating),
            fontSize = 14.sp
        )
    }
}


@Composable
private fun ProductImage(product: Product) {
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