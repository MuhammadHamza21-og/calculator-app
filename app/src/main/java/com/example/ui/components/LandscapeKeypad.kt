package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LandscapeKeypad(
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Left Column: Scientific Functions (5 rows x 5 columns)
        Column(
            modifier = Modifier.weight(1.1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Row 1
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                val h = 40.dp
                CalculatorButton(text = if (!isSecondFunction) "sin" else "sin⁻¹", onClick = { onFunction(if (!isSecondFunction) "sin" else "asin") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 13.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = if (!isSecondFunction) "cos" else "cos⁻¹", onClick = { onFunction(if (!isSecondFunction) "cos" else "acos") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 13.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = if (!isSecondFunction) "tan" else "tan⁻¹", onClick = { onFunction(if (!isSecondFunction) "tan" else "atan") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 13.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "π", onClick = { onConstant("π") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "e", onClick = { onConstant("e") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
            // Row 2
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                val h = 40.dp
                CalculatorButton(text = if (!isSecondFunction) "ln" else "eˣ", onClick = { onFunction(if (!isSecondFunction) "ln" else "exp") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 13.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = if (!isSecondFunction) "log" else "10ˣ", onClick = { if (!isSecondFunction) onFunction("log") else onOperator("^") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 13.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "xʸ", onClick = { onOperator("^") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "√", onClick = { onFunction("sqrt") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "∛", onClick = { onFunction("cbrt") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
            // Row 3
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                val h = 40.dp
                CalculatorButton(text = "sinh", onClick = { onFunction("sinh") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "cosh", onClick = { onFunction("cosh") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "tanh", onClick = { onFunction("tanh") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "(", onClick = { onParenthesis("(") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = ")", onClick = { onParenthesis(")") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 15.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
            // Row 4
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                val h = 40.dp
                CalculatorButton(text = "x!", onClick = onFactorial, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 13.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "1/x", onClick = { onFunction("1/") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "|x|", onClick = { onFunction("abs") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 13.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "mod", onClick = { onOperator(" mod ") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "rand", onClick = { onConstant("rand") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
            // Row 5
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                val h = 40.dp
                CalculatorButton(text = "nCr", onClick = { onFunction("ncr") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "nPr", onClick = { onFunction("npr") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "gcd", onClick = { onFunction("gcd") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "lcm", onClick = { onFunction("lcm") }, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 12.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "%", onClick = onPercentage, modifier = m, variant = ButtonVariant.SCIENTIFIC, fontSize = 14.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
        }

        // Right Column: Numeric & Basic Operators (5 rows x 4 columns)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val h = 40.dp
            // Row 1
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                CalculatorButton(text = "AC", onClick = onClearAll, modifier = m, variant = ButtonVariant.ACTION, fontSize = 14.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "DEL", onClick = onDelete, modifier = m, variant = ButtonVariant.ACTION, fontSize = 14.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "±", onClick = onToggleSign, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 15.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "÷", onClick = { onOperator("÷") }, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 18.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
            // Row 2
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                CalculatorButton(text = "7", onClick = { onDigit("7") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "8", onClick = { onDigit("8") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "9", onClick = { onDigit("9") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "×", onClick = { onOperator("×") }, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 18.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
            // Row 3
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                CalculatorButton(text = "4", onClick = { onDigit("4") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "5", onClick = { onDigit("5") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "6", onClick = { onDigit("6") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "−", onClick = { onOperator("−") }, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 18.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
            // Row 4
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                CalculatorButton(text = "1", onClick = { onDigit("1") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "2", onClick = { onDigit("2") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "3", onClick = { onDigit("3") }, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "+", onClick = { onOperator("+") }, modifier = m, variant = ButtonVariant.OPERATOR, fontSize = 18.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
            // Row 5
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val m = Modifier.weight(1f)
                CalculatorButton(text = "0", onClick = { onDigit("0") }, modifier = Modifier.weight(2f), variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = ".", onClick = onDecimal, modifier = m, variant = ButtonVariant.NUMBER, fontSize = 17.sp, minHeight = h, hapticEnabled = hapticEnabled)
                CalculatorButton(text = "=", onClick = onEquals, modifier = m, variant = ButtonVariant.EQUALS, fontSize = 20.sp, minHeight = h, hapticEnabled = hapticEnabled)
            }
        }
    }
}
