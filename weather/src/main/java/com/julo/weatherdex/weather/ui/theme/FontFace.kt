package com.julo.weatherdex.weather.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object FontFace {
    object Small {
        private const val fontSize = 11
        val normal = TextStyle(
            color = Color.Black,
            fontSize = fontSize.sp,
        )

        val secondary = TextStyle(
            color = Color.Gray,
            fontSize = fontSize.sp,
        )

        val bold = TextStyle(
            color = Color.Black,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.SemiBold
        )
    }

    object Regular {
        private const val fontSize = 12
        val normal = TextStyle(
            color = Color.Black,
            fontSize = fontSize.sp,
        )

        val secondary = TextStyle(
            color = Color.Gray,
            fontSize = fontSize.sp,
        )

        val bold = TextStyle(
            color = Color.Black,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.SemiBold
        )
    }

    object Big {
        private const val fontSize = 16
        val normal = TextStyle(
            color = Color.Black,
            fontSize = fontSize.sp,
        )

        val secondary = TextStyle(
            color = Color.Gray,
            fontSize = fontSize.sp,
        )

        val bold = TextStyle(
            color = Color.Black,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.SemiBold
        )
    }

    object Huge {
        private const val fontSize = 24
        val normal = TextStyle(
            color = Color.Black,
            fontSize = fontSize.sp,
        )

        val secondary = TextStyle(
            color = Color.Gray,
            fontSize = fontSize.sp,
        )

        val bold = TextStyle(
            color = Color.Black,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
