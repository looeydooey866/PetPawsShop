package com.example.petpawsdemo.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.model.Review
import com.example.petpawsdemo.model.ViewData
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import com.example.petpawsdemo.viewmodel.UserCart
import kotlin.math.floor
import kotlin.random.Random

class ViewProductActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!UserCart.contains(ViewData.viewingId)){
            setContent {
                PetPawsDemoTheme {
                    val product = ProductDatabase.getProduct(ViewData.viewingId)!!
                    var index by remember { mutableIntStateOf(0) }
                    var quantity by remember{ mutableIntStateOf(1)}
                    val context = LocalContext.current
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
                                        finish()
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
                                                quantity = Random.nextInt(100)
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ){
                                        Text("Change Quantity", fontSize = 20.sp, color = Color.White/*, fontWeight =  FontWeight.SemiBold*/)
                                    }
                                    if (product.stock > 0){
                                        Row(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.primary)
                                                .clickable{
                                                    UserCart.addProduct(ViewData.viewingId, quantity)
                                                    finish()
                                                    Toast.makeText(context, "Added to cart.", Toast.LENGTH_SHORT).show()
                                                },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ){
                                            Text("Add $quantity To Cart", fontSize = 20.sp, color = Color.White/*, fontWeight =  FontWeight.SemiBold*/)
                                        }
                                    }
                                    else{
                                        Row(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth()
                                                .background(Color.Red),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ){
                                            Text("Out of stock", fontSize = 20.sp, color = Color.White/*, fontWeight =  FontWeight.SemiBold*/)
                                        }
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        ProductContent(innerPadding, product, quantity, index){
                            index = it
                        }
                    }
                }
            }
        }
        else{
            setContent {
                PetPawsDemoTheme {
                    val product = ProductDatabase.getProduct(ViewData.viewingId)!!
                    var index by remember { mutableIntStateOf(0) }
                    val context = LocalContext.current
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
                                        finish()
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
                            if (UserCart.contains(ViewData.viewingId)){
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
                                                    UserCart.changeCount(ViewData.viewingId, Random.nextInt(100))
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
                                                .background(Color.Red)
                                                .clickable{
                                                    finish()
                                                    UserCart.removeProduct(ViewData.viewingId)
                                                    Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show()
                                                },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ){
                                            Text("Remove from cart", fontSize = 20.sp, color = Color.White/*, fontWeight =  FontWeight.SemiBold*/)
                                        }
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        if (UserCart.contains(ViewData.viewingId)) {
                            ProductContent(innerPadding, product, UserCart.getCount(ViewData.viewingId), index){
                                index = it
                            }
                        }
                    }
                }
            }
        }
    }

   @Composable
   private fun ProductContent(innerPadding: PaddingValues, product: Product, quantity: Int, index: Int, onChangeIndex: (Int) -> Unit){
       Column(
           modifier = Modifier
               .padding(innerPadding).fillMaxSize()
               .verticalScroll(rememberScrollState()),
       ) {
           ProductGallery(product, index, onChangeIndex)
           Spacer(
               modifier = Modifier.height(5.dp)
           )
           Column(
               modifier = Modifier.fillMaxSize().padding(10.dp),
               verticalArrangement = Arrangement.spacedBy(5.dp)
           ) {
               Text(
                   text = product.name, fontSize = 26.sp
               )
               ProductPrice(
                   product,
                   quantity
               )
               with(product.producer) {
                   Text(
                       text = "Brand: $this",
                       fontSize = 20.sp,
                       //color = Color.Gray
                   )
               }
               Row(
                   modifier = Modifier.fillMaxWidth(1.0f),
                   horizontalArrangement = Arrangement.SpaceBetween,
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   ProductRatingStars(product.rating, 30.dp)
                   if (product.stock > 0) {
                       Text(
                           text = "${product.stock} in stock",
                           fontSize = 16.sp,
                       )
                   }
                   else{
                       Text(
                           text = "Out of stock",
                           fontSize = 16.sp,
                       )
                   }
               }
               /*
           Spacer(
               modifier = Modifier.height(10.dp)
           )
           Box(
               modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xffd3d3d3))
           )

            */
               Spacer(
                   modifier = Modifier.height(20.dp)
               )
               ProductDescription(product)
               Spacer(
                   modifier = Modifier.height(20.dp)
               )
               ProductReviews(product)
           }
       }
   }

    @Composable
    private fun ProductRatingStars(rating: Double, size: Dp){
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
                fontSize = 20.sp
            )
        }
    }
    @Composable
    private fun ProductGallery(product: Product, index: Int, onChangeIndex: (Int) -> Unit) {
        Box(modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()) {
            AsyncImage(
                model = product.images[index],
                contentDescription = "Image with url ${product.images[index]}",
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth(1f)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                if (index != 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowLeft,
                                contentDescription = null,
                                modifier = Modifier
                                    .requiredSize(40.dp)
                                    .clickable {
                                        onChangeIndex(index - 1)
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
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowRight,
                                contentDescription = null,
                                modifier = Modifier
                                    .requiredSize(40.dp)
                                    .clickable {
                                        onChangeIndex(index + 1)
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
    }

    @Composable
    private fun ProductPrice(product: Product, quantity: Int) {
        Row {
            Text(
                text = "$${product.price / 100}.${
                    String.format(
                        "%02d",
                        product.price % 100
                    )
                }",
                fontSize = 28.sp
            )
            if (quantity > 1) {
                Text(
                    text = " ($quantity items = $${product.price * quantity / 100}.${
                        String.format(
                            "%02d",
                            product.price * quantity % 100
                        )
                    })",
                    fontSize = 28.sp,
                    color = Color.Gray
                )
            }
        }
    }

    @Composable
    private fun ProductDescription(product: Product) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.padding(horizontal = 12.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "About this product",
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = product.description,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 25.sp
            )
        }
    }

    @Composable
    private fun ProductReviews(product: Product){
        Column(
            modifier = Modifier
                .fillMaxWidth()
            //.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row {
                Text(
                    text = "Reviews ",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "(${product.reviews.size})",
                    fontSize = 23.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
            )

            Spacer(modifier = Modifier.height(12.dp))
            product.reviews.forEach{
                ReviewCard(it)
            }
        }
    }

    @Composable
    private fun ReviewCard(review: Review) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = review.profilePicture,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = review.username,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    ProductRatingStars(review.rating, 20.dp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = review.review,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
