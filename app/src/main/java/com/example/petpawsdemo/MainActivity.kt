package com.example.petpawsdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpawsdemo.ui.theme.PetPawsDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetPawsDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val image = "https://media.istockphoto.com/id/539071535/photo/bowl-of-dog-food.jpg?s=612x612&w=0&k=20&c=48jSoNa5Vod-1inwbhpSQWKv5eEIhnWr8YAfhKI823M="
                    val prod = Product(
                        "Nutritious Pet Paws Dog Food",
                        "dog",
                        "food",
                        listOf("dog", "food"),
                        1,
                        1520,
                        5,
                        100,
                        listOf(),
                        listOf(image),
                        "me",
                        "Dog Food to appease your dogs"
                    )
                    val image2 = "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcSpWZMc_-DeArs8vPjvs_rkdHUs0VFAF-4Vgpif5BC9qYbW2fs8F6EwagFA_NvjMwJHcGN0Yza8REfPdAXW4rnepOLgyYefzm3g6498lK9YuHY5AEcqzSpc7w"
                    val prod2 = Product(
                        "Dog Cage",
                        "dog",
                        "food",
                        listOf("dog", "cage"),
                        1,
                        1,
                        10,
                        1,
                        listOf(),
                        listOf(image2),
                        "me",
                        "Dog Cage to appease your dogs"
                    )
                    ProductContainer(listOf(
                        prod, prod, prod2, prod
                    ), innerPadding)
                        /*
                        Column(
                            modifier = Modifier.fillMaxWidth(1.0f).padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
                        ) {
                            Text(
                                text = "Dog Items",
                                fontSize = 35.sp,
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(1.0f).height(3.dp).background(Color.Black),
                            ){}
                        }
                         */
                    /*
                    Column(modifier = Modifier.padding(innerPadding).fillMaxWidth(1.0f).fillMaxHeight(1.0f).verticalScroll(scrollState)) {
                        val mod = Modifier
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
                    }
                     */
                }
            }
        }
    }
}
