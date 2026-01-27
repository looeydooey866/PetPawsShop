package com.example.petpawsdemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.petpawsdemo.ProductClasses.CategorialProductContainer
import com.example.petpawsdemo.ProductClasses.SearchedProductContainer
import com.example.petpawsdemo.ProductClasses.ProductScreen
import com.example.petpawsdemo.ProductClasses.UserCart
import com.example.petpawsdemo.UIComponents.AppBar
import com.example.petpawsdemo.UIComponents.NavigationDrawer
import com.example.petpawsdemo.ui.theme.PetPawsDemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetPawsDemoTheme {


                val context = LocalContext.current
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var currentQuery by remember{mutableStateOf("")}
                var query by remember{mutableStateOf("")}
                var searching by remember{mutableStateOf(false)}
                var everSearched by remember{mutableStateOf(false)}
                var viewing by remember{mutableStateOf(false)}
                var currentlyViewing by remember{mutableIntStateOf(-1)}
                var purchaseQuantity by remember{mutableIntStateOf(-1)}
                val focusManager = LocalFocusManager.current
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
                val onViewProduct = {id: Int ->
                    viewing = true
                    currentlyViewing = id
                    purchaseQuantity = 1
                }
                val onChangeQuantity = {quantity: Int ->
                    purchaseQuantity = quantity
                }
                val onViewBack = {
                    viewing = false
                }
                val onBuy = {
                    onViewBack()
                    UserCart.addProduct(currentlyViewing, purchaseQuantity)
                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                }
                if (!viewing) {
                    HomeScreen(
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
                else{
                    ProductScreen(currentlyViewing, purchaseQuantity, onChangeQuantity, onViewBack, onBuy)
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(
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
                        CategorialProductContainer(
                            ProductDatabase.getAll(),
                            innerPadding
                        ){ id ->
                            onViewProduct(id)
                        }
                    } else {
                        SearchedProductContainer(
                            ProductDatabase.search(currentQuery),
                            innerPadding
                        ){ id ->
                            onViewProduct(id)
                        }
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
