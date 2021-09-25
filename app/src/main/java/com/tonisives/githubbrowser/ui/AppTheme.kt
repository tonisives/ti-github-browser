package com.tonisives.githubbrowser.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightColors = lightColors(
    primary = Color(0xff4681B9),
    secondary = Color(0xffB97E46),
)

@Composable
fun AppTheme(content: @Composable () -> Unit) =
    MaterialTheme(colors = LightColors, content = content)