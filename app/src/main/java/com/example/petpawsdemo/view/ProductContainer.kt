package com.example.petpawsdemo.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpawsdemo.model.Product
import com.example.petpawsdemo.model.ProductDatabase
import com.example.petpawsdemo.model.UserProfileObject

@Composable
fun ProductContainer(
    products: Map<String, Map<String, List<Product>>>,
    innerPadding: PaddingValues,
    onClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth(),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        products.forEach { (type, subMap) ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategorySeparator(type)
            }

            subMap.forEach { (subtype, productList) ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SubCategorySeparator(type, subtype)
                }

                items(productList) { p ->
                    val id = ProductDatabase.getID(p)
                    if (id != null) {
                        // Retrieve the live product instance from the database
                        //println("fuck you, ${p}, ${id}")
                        val product = ProductDatabase.getProduct(id)
                        if (product != null) {
                            ProductCard(product) {
                                onClick(id)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductContainer(
    innerPadding: PaddingValues,
    onClick: (Int) -> Unit
) {
    val products: List<Product> = ProductDatabase.getProductSet().toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth(),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(products) { p ->
            val id = ProductDatabase.getID(p)
            if (id != null) {
                // Retrieve the live product instance from the database
                val product = ProductDatabase.getProduct(id)
                if (product != null) {
                    ProductCard(product) {
                        onClick(id)
                    }
                }
            }
        }
    }
}

@Composable
fun SubCategorySeparator(type: String, subtype: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = type.replaceFirstChar{it.uppercase()} + " " + subtype.replaceFirstChar { it.uppercase() },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (UserProfileObject.darkmode) Color.LightGray else Color.DarkGray
        )
    }
}

@Composable
fun CategorySeparator(type: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1.0f)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Text(
            text = type.replaceFirstChar{it.uppercase()} + " Items",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier
                .weight(1.0f)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {}
    }
}
