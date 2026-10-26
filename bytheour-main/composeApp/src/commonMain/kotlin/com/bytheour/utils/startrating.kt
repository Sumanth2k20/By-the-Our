package com.bytheour.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.floor

@Composable
fun StarRating(rating: Double) {
    val fullStars = floor(rating).toInt()
    val isHalf = (rating - fullStars) >= 0.5
    val emptyStars = 5 - fullStars - if (isHalf) 1 else 0

    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(Modifier.width(1.dp))
        if (isHalf) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(Modifier.width(1.dp))
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))
        val decimal = roundToTwoDecimal(rating)
        Text( "$decimal/5".replace(".0", ""),
            fontSize = 14.sp,
            fontFamily =JtBtoFonts.getOpenSansRegular(),
        )
    }
}

fun roundToTwoDecimal(value: Double): Double {
    return kotlin.math.round(value * 100) / 100.0
}
