package com.example.petpawsdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petpawsdemo.ProductClasses.ExampleProducts
import com.example.petpawsdemo.ProductClasses.Product
import com.example.petpawsdemo.ProductClasses.ProductCategory
import com.example.petpawsdemo.ProductClasses.ProductContainer
import com.example.petpawsdemo.ProductClasses.ProductDatabase
import com.example.petpawsdemo.UIComponents.AppBar
import com.example.petpawsdemo.UIComponents.NavigationDrawer
import com.example.petpawsdemo.ui.theme.PetPawsDemoTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //BEGIN test product set

        val testProductSet: Set<Product> = setOf(Product(
            "Nutritious Pet Paws Dog Food",
            ProductCategory("dog","food"),
            listOf("dog", "food"),
            67,
            1520,
            3.4,
            100,
            listOf(),
            listOf(),
            "me",
            "Dog Food to appease your dogs"
            ),
            Product(
                "Nutritious Pet Paws Dog Food",
                ProductCategory("dog","food"),
                listOf("dog", "food"),
                67,
                1520,
                3.4,
                100,
                listOf(),
                listOf(),
                "me",
                "Dog Food to appease your dogs"
            ),
            Product(
                "Nutritious Pet Paws Dog Food",
                ProductCategory("dog","food"),
                listOf("dog", "food"),
                67,
                1520,
                3.4,
                100,
                listOf(),
                listOf(),
                "me",
                "Dog Food to appease your dogs"
            ),
            Product(
                "Nutritious Pet Paws Dog Food",
                ProductCategory("dog","food"),
                listOf("dog", "food"),
                67,
                1520,
                3.4,
                100,
                listOf(),
                listOf(),
                "me",
                "Dog Food to appease your dogs"
            ))
        //END test product set

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetPawsDemoTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed);
                val scope = rememberCoroutineScope()
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        NavigationDrawer(
                            testProductSet
                                .map { it.productCategory }
                                .toSet()
                            /*
                            ProductDatabase.getProductSet() //TODO
                            .map { it.productCategory }
                            .toSet()
                             */
                        )
                    }
                ) {
                    Scaffold(
                        topBar = {
                            AppBar(
                                onNavigationItemClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        val image = "https://media.istockphoto.com/id/539071535/photo/bowl-of-dog-food.jpg?s=612x612&w=0&k=20&c=48jSoNa5Vod-1inwbhpSQWKv5eEIhnWr8YAfhKI823M="
                        val prod = Product(
                            "Nutritious Pet Paws Dog Food",
                            ProductCategory("dog", "food"),
                            listOf("dog", "food"),
                            67,
                            1520,
                            3.4,
                            100,
                            listOf(),
                            listOf(image),
                            "me",
                            "Dog Food to appease your dogs"
                        )
                        val image2 = "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcSpWZMc_-DeArs8vPjvs_rkdHUs0VFAF-4Vgpif5BC9qYbW2fs8F6EwagFA_NvjMwJHcGN0Yza8REfPdAXW4rnepOLgyYefzm3g6498lK9YuHY5AEcqzSpc7w"
                        val prod2 = Product(
                            "Dog Cage",
                            ProductCategory("dog", "cage"),
                            listOf("dog", "cage"),
                            3,
                            1,
                            4.5,
                            1,
                            listOf(),
                            listOf(image2),
                            "me",
                            "Dog Cage to appease your dogs"
                        )
                        listOf(
                            ExampleProducts.PetPawsDogCage,
                            ExampleProducts.PetPawsDogCage,
                            ExampleProducts.PetPawsDogFood,
                            ExampleProducts.PetPawsDogCage,
                            ExampleProducts.PetPawsDogCage,
                        ).forEach{prod ->
                            ProductDatabase.addProduct(prod)
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f).padding(start = 10.dp, end = 10.dp)
                        ) {
                            ProductContainer(
                                "Dog",
                                ProductDatabase.getCategory("Dog"),
                                innerPadding
                            )
                        }

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
}
