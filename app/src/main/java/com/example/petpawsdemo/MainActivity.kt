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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.petpawsdemo.ProductClasses.ProductContainer
import com.example.petpawsdemo.UIComponents.AppBar
import com.example.petpawsdemo.UIComponents.NavigationDrawer
import com.example.petpawsdemo.ui.theme.PetPawsDemoTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetPawsDemoTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var currentQuery by remember{mutableStateOf("")}
                var query by remember{mutableStateOf("")}
                var searching by remember{mutableStateOf(false)}
                val focusManager = LocalFocusManager.current
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        NavigationDrawer(
                            /*
                            testProductSet
                                .map { it.productCategory }
                                .toSet()
                                */
                            ProductDatabase.getProductSet()
                                .map { it.productCategory }
                                .toSet()
                        )
                    }
                ) {
                    Scaffold(
                        topBar = {
                            AppBar(
                                query = query,
                                onQueryChange = {query = it},
                                onFocus = {searching = it},
                                onSearch = {
                                    searching = false
                                    currentQuery = query
                                    query = ""
                                    focusManager.clearFocus()
                                           },
                                onNavigationItemClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        if (!searching) {
                            Column(
                                modifier = Modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
                                    .padding(start = 10.dp, end = 10.dp)
                            ) {
                                ProductContainer(
                                    ProductDatabase.search(currentQuery),
                                    innerPadding
                                )
                            }
                            /*
                        Column(
                            modifier = Modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f).padding(start = 10.dp, end = 10.dp)
                        ) {
                            ProductContainer(
                                "Dog",
                                ProductDatabase.getCategory("Dog"),
                                innerPadding
                            )
                        }

                         */

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
                        else{
                            Column(
                                modifier = Modifier.padding(innerPadding).fillMaxSize(1.0f)
                            ){
                                Text(text = "yo")
                                Text(text = "yo2")
                                Text(text = "yo3")
                                Text(text = "yo4")
                            }
                        }
                    }
                }
            }
        }
    }
}
