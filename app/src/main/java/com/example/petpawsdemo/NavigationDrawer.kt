package com.example.petpawsdemo

import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val xkcdFontFamily = FontFamily(Font(R.font.xkcdscript))

@Composable
fun DrawerHeader() {
    Box (
        modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        /*
        ImageView(
            context = this,

        )
         */
        Text(
            text = "Everything Your Pets Need, One Tap Away.",
            fontSize = 30.sp,
            fontFamily = xkcdFontFamily,
            color = Color.Black,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 64.sp
        )
    }
}

@Composable
fun DrawerBody(
    items: List<NavigationItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(
        fontSize = 40.sp,
        fontFamily = xkcdFontFamily
    ),
    onItemClick: (NavigationItem) -> Unit
) {
    val selectedStates = remember{ mutableStateMapOf<Int, Boolean>() }

    LazyColumn(modifier) {
        items(items) {item ->
            val isSelected = selectedStates[item.id] ?: false
            val icon = if (isSelected) item.selectedIcon else item.unselectedIcon

            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .clickable() { onItemClick(item) }
            ) {
                Icon(imageVector = icon, contentDescription = null) //before click event, unselected
                Spacer(Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun NavigationDrawer() {
    val context = LocalContext.current

    Column (
        Modifier
            .background( color = Color.White )
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
    ) {
        DrawerHeader()
        DrawerBody(
            items = listOf(
                NavigationItem(
                    id = 0, title = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Filled.Home
                ),
                NavigationItem(
                    id = 1, title = "Product Categories",
                    selectedIcon = Icons.Filled.ShoppingCart,
                    unselectedIcon = Icons.Filled.ShoppingCart
                ),
                NavigationItem(
                    id = 2, title = "Profile",
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Filled.Person
                ),
                NavigationItem(
                    id = 3, title = "About Us",
                    selectedIcon = Icons.Filled.Favorite,
                    unselectedIcon = Icons.Filled.Favorite
                ),
                NavigationItem(
                    id = 4, title = "Settings",
                    selectedIcon = Icons.Filled.Settings,
                    unselectedIcon = Icons.Filled.Settings
                ),
                NavigationItem(
                    id = 5, title = "Logout",
                    selectedIcon = Icons.Filled.PowerSettingsNew,
                    unselectedIcon = Icons.Filled.PowerSettingsNew
                )
            ),
            modifier = Modifier
                .padding(16.dp),
            itemTextStyle = TextStyle(fontFamily = xkcdFontFamily),
            onItemClick = { //TODO
                Toast.makeText(context, "Clicked on ${it.title}", Toast.LENGTH_SHORT).show();
            }
        )
    }
}

data class NavigationItem (
    val id: Int,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
)