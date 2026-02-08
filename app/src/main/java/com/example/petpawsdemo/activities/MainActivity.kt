package com.example.petpawsdemo.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachReversed
import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.model.SearchHistory
import com.example.petpawsdemo.model.UserProfileObject
import com.example.petpawsdemo.model.ViewData
import com.example.petpawsdemo.view.ProductContainer
import com.example.petpawsdemo.view.AppBar
import com.example.petpawsdemo.view.NavigationDrawer
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.petpawsdemo.model.GUEST_USERNAME
import com.example.petpawsdemo.model.UserProfile

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            val currentUser = UserProfileObject.loadCurrentUser(this@MainActivity)
            if (currentUser != null && currentUser != GUEST_USERNAME) {
                UserProfileObject.loadUserProfile(this@MainActivity, currentUser)
                UserProfile.loggedIn = true
            } else {
                UserProfile.loggedIn = false
            }
        }

        setContent {
            PetPawsDemoTheme (darkTheme = UserProfileObject.darkmode) {
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
                    if (f) {
                        query = ""
                        searching = true
                    }
                }
                val onSearch = {
                    searching = false
                    currentQuery = query
                    everSearched = true
                    focusManager.clearFocus()
                    if (!SearchHistory.history.contains(query)) {
                        SearchHistory.history.add(query)
                    }
                    Unit
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
                val onViewCart = {
                    val intent = Intent(context, CartActivity::class.java)
                    context.startActivity(intent)
                }
                val onResetSearch = {
                    searching = false
                    everSearched = false
                    query = ""
                    currentQuery = ""
                }

                val prefs = getSharedPreferences("pet_paws_prefs", MODE_PRIVATE)
                //prefs.edit().putBoolean("first_use", true).apply()

                if (prefs.getBoolean("first_use", true)) {
                    val onboardingIntent = Intent(context, OnboardingActivity::class.java)
                    context.startActivity(onboardingIntent)

                    prefs.edit { putBoolean("first_use", false) }
                }

                lifecycleScope.launch {
                    val savedUser = UserProfileObject.loadCurrentUser(this@MainActivity)

                    if (savedUser != null) {
                        UserProfileObject.loadUserProfile(this@MainActivity, savedUser)
                    } else {
                        UserProfileObject.loadUserProfile(this@MainActivity, GUEST_USERNAME)
                    }
                }
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
                    onViewProduct,
                    onResetSearch,
                    onViewCart
                )
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
    onViewProduct: (Int) -> Unit,
    onResetSearch: () -> Unit,
    onViewCart: () -> Unit,
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
                    onBack = onBack,
                    onResetSearch = onResetSearch,
                    onViewCart = onViewCart
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
                            products = ProductDatabase.getAll(),
                            innerPadding = innerPadding,
                            onClick = { id -> onViewProduct(id) }
                        )
                    } else {
                        ProductContainer(
                            products = ProductDatabase.search(currentQuery),
                            innerPadding = innerPadding,
                            onClick = { id -> onViewProduct(id) }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(innerPadding).padding(6.7.dp)
                        .fillMaxSize(1.0f)
                ) {
                    SearchHistory.history.fastForEachReversed{
                        Row(
                            modifier = Modifier.clickable{
                                onQueryChange(it)
                                onSearch()
                            }.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            IconButton(onClick = {
                                onQueryChange(it)
                                onSearch()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = "Hello!"
                                )
                            }
                            Text(
                                text = it,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}