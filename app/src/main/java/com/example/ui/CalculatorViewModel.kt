package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.CalculationHistoryItem
import com.example.data.model.CalculatorSettings
import com.example.data.repository.CalculatorRepository
import com.example.data.repository.SettingsManager
import com.example.engine.AngleMode
import com.example.engine.CalculationEngine
import com.example.engine.CalculationResult
import com.example.engine.MathFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val repository: CalculatorRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val engine = CalculationEngine()
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        // Collect settings
        viewModelScope.launch {
            settingsManager.settings.collect { newSettings ->
                engine.angleMode = newSettings.angleMode
                engine.precision = newSettings.precision
                engine.notationMode = newSettings.notationMode
                engine.useThousandsSeparator = newSettings.useThousandsSeparator

                _uiState.update { current ->
                    current.copy(
                        settings = newSettings,
                        angleMode = newSettings.angleMode
                    )
                }
                updatePreview()
            }
        }

        // Collect calculation history
        viewModelScope.launch {
            repository.getHistory().collect { historyList ->
                _uiState.update { it.copy(history = historyList) }
            }
        }
    }

    fun onDigit(digit: String) {
        _uiState.update { current ->
            val newExpr = if (current.lastEvaluated) {
                digit
            } else {
                current.expression + digit
            }
            current.copy(
                expression = newExpr,
                errorMessage = null,
                lastEvaluated = false
            )
        }
        updatePreview()
    }

    fun onDecimal() {
        _uiState.update { current ->
            val expr = if (current.lastEvaluated) "0." else {
                if (current.expression.isEmpty() || current.expression.last() in "+-×*÷/^,(") {
                    current.expression + "0."
                } else {
                    current.expression + "."
                }
            }
            current.copy(
                expression = expr,
                errorMessage = null,
                lastEvaluated = false
            )
        }
        updatePreview()
    }

    fun onOperator(op: String) {
        _uiState.update { current ->
            val expr = if (current.lastEvaluated) {
                // Chain from previous result
                current.result + op
            } else if (current.expression.isEmpty()) {
                if (op == "-") "-" else ""
            } else {
                val lastChar = current.expression.last()
                if (lastChar in "+-×*÷/") {
                    // Replace operator if preceding is an operator
                    current.expression.dropLast(1) + op
                } else {
                    current.expression + op
                }
            }
            current.copy(
                expression = expr,
                errorMessage = null,
                lastEvaluated = false
            )
        }
        updatePreview()
    }

    fun onFunction(fn: String) {
        _uiState.update { current ->
            val newExpr = if (current.lastEvaluated) {
                "$fn(${current.result})"
            } else {
                current.expression + "$fn("
            }
            current.copy(
                expression = newExpr,
                errorMessage = null,
                lastEvaluated = false
            )
        }
        updatePreview()
    }

    fun onConstant(constant: String) {
        _uiState.update { current ->
            val newExpr = if (current.lastEvaluated) {
                constant
            } else {
                current.expression + constant
            }
            current.copy(
                expression = newExpr,
                errorMessage = null,
                lastEvaluated = false
            )
        }
        updatePreview()
    }

    fun onParenthesis(paren: String) {
        _uiState.update { current ->
            val newExpr = if (current.lastEvaluated && paren == "(") {
                paren
            } else {
                current.expression + paren
            }
            current.copy(
                expression = newExpr,
                errorMessage = null,
                lastEvaluated = false
            )
        }
        updatePreview()
    }

    fun onFactorial() {
        _uiState.update { current ->
            if (current.expression.isNotEmpty()) {
                current.copy(
                    expression = current.expression + "!",
                    errorMessage = null,
                    lastEvaluated = false
                )
            } else current
        }
        updatePreview()
    }

    fun onPercentage() {
        _uiState.update { current ->
            if (current.expression.isNotEmpty()) {
                current.copy(
                    expression = current.expression + "%",
                    errorMessage = null,
                    lastEvaluated = false
                )
            } else current
        }
        updatePreview()
    }

    fun onToggleSign() {
        _uiState.update { current ->
            val expr = current.expression
            if (expr.isEmpty()) {
                current.copy(expression = "-")
            } else if (expr.startsWith("-") && !expr.drop(1).contains(Regex("[+\\-×*÷/]"))) {
                current.copy(expression = expr.drop(1))
            } else if (!expr.contains(Regex("[+\\-×*÷/]"))) {
                current.copy(expression = "-$expr")
            } else {
                current.copy(expression = "-($expr)")
            }
        }
        updatePreview()
    }

    fun onClearAll() {
        _uiState.update {
            it.copy(
                expression = "",
                result = "0",
                previewResult = null,
                errorMessage = null,
                lastEvaluated = false
            )
        }
    }

    fun onDelete() {
        _uiState.update { current ->
            if (current.lastEvaluated) {
                current.copy(expression = "", lastEvaluated = false)
            } else if (current.expression.isNotEmpty()) {
                val expr = current.expression
                val newExpr = if (expr.endsWith("asin(") || expr.endsWith("acos(") || expr.endsWith("atan(") ||
                    expr.endsWith("sinh(") || expr.endsWith("cosh(") || expr.endsWith("tanh(") ||
                    expr.endsWith("sqrt(") || expr.endsWith("cbrt(") || expr.endsWith("npr(") || expr.endsWith("ncr(") ||
                    expr.endsWith("gcd(") || expr.endsWith("lcm(")
                ) {
                    expr.dropLast(5)
                } else if (expr.endsWith("sin(") || expr.endsWith("cos(") || expr.endsWith("tan(") ||
                    expr.endsWith("log(") || expr.endsWith("abs(") || expr.endsWith("exp(")
                ) {
                    expr.dropLast(4)
                } else if (expr.endsWith("ln(")) {
                    expr.dropLast(3)
                } else {
                    expr.dropLast(1)
                }
                current.copy(
                    expression = newExpr,
                    errorMessage = null
                )
            } else {
                current
            }
        }
        updatePreview()
    }

    fun onEquals() {
        val currentExpr = _uiState.value.expression
        if (currentExpr.isBlank()) return

        when (val calcResult = engine.evaluate(currentExpr)) {
            is CalculationResult.Success -> {
                _uiState.update {
                    it.copy(
                        result = calcResult.formatted,
                        previewResult = null,
                        errorMessage = null,
                        lastEvaluated = true
                    )
                }
                // Save to persistent history
                viewModelScope.launch {
                    repository.saveCalculation(
                        expression = currentExpr,
                        result = calcResult.formatted,
                        rawValue = calcResult.value,
                        angleMode = engine.angleMode.name
                    )
                }
            }
            is CalculationResult.Error -> {
                _uiState.update {
                    it.copy(
                        errorMessage = calcResult.message,
                        previewResult = null
                    )
                }
            }
        }
    }

    fun onToggleAngleMode() {
        val nextMode = _uiState.value.angleMode.toggle()
        settingsManager.setAngleMode(nextMode)
    }

    fun onToggleSecondFunction() {
        _uiState.update { it.copy(isSecondFunction = !it.isSecondFunction) }
    }

    fun onToggleScientificLayout() {
        _uiState.update { it.copy(isScientificExpanded = !it.isScientificExpanded) }
    }

    // Memory operations
    fun onMemoryClear() {
        engine.memoryClear()
        _uiState.update { it.copy(hasMemory = false, memoryValue = 0.0) }
    }

    fun onMemoryStore() {
        val currentVal = getCurrentNumericValue()
        if (currentVal != null) {
            engine.memoryStore(currentVal)
            _uiState.update { it.copy(hasMemory = true, memoryValue = currentVal) }
        }
    }

    fun onMemoryRecall() {
        if (engine.hasMemoryValue) {
            val memStr = MathFunctions.let {
                if (it.isInteger(engine.memoryValue)) engine.memoryValue.toLong().toString()
                else engine.memoryValue.toString()
            }
            _uiState.update { current ->
                val newExpr = if (current.lastEvaluated) memStr else current.expression + memStr
                current.copy(expression = newExpr, lastEvaluated = false)
            }
            updatePreview()
        }
    }

    fun onMemoryAdd() {
        val currentVal = getCurrentNumericValue()
        if (currentVal != null) {
            engine.memoryAdd(currentVal)
            _uiState.update { it.copy(hasMemory = true, memoryValue = engine.memoryValue) }
        }
    }

    fun onMemorySubtract() {
        val currentVal = getCurrentNumericValue()
        if (currentVal != null) {
            engine.memorySubtract(currentVal)
            _uiState.update { it.copy(hasMemory = true, memoryValue = engine.memoryValue) }
        }
    }

    private fun getCurrentNumericValue(): Double? {
        val current = _uiState.value
        // If there's an evaluated result, parse it or evaluate current expression
        return when (val res = engine.evaluate(if (current.expression.isNotEmpty()) current.expression else current.result)) {
            is CalculationResult.Success -> res.value
            is CalculationResult.Error -> null
        }
    }

    // History interactions
    fun onSelectHistoryItem(item: CalculationHistoryItem, useResultOnly: Boolean = false) {
        _uiState.update { current ->
            val expr = if (useResultOnly) item.result else item.expression
            current.copy(
                expression = expr,
                result = item.result,
                isHistoryOpen = false,
                lastEvaluated = false
            )
        }
        updatePreview()
    }

    fun onDeleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteHistoryItem(id)
        }
    }

    fun onClearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // Settings
    fun onUpdateSettings(newSettings: CalculatorSettings) {
        settingsManager.updateSettings(newSettings)
    }

    fun setHistoryOpen(open: Boolean) {
        _uiState.update { it.copy(isHistoryOpen = open) }
    }

    fun setSettingsOpen(open: Boolean) {
        _uiState.update { it.copy(isSettingsOpen = open) }
    }

    fun setGuideOpen(open: Boolean) {
        _uiState.update { it.copy(isGuideOpen = open) }
    }

    private fun updatePreview() {
        val expr = _uiState.value.expression
        if (expr.isNotBlank()) {
            val preview = engine.evaluatePreview(expr)
            _uiState.update { it.copy(previewResult = preview) }
        } else {
            _uiState.update { it.copy(previewResult = null) }
        }
    }

    class Factory(
        private val repository: CalculatorRepository,
        private val settingsManager: SettingsManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalculatorViewModel(repository, settingsManager) as T
        }
    }
}
