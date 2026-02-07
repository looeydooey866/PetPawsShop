package com.example.petpawsdemo.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.petpawsdemo.model.ProductCategory

open class NavigationItem(
    val id: Int,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val onClick: () -> Unit
) {
    companion object {
        private var nextID = 0
        fun genID(): Int = nextID++
    }
}


class NavigationItemDropdown(
    val children: Set<ProductCategory>,
    itemId: Int,
    itemTitle: String,
    itemSelectedIcon: ImageVector,
    itemUnselectedIcon: ImageVector,
    itemBadgeCount: Int? = null,
    onClick: () -> Unit
) : NavigationItem(
    id = itemId,
    title = itemTitle,
    selectedIcon = itemSelectedIcon,
    unselectedIcon = itemUnselectedIcon,
    badgeCount = itemBadgeCount,
    onClick = onClick
) {
    val navigationItemChildren: List<NavigationItem> =
        children.map {
            NavigationItem(
                id = NavigationItem.genID(),
                title = it.subtype.replaceFirstChar { c -> c.uppercase() },
                selectedIcon = Icons.Filled.KeyboardArrowUp,
                unselectedIcon = Icons.Filled.KeyboardArrowUp,
                onClick = onClick
            )
        }
}