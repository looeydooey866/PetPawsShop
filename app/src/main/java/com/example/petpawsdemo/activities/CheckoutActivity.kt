package com.example.petpawsdemo.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.model.CartObject
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.model.UserProfileObject
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import com.example.petpawsdemo.viewmodel.UserCartObject
import kotlin.math.floor

class CheckoutActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var paymentMethod by remember{mutableStateOf("Credit/Debit Card")}
            var deliveryAddress by remember{mutableStateOf("")}
            val context = LocalContext.current
            PetPawsDemoTheme (darkTheme = UserProfileObject.darkmode) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Checkout")
                            },
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
                ){innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(10.dp).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        Text(
                            text = "Checkout",
                            fontSize = 30.sp
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth().height(5.dp).background(Color.Gray)
                        )
                        UserCartObject.products.forEach{
                            CartItemReadonly(it)
                        }
                        val cost = UserCartObject.getSubtotal()
                        Text(
                            text = "Items: ${UserCartObject.products.size}",
                            fontSize = 23.sp,
                        )
                        Text(
                            text = "Total: $${cost / 100}.${
                                String.format(
                                    "%02d",
                                    cost % 100
                                )
                            }",
                            fontSize = 25.sp,
                        )
                        Text(
                            text = "Payment method",
                            fontSize = 26.sp,
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            PaymentOption(
                                paymentMethod = paymentMethod,
                                myMethod = "Credit/Debit Card",
                                onChangeMethod = {paymentMethod = "Credit/Debit Card"},
                                icon = Icons.Default.CreditCard,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                            PaymentOption(
                                paymentMethod = paymentMethod,
                                myMethod = "Pet Paws E-Wallet",
                                onChangeMethod = {paymentMethod = "Pet Paws E-Wallet"},
                                icon = Icons.Default.Pets,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = "Delivery Address",
                            fontSize = 26.sp,
                        )
                        TextField(
                            value = deliveryAddress,
                            onValueChange = {
                                deliveryAddress = it
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            modifier = Modifier.background(MaterialTheme.colorScheme.background),
                            onClick = {
                                UserCartObject.getProducts().forEach{
                                    val cartObject: CartObject = it
                                    UserProfileObject.addPurchasedItem(cartObject.id)
                                }
                                val intent = Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                                }
                                startActivity(intent)
                            },
                        ){
                            Text(
                                text = "Purchase!"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemReadonly(cartObject: CartObject){
    val product = ProductDatabase.getProduct(cartObject.id)!!
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(1.0f).height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
    ){
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

@Composable
private fun PaymentOption(paymentMethod: String, myMethod: String, onChangeMethod: (String) -> Unit, color: Color, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth().selectable(
            selected = (paymentMethod == myMethod),
            onClick = {
                onChangeMethod(myMethod)
            },
            role = Role.RadioButton
        ).clip(RoundedCornerShape(25)).background(MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (paymentMethod == myMethod),
            onClick = null
        )
        Text(
            text = myMethod,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.weight(1.0f))
        IconButton(onClick = { }) {
            Icon(
                icon,
                contentDescription = null
            )
        }
    }
}
