package com.losaiassistant.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0D5C63),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8F1EF),
    onPrimaryContainer = Color(0xFF002021),
    secondary = Color(0xFF485D92),
    surface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFFE7ECEF),
    error = Color(0xFFB3261E),
)

@Composable
fun LosAiAssistantTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, typography = Typography, content = content)
}
