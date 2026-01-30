package com.example.petpawsdemo.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petpawsdemo.ProductDatabase
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.model.UserProfile
import com.example.petpawsdemo.view.AppBar
import com.example.petpawsdemo.view.ui.theme.PetPawsDemoTheme
import kotlinx.coroutines.launch

class ProfileActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PetPawsDemoTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "${UserProfile.userName}'s Profile") },
                            navigationIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    modifier = Modifier.clickable{finish()}
                                )
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
                    ) {
                        ProfileScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen() {
    val accountHandle = "@" + UserProfile.userName
    val recentPurchases = UserProfile.getPurchasedItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserProfile.userPfpReference?.let { pfpId ->
            Image(
                painter = painterResource(id = pfpId),
                contentDescription = "",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 12.dp)
            )
        }

        Text(
            text = accountHandle,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Recent Purchases",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(recentPurchases) { product ->
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
