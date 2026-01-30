package com.example.petpawsdemo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.model.ViewData
import com.example.petpawsdemo.view.ProductContainer
import com.example.petpawsdemo.view.AppBar
import com.example.petpawsdemo.view.NavigationDrawer
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import com.example.petpawsdemo.viewmodel.CartViewModel
import com.example.petpawsdemo.viewmodel.CartViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val cartViewModel: CartViewModel by viewModels { CartViewModelFactory(application) }
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
                var everSearched by remember{mutableStateOf(false)}
                val focusManager = LocalFocusManager.current
                val context = LocalContext.current
                val onQueryChange = {s: String ->
                    query = s
                }
                val onFocus = {f: Boolean ->
                    if (f){
                        query = ""
                        searching = true
                        Toast.makeText(context, "Focus", Toast.LENGTH_SHORT).show()
                    }
                }
                val onSearch = {
                    searching = false
                    currentQuery = query
                    everSearched = true
                    focusManager.clearFocus()
                }
                val onBack = {
                    searching = false
                    focusManager.clearFocus()
                    query = currentQuery
                }
                val onViewProduct = {product: Int->
                    ViewData.viewingId = product
                    val intent = Intent(context, ViewProductActivity::class.java)
                    context.startActivity(intent)
                }
                HomeScreen(
                    cartViewModel,
                    drawerState,
                    query,
                    searching,
                    onQueryChange,
                    onFocus,
                    onSearch,
                    scope,
                    onBack,
                    everSearched,
                    currentQuery,
                    onViewProduct
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    cartViewModel: CartViewModel,
    drawerState: DrawerState,
    query: String,
    searching: Boolean,
    onQueryChange: (String) -> Unit,
    onFocus: (Boolean) -> Unit,
    onSearch: () -> Unit,
    scope: CoroutineScope,
    onBack: () -> Unit,
    everSearched: Boolean,
    currentQuery: String,
    onViewProduct: (Int) -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                ProductDatabase.getProductSet()
                    .map { it.productCategory }
                    .toSet()
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppBar(
                    cartViewModel = cartViewModel,
                    query = query,
                    focus = searching,
                    onQueryChange = onQueryChange,
                    onFocus = onFocus,
                    onSearch = onSearch,
                    onNavigationItemClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onBack = onBack
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            if (!searching) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1.0f)
                        .fillMaxHeight(1.0f)
                        .padding(start = 10.dp, end = 10.dp)
                ) {
                    if (!everSearched) {
                        ProductContainer(
                            ProductDatabase.getAll(),
                            innerPadding,
                            onClick = {id ->
                                onViewProduct(id)
                            }
                        )
                    } else {
                        ProductContainer(
                            ProductDatabase.search(currentQuery),
                            innerPadding,
                            onClick = {id ->
                                onViewProduct(id)
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(1.0f)
                ) {
                    Text(text = "yo")
                    Text(text = "yo2")
                    Text(text = "yo3")
                    Text(text = "yo4")
                }
            }
        }
    }
}