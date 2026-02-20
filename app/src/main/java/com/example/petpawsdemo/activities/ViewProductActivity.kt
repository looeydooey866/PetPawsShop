package com.example.petpawsdemo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petpawsdemo.model.GUEST_USERNAME
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.model.Review
import com.example.petpawsdemo.model.UserProfileObject
import com.example.petpawsdemo.model.ViewData
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import com.example.petpawsdemo.viewmodel.UserCartObject
import kotlinx.coroutines.launch
import kotlin.math.floor
import androidx.compose.ui.Alignment
import com.example.petpawsdemo.model.ReviewsDatabase
import androidx.activity.result.ActivityResultLauncher
import com.example.petpawsdemo.model.ProductDatabase


class ViewProductActivity : ComponentActivity() {
    private val ratingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult

                val username = data.getStringExtra("username") ?: return@registerForActivityResult
                val rating = data.getDoubleExtra("rating", 0.0)
                val reviewText = data.getStringExtra("comment") ?: ""
                val productId = data.getIntExtra("productId", 0)

                val review = Review(username, "", rating, reviewText)

                ReviewsDatabase.addReview(productId, review)
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!UserCartObject.contains(ViewData.viewingId)){
            setContent {
                PetPawsDemoTheme (darkTheme = UserProfileObject.darkmode) {
                    val dbProduct = ProductDatabase.getProduct(ViewData.viewingId)!!
                    val product = UserProfileObject.getPurchasedItems().find { it.name == dbProduct.name } ?: dbProduct

                    var index by remember { mutableIntStateOf(0) }
                    var quantity by remember{ mutableIntStateOf(1)}
                    val context = LocalContext.current
                    val focusManager = LocalFocusManager.current
                    val keyboardController = LocalSoftwareKeyboardController.current
                    var editingQuantity by remember{mutableStateOf(false)}
                    Scaffold(
                        modifier = Modifier.fillMaxSize().pointerInput(Unit){
                            detectTapGestures{offset ->
                                focusManager.clearFocus(true)
                                keyboardController?.hide()
                            }
                        },
                        topBar = {
                            TopAppBar (
                                title = {
                                    Text(text = "Product Info")
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
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
                                                editingQuantity = true
                                                //quantity = Random.nextInt(100)
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
                                                    UserCartObject.addProduct(ViewData.viewingId, quantity)
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
                        if (!editingQuantity) {
                            ProductContent(innerPadding, product, quantity, index, { index = it }, ratingLauncher)
                        }
                        else{
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                ProductContent(innerPadding, product, quantity, index, { index = it }, ratingLauncher)

                                Box(
                                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = .3f))
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth(.5f).clip(RoundedCornerShape(10)).background(MaterialTheme.colorScheme.secondaryContainer).padding(10.dp)
                                ){
                                    var newQuantity by remember{mutableIntStateOf(quantity)}
                                    var stringQuantity by remember{mutableStateOf(quantity.toString())}
                                    Text(
                                        text = "Edit quantity",
                                        fontSize = 24.sp
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ){
                                        IconButton(onClick = {
                                            if (stringQuantity.isNotEmpty() && stringQuantity.all{it.isDigit()}){
                                                newQuantity = stringQuantity.toInt()
                                            }
                                            newQuantity--
                                            stringQuantity = newQuantity.toString()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.RemoveCircleOutline,
                                                contentDescription = "Hello!"
                                            )
                                        }
                                        TextField(
                                            value = "" + stringQuantity,
                                            onValueChange = {
                                                stringQuantity = it
                                            },
                                            modifier = Modifier.weight(1.0f)
                                        )
                                        IconButton(onClick = {
                                            if (stringQuantity.isNotEmpty() && stringQuantity.all{it.isDigit()}){
                                                newQuantity = stringQuantity.toInt()
                                            }
                                            newQuantity++
                                            stringQuantity = newQuantity.toString()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.AddCircleOutline,
                                                contentDescription = "Hello!"
                                            )
                                        }
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Button(onClick = {
                                            editingQuantity = false
                                        },
                                            content = {
                                                Text("Back")
                                            }
                                        )
                                        Button(onClick = {
                                            editingQuantity = false
                                            if (stringQuantity.isNotEmpty() && stringQuantity.all{it.isDigit()}){
                                                quantity = stringQuantity.toInt()
                                            }
                                            else{
                                                quantity = newQuantity
                                            }
                                        },
                                            content = {
                                                Text("Ok")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else{
            setContent {
                PetPawsDemoTheme {
                    val dbProduct = ProductDatabase.getProduct(ViewData.viewingId)!!
                    val product = UserProfileObject.getPurchasedItems().find { it.name == dbProduct.name } ?: dbProduct

                    var index by remember { mutableIntStateOf(0) }
                    val context = LocalContext.current
                    var editingQuantity by remember{mutableStateOf(false)}
                    val focusManager = LocalFocusManager.current
                    val keyboardController = LocalSoftwareKeyboardController.current
                    Scaffold(
                        modifier = Modifier.fillMaxSize().pointerInput(Unit){
                            detectTapGestures{offset ->
                                focusManager.clearFocus(true)
                                keyboardController?.hide()
                            }
                        },
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
                            if (UserCartObject.contains(ViewData.viewingId)){
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
                                                    editingQuantity = true
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
                                                    UserCartObject.removeProduct(ViewData.viewingId)
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
                        if (UserCartObject.contains(ViewData.viewingId)) {
                            if (!editingQuantity) {
                                ProductContent(innerPadding, product, UserCartObject.getCount(ViewData.viewingId), index, { index = it }, ratingLauncher)
                            }
                            else{
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ProductContent(innerPadding, product, UserCartObject.getCount(ViewData.viewingId), index, { index = it }, ratingLauncher)
                                    Box(
                                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = .3f))
                                    )
                                    Column(
                                        modifier = Modifier.fillMaxWidth(.5f).clip(RoundedCornerShape(10)).background(MaterialTheme.colorScheme.secondaryContainer).padding(10.dp)
                                    ){
                                        var newQuantity by remember{mutableIntStateOf(UserCartObject.getCount(ViewData.viewingId))}
                                        var stringQuantity by remember{mutableStateOf(UserCartObject.getCount(ViewData.viewingId).toString())}
                                        Text(
                                            text = "Edit quantity",
                                            fontSize = 24.sp
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth()
                                        ){
                                            IconButton(onClick = {
                                                if (stringQuantity.isNotEmpty() && stringQuantity.all{it.isDigit()}){
                                                    newQuantity = stringQuantity.toInt()
                                                }
                                                newQuantity--
                                                stringQuantity = newQuantity.toString()
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.RemoveCircleOutline,
                                                    contentDescription = "Hello!"
                                                )
                                            }
                                            TextField(
                                                value = "" + stringQuantity,
                                                onValueChange = {
                                                    stringQuantity = it
                                                },
                                                modifier = Modifier.weight(1.0f)
                                            )
                                            IconButton(onClick = {
                                                if (stringQuantity.isNotEmpty() && stringQuantity.all{it.isDigit()}){
                                                    newQuantity = stringQuantity.toInt()
                                                }
                                                newQuantity++
                                                stringQuantity = newQuantity.toString()
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.AddCircleOutline,
                                                    contentDescription = "Hello!"
                                                )
                                            }
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ){
                                            Button(onClick = {
                                                editingQuantity = false
                                            },
                                                content = {
                                                    Text("Back")
                                                }
                                            )
                                            Button(onClick = {
                                                editingQuantity = false
                                                if (stringQuantity.isNotEmpty() && stringQuantity.all{it.isDigit()}){
                                                    UserCartObject.changeCount(ViewData.viewingId, stringQuantity.toInt())
                                                }
                                                else{
                                                    UserCartObject.changeCount(ViewData.viewingId, newQuantity)
                                                }
                                            },
                                                content = {
                                                    Text("Ok")
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductContent(
    innerPadding: PaddingValues,
    product: Product,
    quantity: Int,
    index: Int,
    onChangeIndex: (Int) -> Unit,
    ratingLauncher: ActivityResultLauncher<Intent>
){
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
            with(product.brand) {
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
            ProductReviews(product, ratingLauncher)
        }
    }
}

@Composable
private fun ProductRatingStars(rating: Double, size: Dp){
    val golden: Color = Color(0xFFFFC107)
    val iconModifier: Modifier = Modifier.size(size)

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val fullStars = floor(rating).toInt()
        val fraction = rating - fullStars
        val hasHalfStar = fraction >= 0.25 && fraction < 0.75
        val extraFullStar = fraction >= 0.75
        val starsShown = fullStars + if (extraFullStar) 1 else 0
        val emptyStars = 5 - starsShown - if (hasHalfStar) 1 else 0

        repeat(starsShown) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = golden,
                modifier = iconModifier
            )
        }

        if (hasHalfStar) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = null,
                tint = golden,
                modifier = iconModifier
            )
        }

        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = golden,
                modifier = iconModifier
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = String.format("%.1f", rating),
            fontSize = 20.sp
        )
    }
}
@Composable
private fun ProductGallery(product: Product, index: Int, onChangeIndex: (Int) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { product.images.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = product.images[page],
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (product.images.size > 1) {
            ImageIndicator(
                count = product.images.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            )
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
private fun ReviewCard(review: Review, onDelete: (() -> Unit)? = null) {
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProductRatingStars(review.rating, 20.dp)
                    if (onDelete != null) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete review",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
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

@Composable
private fun ImageIndicator(
    count: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.25f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 8.dp else 6.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage)
                            Color.DarkGray
                        else
                            Color.LightGray
                    )
            )
        }
    }
}

@Composable
//private fun ProductReviews(product: Product){
private fun ProductReviews(
    product: Product,
    ratingLauncher: ActivityResultLauncher<Intent>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val purchasedItems = UserProfileObject.getPurchasedItems()
    var ratingButtonEnabled by remember {
        mutableStateOf(UserProfileObject.userName != GUEST_USERNAME
                && purchasedItems.any { it.name == product.name })
    }
    val snackbarHostState = remember { SnackbarHostState() }

    var reviews by remember {
        mutableStateOf(
            ProductDatabase.getID(product)?.let { id ->
                ReviewsDatabase.getReviewsByProductId(id)
            } ?: mutableListOf()
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
            //.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row {
                Text(
                    text = "Reviews",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "(${reviews.size})",
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

            reviews.forEach { review ->
                val canDelete = review.username == UserProfileObject.userName
                ReviewCard(
                    review = review,
                    onDelete = if (canDelete) {
                        {
                            scope.launch {
                                ProductDatabase.getID(product)?.let { id ->
                                    ReviewsDatabase.getReviewsByProductId(id)
                                        ?.let { reviews = it.toMutableList() }
                                }
                                Toast.makeText(context, "Review deleted", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else null
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (ratingButtonEnabled) {
                        ProductDatabase.getID(product)?.let { id ->
                            val intent = Intent(context, RatingActivity::class.java)
                                .putExtra("productId", id)
                            ratingLauncher.launch(intent)
                        } ?: run {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Cannot rate this product right now"
                                )
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "You must be logged in and have bought the product to rate it"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                //enabled = buttonEnabled
            ) {
                Text("Add Rating")
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
