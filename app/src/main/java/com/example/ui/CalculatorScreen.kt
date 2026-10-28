package com.example.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.ui.components.CalculatorDisplay
import com.example.ui.components.CalculatorKeypad
import com.example.ui.components.FunctionGuideDialog
import com.example.ui.components.HistoryDrawer
import com.example.ui.components.LandscapeKeypad
import com.example.ui.components.MemoryBar
import com.example.ui.components.ModeSelectorBar
import com.example.ui.components.SettingsDialog

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            val maxW = maxWidth

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = if (isLandscape) 900.dp else 600.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Mode / Navigation Bar
                ModeSelectorBar(
                    angleMode = uiState.angleMode,
                    isScientificExpanded = uiState.isScientificExpanded,
                    isSecondFunction = uiState.isSecondFunction,
                    onToggleAngleMode = viewModel::onToggleAngleMode,
                    onToggleScientific = viewModel::onToggleScientificLayout,
                    onToggleSecond = viewModel::onToggleSecondFunction,
                    onOpenHistory = { viewModel.setHistoryOpen(true) },
                    onOpenGuide = { viewModel.setGuideOpen(true) },
                    onOpenSettings = { viewModel.setSettingsOpen(true) }
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Calculator Display (Formula + Live Preview + Result)
                CalculatorDisplay(
                    expression = uiState.expression,
                    result = uiState.result,
                    previewResult = uiState.previewResult,
                    errorMessage = uiState.errorMessage,
                    angleMode = uiState.angleMode,
                    hasMemory = uiState.hasMemory,
                    memoryValue = uiState.memoryValue,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Memory Bar (shown in portrait or if space permits)
                if (!isLandscape) {
                    MemoryBar(
                        hasMemory = uiState.hasMemory,
                        onMemoryClear = viewModel::onMemoryClear,
                        onMemoryRecall = viewModel::onMemoryRecall,
                        onMemoryAdd = viewModel::onMemoryAdd,
                        onMemorySubtract = viewModel::onMemorySubtract,
                        onMemoryStore = viewModel::onMemoryStore,
                        hapticEnabled = uiState.settings.hapticFeedback
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Keypad: Portrait or Landscape layout
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    if (isLandscape) {
                        LandscapeKeypad(
                            isSecondFunction = uiState.isSecondFunction,
                            hapticEnabled = uiState.settings.hapticFeedback,
                            onDigit = viewModel::onDigit,
                            onDecimal = viewModel::onDecimal,
                            onOperator = viewModel::onOperator,
                            onFunction = viewModel::onFunction,
                            onConstant = viewModel::onConstant,
                            onParenthesis = viewModel::onParenthesis,
                            onFactorial = viewModel::onFactorial,
                            onPercentage = viewModel::onPercentage,
                            onToggleSign = viewModel::onToggleSign,
                            onClearAll = viewModel::onClearAll,
                            onDelete = viewModel::onDelete,
                            onEquals = viewModel::onEquals
                        )
                    } else {
                        CalculatorKeypad(
                            isScientificExpanded = uiState.isScientificExpanded,
                            isSecondFunction = uiState.isSecondFunction,
                            hapticEnabled = uiState.settings.hapticFeedback,
                            onDigit = viewModel::onDigit,
                            onDecimal = viewModel::onDecimal,
                            onOperator = viewModel::onOperator,
                            onFunction = viewModel::onFunction,
                            onConstant = viewModel::onConstant,
                            onParenthesis = viewModel::onParenthesis,
                            onFactorial = viewModel::onFactorial,
                            onPercentage = viewModel::onPercentage,
                            onToggleSign = viewModel::onToggleSign,
                            onClearAll = viewModel::onClearAll,
                            onDelete = viewModel::onDelete,
                            onEquals = viewModel::onEquals
                        )
                    }
                }
            }
        }

        // History Modal Sheet
        HistoryDrawer(
            isOpen = uiState.isHistoryOpen,
            history = uiState.history,
            onDismiss = { viewModel.setHistoryOpen(false) },
            onSelectItem = { item -> viewModel.onSelectHistoryItem(item) },
            onDeleteItem = { id -> viewModel.onDeleteHistoryItem(id) },
            onClearAll = { viewModel.onClearAllHistory() }
        )

        // Settings Modal Sheet
        SettingsDialog(
            isOpen = uiState.isSettingsOpen,
            settings = uiState.settings,
            onDismiss = { viewModel.setSettingsOpen(false) },
            onUpdateSettings = { newSettings -> viewModel.onUpdateSettings(newSettings) }
        )

        // Function Guide Dialog
        FunctionGuideDialog(
            isOpen = uiState.isGuideOpen,
            onDismiss = { viewModel.setGuideOpen(false) }
        )
    }
}
