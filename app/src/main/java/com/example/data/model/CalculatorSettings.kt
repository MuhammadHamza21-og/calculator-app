package com.example.data.model

import com.example.engine.AngleMode
import com.example.engine.NotationMode

enum class AppThemeMode {
    SYSTEM,
    DARK,
    LIGHT
}

data class CalculatorSettings(
    val angleMode: AngleMode = AngleMode.DEG,
    val precision: Int = 10,
    val notationMode: NotationMode = NotationMode.AUTO,
    val useThousandsSeparator: Boolean = true,
    val hapticFeedback: Boolean = true,
    val themeMode: AppThemeMode = AppThemeMode.DARK
)
