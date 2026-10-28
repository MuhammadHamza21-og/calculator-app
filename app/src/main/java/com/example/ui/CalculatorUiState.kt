package com.example.ui

import com.example.data.model.CalculationHistoryItem
import com.example.data.model.CalculatorSettings
import com.example.engine.AngleMode

data class CalculatorUiState(
    val expression: String = "",
    val result: String = "0",
    val previewResult: String? = null,
    val errorMessage: String? = null,
    val angleMode: AngleMode = AngleMode.DEG,
    val isSecondFunction: Boolean = false,
    val isScientificExpanded: Boolean = true,
    val hasMemory: Boolean = false,
    val memoryValue: Double = 0.0,
    val history: List<CalculationHistoryItem> = emptyList(),
    val settings: CalculatorSettings = CalculatorSettings(),
    val isHistoryOpen: Boolean = false,
    val isSettingsOpen: Boolean = false,
    val isGuideOpen: Boolean = false,
    val lastEvaluated: Boolean = false
)
