package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MemoryBar(
    hasMemory: Boolean,
    onMemoryClear: () -> Unit,
    onMemoryRecall: () -> Unit,
    onMemoryAdd: () -> Unit,
    onMemorySubtract: () -> Unit,
    onMemoryStore: () -> Unit,
    hapticEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val btnModifier = Modifier.weight(1f)

        CalculatorButton(
            text = "MC",
            onClick = onMemoryClear,
            modifier = btnModifier,
            variant = if (hasMemory) ButtonVariant.ACCENT else ButtonVariant.NUMBER,
            fontSize = 13.sp,
            minHeight = 36.dp,
            hapticEnabled = hapticEnabled,
            testTag = "btn_mc"
        )
        CalculatorButton(
            text = "MR",
            onClick = onMemoryRecall,
            modifier = btnModifier,
            variant = if (hasMemory) ButtonVariant.ACCENT else ButtonVariant.NUMBER,
            fontSize = 13.sp,
            minHeight = 36.dp,
            hapticEnabled = hapticEnabled,
            testTag = "btn_mr"
        )
        CalculatorButton(
            text = "M+",
            onClick = onMemoryAdd,
            modifier = btnModifier,
            variant = ButtonVariant.NUMBER,
            fontSize = 13.sp,
            minHeight = 36.dp,
            hapticEnabled = hapticEnabled,
            testTag = "btn_m_plus"
        )
        CalculatorButton(
            text = "M−",
            onClick = onMemorySubtract,
            modifier = btnModifier,
            variant = ButtonVariant.NUMBER,
            fontSize = 13.sp,
            minHeight = 36.dp,
            hapticEnabled = hapticEnabled,
            testTag = "btn_m_minus"
        )
        CalculatorButton(
            text = "MS",
            onClick = onMemoryStore,
            modifier = btnModifier,
            variant = ButtonVariant.NUMBER,
            fontSize = 13.sp,
            minHeight = 36.dp,
            hapticEnabled = hapticEnabled,
            testTag = "btn_ms"
        )
    }
}
