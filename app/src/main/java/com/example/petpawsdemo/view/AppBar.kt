package com.example.petpawsdemo.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpawsdemo.R
import com.example.petpawsdemo.activities.ProfileActivity
import com.example.petpawsdemo.viewmodel.UserCart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar (
    query: String,
    focus: Boolean,
    sorting: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    onFocus: (Boolean) -> Unit,
    onNavigationItemClick: () -> Unit,
    onResetSearch: () -> Unit,
    onViewCart: () -> Unit
) {
    TopAppBar (
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1.0f)
            ) {
                if (!sorting) {
                    SearchArea(
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = onSearch,
                        onFocus = onFocus,
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .clip(RoundedCornerShape(20)),
                        textStyle = TextStyle(fontSize = 15.sp, lineHeight = 20.sp),
                        onResetSearch = onResetSearch
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            if (!focus) {
                IconButton(onClick = onNavigationItemClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Hello!"
                    )
                }
            }
            else{
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Hello!"
                    )
                }
            }
        },
        actions = {
            if (focus){
                IconButton(onClick = {
                    onSearch()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        tint = Color.White,
                        contentDescription = "Hello!"
                    )
                }
            }
            else{
                val context = LocalContext.current
                Box(
                    modifier = Modifier.padding(5.dp)
                ) {
                    IconButton(onClick = {
                        if (UserCart.products.isNotEmpty()) {
                            onViewCart()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            tint = Color.White,
                            contentDescription = "Hello!"
                        )
                    }
                    if (UserCart.products.isNotEmpty()){
                        Box(
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Row(
                                modifier = Modifier.size(20.dp).clip(RoundedCornerShape(50))
                                    .background(Color.Red),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${UserCart.products.size}",
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            val context = LocalContext.current;
            IconButton(
                onClick = {
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.pfp),
                    contentDescription = "Profile picture"
                ) //switch to pfp activity
            }
        }
    )
}