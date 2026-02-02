package com.example.petpawsdemo.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpawsdemo.model.ProductCategory
import com.example.petpawsdemo.R
import com.example.petpawsdemo.activities.AboutUsActivity

val xkcdTextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.xkcdscript)),
    fontSize = 18.sp
)

@Composable
fun DrawerHeader() {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.bunny),
            contentDescription = "Bunny",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box( //js for contrast against white text
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.275f))
        )

        Text(
            text = "Everything Your Pets Need, \nOne Tap Away.",
            fontSize = 26.sp,
            style = xkcdTextStyle,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun DrawerBody(
    highItems: List<NavigationItem>,
    lowItems: List<NavigationItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = xkcdTextStyle,
    onItemClick: (NavigationItem) -> Unit //TODO
) {
    val selectedStates = remember{ mutableStateMapOf<Int, Boolean>() }
    NavigationItemGroup(highItems, selectedStates, modifier, itemTextStyle, onItemClick)
    Spacer(modifier = Modifier.fillMaxHeight(0.2f))
    HorizontalDivider()
    NavigationItemGroup(lowItems, selectedStates, modifier, itemTextStyle, onItemClick)
}

@Composable
fun NavigationItemGroup(
    items: List<NavigationItem>,
    selectedStates: SnapshotStateMap<Int, Boolean>,
    modifier: Modifier,
    itemTextStyle: TextStyle,
    onItemClick: (NavigationItem) -> Unit //TODO
) {
    val expandedChildStates = remember { mutableStateMapOf<Int, Boolean>() }
    val MAX_RECURSION_DEPTH = 2; //TODO: 1 for types, 1 for subtypes

    LazyColumn(modifier) {
        items(items.toList()) {item ->
            val isExpanded = expandedChildStates[item.id] ?: false;
            val isSelected = selectedStates[item.id]?: false
            val icon = if (isSelected) item.selectedIcon else item.unselectedIcon

            NavigationDrawerItem(
                label = {
                    Text(
                        text = item.title,
                        style = itemTextStyle,
                        modifier = Modifier.width(220.dp)
                    )},
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )},
                selected = isSelected,
                onClick = {
                    if (item is NavigationItemDropdown) {
                        expandedChildStates[item.id] = !isExpanded
                    }
                    else {
                        item.onClick()
                        onItemClick(item) //debug
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(8.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color.LightGray
                )
            )

            if (item is NavigationItemDropdown && isExpanded) {
                /*
                NavigationItemGroup(
                    items = item.navigationItemChildren,
                    selectedStates = remember{ mutableStateMapOf<Int, Boolean>() },
                    modifier = Modifier,
                    itemTextStyle = itemTextStyle,
                    onItemClick = onItemClick //TODO
                )
                */
                //TODO: implement recursive categorisation up to 2 recursions

                item.navigationItemChildren.forEach { child ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = child.title,
                                style = xkcdTextStyle
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardDoubleArrowRight,
                                contentDescription = null
                            )
                        },
                        selected = selectedStates[child.id] ?: false,
                        onClick = {
                            selectedStates[child.id] = true
                            onItemClick(child)
                        },
                        modifier = Modifier
                            .padding(start = 32.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationDrawer(
    productCategories: Set<ProductCategory>
) {
    val context = LocalContext.current
    val highItems = remember {
        listOf(
            NavigationItemDropdown(
                children = productCategories, itemId = NavigationItem.genID(),
                itemTitle = "Product Categories",
                itemSelectedIcon = Icons.Filled.ShoppingCart,
                itemUnselectedIcon = Icons.Filled.ShoppingCart,
                onClick = {}
            ),
        )
    }
    val lowItems = remember {
        listOf(
            NavigationItem(
                id =NavigationItem.genID(), title = "About Us",
                selectedIcon = Icons.Filled.Favorite,
                unselectedIcon = Icons.Filled.Favorite,
                onClick = {
                    val intent = Intent(context, AboutUsActivity::class.java)
                    context.startActivity(intent)
                }
            ),
            NavigationItem(
                id =NavigationItem.genID(), title = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Filled.Settings,
                onClick = {}
            ),
            NavigationItem(
                id =NavigationItem.genID(), title = "Logout",
                selectedIcon = Icons.Filled.PowerSettingsNew,
                unselectedIcon = Icons.Filled.PowerSettingsNew,
                onClick = {}
            )
        )
    }

    Column (
        Modifier
            .background( color = Color.White )
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
    ) {
        DrawerHeader()
        DrawerBody(
            highItems = highItems,
            lowItems = lowItems,
            modifier = Modifier
                .padding(16.dp),
            itemTextStyle = xkcdTextStyle,
            onItemClick = { //TODO
                //Toast.makeText(context, "Clicked on ${it.title}", Toast.LENGTH_SHORT).show();
            }
        )
    }
}