package com.example.petpawsdemo.activities

import androidx.room.util.copy
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.example.petpawsdemo.model.ShapeState
import com.example.petpawsdemo.model.UserProfileObject
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class ContactUsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetPawsDemoTheme (darkTheme = UserProfileObject.darkmode){
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Contact Us") },
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
                        AboutPetPawsScreen(UserProfileObject.darkmode) { finish() }
                    }
                }
            }
        }
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
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //inspirational words
            Text(
                text = "Feel free to voice your concerns or queries. We are here for you 24/7.",
                style = xkcdTextStyle.copy(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        if (darkmode) Color.White
                        else Color.Black
                ),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 32.dp),
                color =
                    if (darkmode) Color.White.copy(alpha = 0.5f)
                    else Color.Black.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column (
                modifier = Modifier
                    .background(Color.White.copy(alpha = if (darkmode) 0f else 0.7f)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "\n Our Address: 61 Lengkok Bahru \n" +
                            "Our Hotline: +65 8915 9218 \n" +
                            "Our Email: heckerhaccersky@gmail.com \n",
                    style = xkcdTextStyle.copy(
                        fontSize = 19.sp,
                        color = if (darkmode) Color.White else Color.Black
                    ),
                    textAlign = TextAlign.Center,
                )

                Image(
                    painter = painterResource(
                        if (darkmode) R.drawable.petpawslogodarkthemenobg
                        else R.drawable.petpawslogolightthemenobg
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
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