package com.example.graphqlandcomposepoc.view.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        color = Color.White,
        fontSize = 14.sp
    ),
    h1 = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        color = Color.White,
        fontSize = 26.sp
    ),
)