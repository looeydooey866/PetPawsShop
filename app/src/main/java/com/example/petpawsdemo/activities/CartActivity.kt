package com.example.petpawsdemo.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.model.CartObject
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.model.UserProfile
import com.example.petpawsdemo.model.ViewData
import com.example.petpawsdemo.view.AppBar
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import com.example.petpawsdemo.viewmodel.UserCart
import kotlinx.coroutines.launch
import kotlin.math.floor

class CartActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetPawsDemoTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Review Purchase") },
                            navigationIcon = {
                                IconButton( onClick = { finish() } ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "My Cart",
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                )
                        )
                        Column(
                            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            UserCart.products.forEach {
                                CartItem(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItem(cartObject: CartObject){
    val product = ProductDatabase.getProduct(cartObject.id)!!
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(1.0f).height(100.dp).clickable{
            ViewData.viewingId = cartObject.id
            val intent = Intent(context, ViewProductActivity::class.java)
            context.startActivity(intent)
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
    ){
        IconButton(
            onClick = {
                UserCart.removeProduct(cartObject.id)
            },
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Remove Product"
            )
        }
        CartImage(product)
        Column(
            modifier = Modifier.weight(1f)
        ){
            Text(text = product.name, fontSize = 17.sp)
            CartPrice(product, cartObject.quantity)
            RatingStars(product.rating, 15.dp)
            Text(text = "Edit", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        }
        Text(
            text = "${cartObject.quantity}x",
            fontSize = 18.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun CartImage(product: Product){
    Row(
        modifier = Modifier.fillMaxHeight(1f)
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
private fun CartPrice(product: Product, quantity: Int) {
    Row {
        Text(
            text = "$${product.price * quantity / 100}.${
                String.format(
                    "%02d",
                    product.price * quantity % 100
                )
            }",
            fontSize = 16.sp,
        )
        if (quantity > 1) {
            Text(
                text = " ($${product.price / 100}.${
                    String.format(
                        "%02d",
                        product.price % 100
                    )
                } each)",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun RatingStars(rating: Double, size: Dp){
    val golden: Color = Color(0xFFDAA520)
    val iconModifier: Modifier = Modifier
        .size(size)

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val rating = rating
        var fullStars: Int = floor(rating).toInt()
        if (rating - fullStars.toDouble() > 0.75) fullStars++;
        val hasHalfStar: Boolean = ((rating - fullStars.toDouble()) > 0.25) && ((rating - fullStars.toDouble()) < 0.75)
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
            text = "" + rating,
            fontSize = 15.sp
        )
    }
}
