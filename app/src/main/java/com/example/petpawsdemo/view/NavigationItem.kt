package com.example.petpawsdemo.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.petpawsdemo.model.ProductCategory

open class NavigationItem (
    val id: Int,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
) {
    companion object {
        private var nextID = 0;
        fun genID(): Int = nextID++;
    }
}

class NavigationItemDropdown(
    val children: Set<ProductCategory>, //TODO
    val itemId: Int,
    val itemTitle: String,
    val itemSelectedIcon: ImageVector,
    val itemUnselectedIcon: ImageVector,
    val itemBadgeCount: Int? = null
) : NavigationItem(
    itemId,
    itemTitle,
    itemSelectedIcon,
    itemUnselectedIcon,
    itemBadgeCount
) {
    var navigationItemChildren: List<NavigationItem> =
        children.map{
            NavigationItem(
                id = NavigationItem.genID(),
                title = (it.type)[0].uppercase() + (it.type).substring(1).lowercase(),
                selectedIcon = Icons.Filled.KeyboardArrowUp,
                unselectedIcon = Icons.Filled.KeyboardArrowUp
            )
        }
}