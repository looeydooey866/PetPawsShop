package com.example.petpawsdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petpawsdemo.ui.theme.PetPawsDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetPawsDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val scrollState = rememberScrollState()
                    val image = "https://media.istockphoto.com/id/539071535/photo/bowl-of-dog-food.jpg?s=612x612&w=0&k=20&c=48jSoNa5Vod-1inwbhpSQWKv5eEIhnWr8YAfhKI823M="
                    val prod = Product(
                        "Dog Food",
                        listOf("dog", "food"),
                        1,
                        1,
                        listOf(image),
                        "me",
                        "Dog Food to appease your dogs"
                    )
                    val image2 = "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcSpWZMc_-DeArs8vPjvs_rkdHUs0VFAF-4Vgpif5BC9qYbW2fs8F6EwagFA_NvjMwJHcGN0Yza8REfPdAXW4rnepOLgyYefzm3g6498lK9YuHY5AEcqzSpc7w"
                    val prod2 = Product(
                        "Dog Cage",
                        listOf("dog", "cage"),
                        1,
                        1,
                        listOf(image2),
                        "me",
                        "Dog Cage to appease your dogs"
                    )
                    Column(modifier = Modifier.padding(innerPadding).fillMaxWidth(1.0f).fillMaxHeight(1.0f).verticalScroll(scrollState)) {
                        Text(
                            text = "Dog Items",
                            fontSize = 40.sp,
                        )
                        val mod = Modifier
                            .aspectRatio(6f / 7)
                            .background(Color.White)
                            .padding(5.dp)
                            .weight(1.0f)
                        Row(
                            modifier = Modifier.fillMaxWidth(1.0f)
                        ) {
                            ProductCard(
                                product = prod,
                                modifier = mod
                            ){}
                            ProductCard(
                                product = prod2,
                                modifier = mod
                            ){}
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(1.0f)
                        ) {
                            ProductCard(
                                product = prod,
                                modifier = mod
                            ){}
                            ProductCard(
                                product = prod2,
                                modifier = mod
                            ){}
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(1.0f)
                        ) {
                            ProductCard(
                                product = prod,
                                modifier = mod
                            ){}
                            ProductCard(
                                product = prod2,
                                modifier = mod
                            ){}
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(1.0f)
                        ) {
                            ProductCard(
                                product = prod,
                                modifier = mod
                            ){}
                            ProductCard(
                                product = prod2,
                                modifier = mod
                            ){}
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(1.0f)
                        ) {
                            ProductCard(
                                product = prod,
                                modifier = mod
                            ){}
                            ProductCard(
                                product = prod2,
                                modifier = mod
                            ){}
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(1.0f)
                        ) {
                            ProductCard(
                                product = prod,
                                modifier = mod
                            ){}
                            ProductCard(
                                product = prod2,
                                modifier = mod
                            ){}
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(1.0f)
                        ) {
                            ProductCard(
                                product = prod,
                                modifier = mod
                            ){}
                            ProductCard(
                                product = prod2,
                                modifier = mod
                            ){}
                        }
                        /*
                        Column(
                            modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(1.0f).verticalScroll(scrollState)
                        ) {
                            ProductCard(
                                product = prod2
                            ){}
                            ProductCard(
                                product = prod2
                            ){}
                            ProductCard(
                                product = prod2
                            ){}
                            ProductCard(
                                product = prod2
                            ){}
                            ProductCard(
                                product = prod2
                            ){}
                        }
                         */
                    }
                }
            }
        }
    }
}
