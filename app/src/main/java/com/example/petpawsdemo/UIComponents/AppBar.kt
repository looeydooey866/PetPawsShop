package com.example.petpawsdemo.UIComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar (
    query: String,
    focus: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    onFocus: (Boolean) -> Unit,
    onNavigationItemClick: () -> Unit
) {
    TopAppBar (
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1.0f)
            ) {
                SearchArea(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = onSearch,
                    onFocus = onFocus,
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(20)),
                    textStyle = TextStyle(fontSize = 15.sp, lineHeight = 20.sp)
                )
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
        }
    )
}