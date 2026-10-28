package com.example.engine

class CalculationEngine(
    var angleMode: AngleMode = AngleMode.DEG,
    var precision: Int = 10,
    var notationMode: NotationMode = NotationMode.AUTO,
    var useThousandsSeparator: Boolean = true
) {
    var memoryValue: Double = 0.0
        private set
    var hasMemoryValue: Boolean = false
        private set

    fun evaluate(expression: String): CalculationResult {
        if (expression.isBlank()) {
            return CalculationResult.Error("Enter an expression")
        }
        return try {
            val parser = ExpressionParser(angleMode)
            val result = parser.parseAndEvaluate(expression)
            val formatted = ScientificFormatter.format(
                value = result,
                precision = precision,
                notation = notationMode,
                useThousandsSeparator = useThousandsSeparator
            )
            val isInt = MathFunctions.isInteger(result)
            CalculationResult.Success(result, formatted, isInt)
        } catch (e: ArithmeticException) {
            CalculationResult.Error(e.message ?: "Math Error")
        } catch (e: IllegalArgumentException) {
            CalculationResult.Error(e.message ?: "Syntax Error")
        } catch (e: Exception) {
            CalculationResult.Error("Error: ${e.localizedMessage ?: "Invalid expression"}")
        }
    }

    /**
     * Attempts a tentative evaluation for live result preview while typing.
     * Silently returns null if the expression is incomplete or intermediate.
     */
    fun evaluatePreview(expression: String): String? {
        if (expression.isBlank()) return null
        // Avoid previewing single numbers or expressions ending in operators
        val trimmed = expression.trim()
        val lastChar = trimmed.last()
        if (lastChar in "+-×*÷/^,(" || trimmed.endsWith("mod", ignoreCase = true)) {
            return null
        }

        return try {
            // Balance unclosed open parens for preview purposes
            var openParens = 0
            for (c in trimmed) {
                if (c == '(') openParens++
                else if (c == ')') openParens--
            }
            val balanced = if (openParens > 0) {
                trimmed + ")".repeat(openParens)
            } else {
                trimmed
            }

            val parser = ExpressionParser(angleMode)
            val res = parser.parseAndEvaluate(balanced)
            ScientificFormatter.format(
                value = res,
                precision = minOf(precision, 8),
                notation = notationMode,
                useThousandsSeparator = useThousandsSeparator
            )
        } catch (_: Exception) {
            null
        }
    }

    fun memoryClear() {
        memoryValue = 0.0
        hasMemoryValue = false
    }

    fun memoryStore(value: Double) {
        memoryValue = value
        hasMemoryValue = true
    }

    fun memoryAdd(value: Double) {
        memoryValue += value
        hasMemoryValue = true
    }

    fun memorySubtract(value: Double) {
        memoryValue -= value
        hasMemoryValue = true
    }

    fun memoryRecall(): Double {
        return memoryValue
    }
}
