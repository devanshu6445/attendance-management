package com.college.attendance.management.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

fun darkColors(
    primary: Color = Color(0xFFBB86FC),
    primaryVariant: Color = Color(0xFF3700B3),
    secondary: Color = Color(0xFF03DAC6),
    secondaryVariant: Color = secondary,
    background: Color = Color(0xFF121212),
    surface: Color = Color(0xFF121212),
    error: Color = Color(0xFFCF6679),
    onPrimary: Color = Color.Black,
    onSecondary: Color = Color.Black,
    onBackground: Color = Color.White,
    onSurface: Color = Color.White,
    onError: Color = Color.Black
): Colors = Colors(
    primary,
    primaryVariant,
    secondary,
    secondaryVariant,
    background,
    surface,
    error,
    onPrimary,
    onSecondary,
    onBackground,
    onSurface,
    onError
)

data class Colors(
    val primary: Color = Color(0xFFBB86FC),
    val primaryVariant: Color = Color(0xFF3700B3),
    val secondary: Color = Color(0xFF03DAC6),
    val secondaryVariant: Color = secondary,
    val background: Color = Color(0xFF121212),
    val surface: Color = Color(0xFF121212),
    val error: Color = Color(0xFFCF6679),
    val onPrimary: Color = Color.Black,
    val onSecondary: Color = Color.Black,
    val onBackground: Color = Color.White,
    val onSurface: Color = Color.White,
    val onError: Color = Color.Black
)