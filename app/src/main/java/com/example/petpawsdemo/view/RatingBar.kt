package com.example.petpawsdemo.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.floor

@Composable
fun RatingBar(
    maxRating: Int = 5,
    initialRating: Float = 0f,
    onRatingChanged: (Float) -> Unit = {}
) {
    //BEGIN data stuff
    var rating by remember { mutableStateOf<Float>(initialRating) }
    val interactionSource = remember { MutableInteractionSource() }

    val starSizeModifier = Modifier
        .size(32.dp, 32.dp)
        .padding(0.dp, 0.dp)
    //END data stuff

    Box {
        //grey stars
        Row {
            for (i in 1..maxRating) {
                Icon (
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating ${i}",
                    tint = Color.Gray,
                    modifier = starSizeModifier
                )
            }
        }

        //cyan stars
        Row {
            for (i in 1..floor(rating).toInt()) {
                Icon (
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating ${i}",
                    tint = Color.Cyan,
                    modifier = starSizeModifier
                )
            }

            if (rating == floor(rating) + 0.5f) { // has half star
                Icon (
                    imageVector = Icons.AutoMirrored.Filled.StarHalf,
                    contentDescription = "Rating 0.5 Contribution",
                    tint = Color.Cyan,
                    modifier = starSizeModifier
                )
            }
        }

        //hitboxes
        Row {
            for (i in 1..(maxRating * 2)) {
                Box (
                    modifier = Modifier
                        .size(16.dp, 32.dp)
                        .padding(0.dp, 0.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            rating = i * 0.5f
                            onRatingChanged(rating)
                        }
                    //.background( color = Color.Yellow ) //debugging
                )
            }
        }
    }
}