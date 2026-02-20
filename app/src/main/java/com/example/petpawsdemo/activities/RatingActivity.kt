package com.example.petpawsdemo.activities

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpawsdemo.model.GUEST_USERNAME
import com.example.petpawsdemo.model.Review
import com.example.petpawsdemo.model.UserProfileObject
import com.example.petpawsdemo.model.UserProfileObject.darkmode
import com.example.petpawsdemo.view.RatingBar
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme

class RatingActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val productId = intent.getIntExtra("productId", -1)

        setContent {
            PetPawsDemoTheme(darkTheme = darkmode) {
                RatingActivityContent(
                    onSend = { finalReview ->
                        val resultIntent = Intent().apply {
                            putExtra("username", finalReview.username)
                            putExtra("rating", finalReview.rating)
                            putExtra("comment", finalReview.review)
                            putExtra("productId", productId)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    },
                    onCancel = {
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingActivityContent(
    onSend: (Review) -> Unit,
    onCancel: () -> Unit
) {
    var rating by remember { mutableStateOf(0.0) }
    var reviewText by remember { mutableStateOf("") }

    val backgroundColor = if (darkmode) Color.Black else Color(0xFFF2F2F7)
    val cardColor = if (darkmode) Color(0xFF1C1C1E) else Color.White

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Write a Review",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onCancel) {
                        Text("Cancel", color = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (UserProfileObject.userName != GUEST_USERNAME && rating > 0.0) {
                                onSend(
                                    Review(
                                        UserProfileObject.userName,
                                        UserProfileObject.userPfpReference,
                                        rating,
                                        reviewText
                                    )
                                )
                            }
                        }
                    ) {
                        Text("Send", color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            RatingBar(
                maxRating = 5,
                initialRating = rating,
                onRatingChanged = { newRating ->
                    rating = newRating
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "COMMENT",
                    fontSize = 13.sp,
                    color = Color(0xFF8E8E93),
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )

                Surface(
                    color = cardColor,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .heightIn(min = 150.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 17.sp,
                            color = if (darkmode) Color.White else Color.Black
                        ),
                        decorationBox = { innerTextField ->
                            if (reviewText.isEmpty()) {
                                Text(
                                    "Explain your experience (optional)",
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
        }
    }
}