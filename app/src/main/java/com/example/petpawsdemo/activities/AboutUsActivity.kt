package com.example.petpawsdemo.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpawsdemo.R
import com.example.petpawsdemo.model.UserProfile
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

val xkcdTextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.xkcdscript)),
    fontSize = 18.sp
)

class AboutUsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetPawsDemoTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("About Us") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
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
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        if (UserProfile.firstTimeEntering) {
                            UserProfile.firstTimeEntering = false
                            AboutPetPawsScreenReactive(UserProfile.darkmode) {
                                finish()
                            }
                        }
                        else {
                            AboutPetPawsScreen(UserProfile.darkmode) {
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}

//animated background stuff below yay!
@Composable
fun AboutPetPawsScreenReactive(
    darkmode: Boolean,
    onFinish: () -> Unit
) {
    val lines = listOf(
        "You and your pets deserve quality products.",
        "Pet Paws is a modern pet lifestyle brand dedicated to " +
                "providing high-quality products for our animal companions.",
        "From everyday essentials to beautiful accessories, we focus on comfort, safety, and style;" +
                " all for the satisfaction of you, our customers, and your pets.",
        "Our brand is committed to responsible pet care.",
        "We uphold this value by providing pet owners with " +
                "the best pet products to care for them responsibly.",
        "May you find the perfect pet products for your pets. Happy browsing!"
    )

    val shapes = remember {
        List(15) {
            ShapeState(
                x = Random.nextFloat() * 1080f,
                y = Random.nextFloat() * 1920f,
                size = Random.nextFloat() * 25f + 15f,
                rotation = Random.nextFloat() * 360f,
                alpha = Random.nextFloat() * 0.5f + 0.3f,
                color = Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(), 1f
                )
            )
        }
    }

    //leaving link stuff
    val listState = rememberLazyListState()
    val lastIndex = lines.lastIndex
    val linkAlpha = remember { Animatable(0f) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.map { it.index } }
            .collect { visibleIndices ->
                if (lastIndex in visibleIndices) {
                    delay(1000) //aft last line appear
                    linkAlpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(1500)
                    )
                }
            }
    }

    //animate: shape
    shapes.forEach { shape ->
        LaunchedEffect(shape) {
            while (true) {
                shape.rotationAnim.animateTo(
                    targetValue = shape.rotationAnim.value + 360f,
                    animationSpec = tween(4000, easing = LinearEasing)
                )
                shape.alphaAnim.animateTo(
                    targetValue = 0.3f + Random.nextFloat() * 0.5f,
                    animationSpec = tween(4000, easing = LinearEasing)
                )
                shape.sizeAnim.animateTo(
                    targetValue = 15f + Random.nextFloat() * 25f,
                    animationSpec = tween(4000, easing = LinearEasing)
                )
            }
        }
    }

    //animate: bg
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            shapes.forEach { shape ->
                val firstVisible = listState.layoutInfo.visibleItemsInfo.firstOrNull()
                val screenCenter = size.height / 2
                var velocity = 1f
                if (firstVisible != null) {
                    val itemCenter = firstVisible.offset + firstVisible.size / 2
                    val distanceFromCenter = kotlin.math.abs(itemCenter - screenCenter)
                    velocity += distanceFromCenter / 100f
                }
                shape.x += 0.5f * velocity
                shape.y += 0.3f * velocity
                if (shape.x > size.width) shape.x = 0f
                if (shape.y > size.height) shape.y = 0f

                rotate(
                    shape.rotationAnim.value,
                    pivot = Offset(shape.x, shape.y)
                ) {
                    drawCircle(
                        color = shape.color.copy(alpha = shape.alphaAnim.value),
                        radius = shape.sizeAnim.value,
                        center = Offset(shape.x, shape.y)
                    )
                }
            }
        }

        //scrolling text
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
        ) {
            itemsIndexed(lines) { index, line ->
                val itemInfo = listState.layoutInfo.visibleItemsInfo
                    .firstOrNull { it.index == index }
                val screenHeight = listState.layoutInfo.viewportEndOffset
                val centerOffset = screenHeight / 2
                val distanceFromCenter =
                    if (itemInfo != null) kotlin.math.abs(itemInfo.offset + itemInfo.size / 2 - centerOffset)
                    else 0

                val alpha = (1f - (distanceFromCenter.toFloat() / centerOffset.toFloat()))
                    .coerceIn(0f, 1f)

                Box(
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.5f))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = line,
                            style = xkcdTextStyle.copy(fontSize = 28.sp),
                            textAlign = TextAlign.Center,
                            color = Color.Black.copy(alpha = alpha)
                        )
                    }
                }
            }

            //return link
            items(1) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Back to browsing",
                        style = xkcdTextStyle.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (darkmode) Color.White else Color.Black
                        ),
                        modifier = Modifier
                            .alpha(linkAlpha.value)
                            .clickable { onFinish() },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        //logo absolutely minimatic
        Image(
            painter = painterResource(
                id = if (darkmode) R.drawable.petpawslogodarkthemenobg
                else R.drawable.petpawslogolightthemenobg
            ),
            contentDescription = null,
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun AboutPetPawsScreen(darkmode: Boolean, onFinish: () -> Unit = {}) {
    //shapes def
    val shapes = remember {
        List(15) {
            ShapeState(
                x = Random.nextFloat() * 1080f,
                y = Random.nextFloat() * 1920f,
                size = Random.nextFloat() * 25f + 15f,
                rotation = Random.nextFloat() * 360f,
                alpha = Random.nextFloat() * 0.3f,
                color = Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(), 1f
                )
            )
        }
    }

    //link fadein
    val linkAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(6000)
        linkAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(1500)
        )
    }

    //animate: shapes
    shapes.forEach { shape ->
        LaunchedEffect(shape) {
            while (true) {
                shape.rotationAnim.animateTo(
                    targetValue = shape.rotationAnim.value + 360f,
                    animationSpec = tween(4000, easing = LinearEasing)
                )
                shape.alphaAnim.animateTo(
                    targetValue = 0.3f + Random.nextFloat() * 0.5f,
                    animationSpec = tween(4000, easing = LinearEasing)
                )
                shape.sizeAnim.animateTo(
                    targetValue = 15f + Random.nextFloat() * 25f,
                    animationSpec = tween(4000, easing = LinearEasing)
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        //animate: bg
        Canvas(modifier = Modifier.fillMaxSize()) {
            shapes.forEach { shape ->
                shape.x += 0.5f
                shape.y += 0.3f
                if (shape.x > size.width) shape.x = 0f
                if (shape.y > size.height) shape.y = 0f

                rotate(shape.rotationAnim.value, pivot = Offset(shape.x, shape.y)) {
                    drawCircle(
                        color = shape.color.copy(alpha = shape.alphaAnim.value),
                        radius = shape.sizeAnim.value,
                        center = Offset(shape.x, shape.y)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id =
                        if (darkmode) R.drawable.petpawslogodarkthemenobg
                        else R.drawable.petpawslogolightthemenobg
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
            )

            //inspirational words
            Text(
                text = "You and your pets deserve quality products.",
                style = xkcdTextStyle.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        if (darkmode) Color.White
                        else Color.Black
                ),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 32.dp),
                color =
                    if (darkmode) Color.White.copy(alpha = 0.5f)
                    else Color.Black.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column (
                modifier = Modifier.background(Color.White.copy(alpha = 0.7f))
            ) {
                Text(
                    text = "Pet Paws is a modern pet lifestyle brand dedicated to providing " +
                            "high-quality products for our animal companions. " +
                            "From everyday essentials to beautiful accessories, we focus " +
                            "on comfort, safety, and style; all for the satisfaction " +
                            "of you, our customers, and your pets.",
                    style = xkcdTextStyle.copy(
                        fontSize = 19.sp,
                        color = if (darkmode) Color.White else Color.Black
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Our brand is committed to responsible pet care. We uphold this " +
                            "value by providing pet owners with the best " +
                            "pet products to care for them responsibly.",
                    style = xkcdTextStyle.copy(
                        fontSize = 19.sp,
                        color = if (darkmode) Color.White else Color.Black
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        }

        // bye bye link
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Back to Browsing",
                style = xkcdTextStyle.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (darkmode) Color.White else Color.Black
                ),
                modifier = Modifier
                    .alpha(linkAlpha.value)
                    .clickable { onFinish() },
                textAlign = TextAlign.Center
            )
        }
    }
}

class ShapeState(
    var x: Float,
    var y: Float,
    size: Float,
    rotation: Float,
    alpha: Float,
    val color: Color
) {
    val sizeAnim = Animatable(size)
    val rotationAnim = Animatable(rotation)
    val alphaAnim = Animatable(alpha)
}
