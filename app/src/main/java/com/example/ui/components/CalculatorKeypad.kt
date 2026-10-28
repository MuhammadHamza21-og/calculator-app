package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorKeypad(
    isScientificExpanded: Boolean,
    isSecondFunction: Boolean,
    hapticEnabled: Boolean,
    onDigit: (String) -> Unit,
    onDecimal: () -> Unit,
    onOperator: (String) -> Unit,
    onFunction: (String) -> Unit,
    onConstant: (String) -> Unit,
    onParenthesis: (String) -> Unit,
    onFactorial: () -> Unit,
    onPercentage: () -> Unit,
    onToggleSign: () -> Unit,
    onClearAll: () -> Unit,
    onDelete: () -> Unit,
    onEquals: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Scientific keypad rows (collapsible)
        AnimatedVisibility(
            visible = isScientificExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Sci Row 1: Trig & Logs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val m = Modifier.weight(1f)
                    if (!isSecondFunction) {
                        CalculatorButton(text = "sin", onClick = { onFunction("sin") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "cos", onClick = { onFunction("cos") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "tan", onClick = { onFunction("tan") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "ln", onClick = { onFunction("ln") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "log", onClick = { onFunction("log") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                    } else {
                        CalculatorButton(text = "sin⁻¹", onClick = { onFunction("asin") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "cos⁻¹", onClick = { onFunction("acos") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "tan⁻¹", onClick = { onFunction("atan") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "eˣ", onClick = { onFunction("exp") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "10ˣ", onClick = { onOperator("^") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                    }
                }

                // Sci Row 2: Roots, Powers, Parens
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val m = Modifier.weight(1f)
                    if (!isSecondFunction) {
                        CalculatorButton(text = "√", onClick = { onFunction("sqrt") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 17.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "xʸ", onClick = { onOperator("^") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 16.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "(", onClick = { onParenthesis("(") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 17.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = ")", onClick = { onParenthesis(")") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 17.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "π", onClick = { onConstant("π") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 17.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                    } else {
                        CalculatorButton(text = "x²", onClick = { onOperator("^2") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 16.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "∛", onClick = { onFunction("cbrt") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 17.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "x³", onClick = { onOperator("^3") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 16.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "|x|", onClick = { onFunction("abs") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "1/x", onClick = { onFunction("1/") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                    }
                }

                // Sci Row 3: Combinatorics, Factorial, Hyperbolic & Advanced
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val m = Modifier.weight(1f)
                    if (!isSecondFunction) {
                        CalculatorButton(text = "x!", onClick = onFactorial, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 16.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "e", onClick = { onConstant("e") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 16.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "mod", onClick = { onOperator(" mod ") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "nCr", onClick = { onFunction("ncr") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "nPr", onClick = { onFunction("npr") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                    } else {
                        CalculatorButton(text = "sinh", onClick = { onFunction("sinh") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "cosh", onClick = { onFunction("cosh") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "tanh", onClick = { onFunction("tanh") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "gcd", onClick = { onFunction("gcd") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                        CalculatorButton(text = "rand", onClick = { onConstant("rand") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 13.sp, minHeight = 44.dp, hapticEnabled = hapticEnabled)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Standard Keypad: 5 Rows x 4 Columns
        // Row 1: AC, DEL, %, ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val m = Modifier.weight(1f)
            CalculatorButton(text = "AC", onClick = onClearAll, modifier = m, variant = ButtonVariant.ACTION, fontSize = 19.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_ac")
            CalculatorButton(text = "DEL", onClick = onDelete, modifier = m, variant = ButtonVariant.ACTION, fontSize = 18.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_del")
            CalculatorButton(text = "%", onClick = onPercentage, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 20.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_percent")
            CalculatorButton(text = "÷", onClick = { onOperator("÷") }, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 23.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_div")
        }

        // Row 2: 7, 8, 9, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val m = Modifier.weight(1f)
            CalculatorButton(text = "7", onClick = { onDigit("7") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = "8", onClick = { onDigit("8") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = "9", onClick = { onDigit("9") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = "×", onClick = { onOperator("×") }, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 23.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_mult")
        }

        // Row 3: 4, 5, 6, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val m = Modifier.weight(1f)
            CalculatorButton(text = "4", onClick = { onDigit("4") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = "5", onClick = { onDigit("5") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = "6", onClick = { onDigit("6") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = "−", onClick = { onOperator("−") }, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 24.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_sub")
        }

        // Row 4: 1, 2, 3, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val m = Modifier.weight(1f)
            CalculatorButton(text = "1", onClick = { onDigit("1") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = "2", onClick = { onDigit("2") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = "3", onClick = { onDigit("3") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = "+", onClick = { onOperator("+") }, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 23.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_add")
        }

        // Row 5: ±, 0, ., =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val m = Modifier.weight(1f)
            CalculatorButton(text = "±", onClick = onToggleSign, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 20.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_sign")
            CalculatorButton(text = "0", onClick = { onDigit("0") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled)
            CalculatorButton(text = ".", onClick = onDecimal, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 22.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_dot")
            CalculatorButton(text = "=", onClick = onEquals, modifier = m, variant = ButtonVariant.EQUALS, fontSize = 26.sp, minHeight = 52.dp, hapticEnabled = hapticEnabled, testTag = "btn_equals")
        }
    }
}
