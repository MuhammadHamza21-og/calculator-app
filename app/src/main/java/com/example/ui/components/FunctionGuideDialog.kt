package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionGuideDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Function Guide & Cheat-Sheet",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    GuideCategoryCard(
                        category = "Trigonometry & Inverse",
                        items = listOf(
                            Triple("sin(x), cos(x), tan(x)", "Trigonometric functions. Uses DEG or RAD mode.", "sin(30) = 0.5 (DEG)"),
                            Triple("sin⁻¹(x), cos⁻¹(x), tan⁻¹(x)", "Inverse trig functions (asin, acos, atan).", "asin(0.5) = 30°"),
                            Triple("DEG / RAD Mode", "Tap the DEG/RAD badge on top to toggle angle modes.", "cos(π) = -1 (RAD)")
                        )
                    )
                }

                item {
                    GuideCategoryCard(
                        category = "Hyperbolic Functions",
                        items = listOf(
                            Triple("sinh(x), cosh(x), tanh(x)", "Hyperbolic sine, cosine, tangent.", "sinh(0) = 0"),
                            Triple("asinh(x), acosh(x), atanh(x)", "Inverse hyperbolic functions.", "acosh(1) = 0")
                        )
                    )
                }

                item {
                    GuideCategoryCard(
                        category = "Powers, Roots & Logarithms",
                        items = listOf(
                            Triple("xʸ (or ^)", "Power / exponentiation.", "2^3 = 8, 2^3^2 = 512"),
                            Triple("√ (sqrt), ∛ (cbrt)", "Square and cube roots.", "√81 = 9, ∛27 = 3"),
                            Triple("eˣ (exp)", "Natural exponential function.", "e^1 ≈ 2.71828"),
                            Triple("ln(x)", "Natural logarithm (base e).", "ln(e) = 1"),
                            Triple("log(x)", "Common logarithm (base 10).", "log(100) = 2"),
                            Triple("1/x", "Reciprocal of x.", "1/4 = 0.25"),
                            Triple("|x|", "Absolute value.", "|-5| = 5")
                        )
                    )
                }

                item {
                    GuideCategoryCard(
                        category = "Combinatorics & Discrete Math",
                        items = listOf(
                            Triple("x! (Factorial)", "Factorial of non-negative integer n ≤ 170.", "5! = 120"),
                            Triple("nPr(n, r)", "Permutations: n! / (n - r)!", "npr(5, 2) = 20"),
                            Triple("nCr(n, r)", "Combinations: n! / (r!(n - r)!)", "ncr(5, 2) = 10"),
                            Triple("gcd(a, b)", "Greatest Common Divisor of two integers.", "gcd(12, 18) = 6"),
                            Triple("lcm(a, b)", "Least Common Multiple of two integers.", "lcm(4, 6) = 12"),
                            Triple("mod", "Modulo remainder operation.", "17 mod 5 = 2"),
                            Triple("rand", "Generates pseudo-random number in [0, 1).", "rand × 100")
                        )
                    )
                }

                item {
                    GuideCategoryCard(
                        category = "Constants & Syntax",
                        items = listOf(
                            Triple("π (pi)", "Archimedes' constant ≈ 3.1415926535...", "2π = 6.283185..."),
                            Triple("e", "Euler's number ≈ 2.718281828...", "e^2 ≈ 7.389"),
                            Triple("Implicit Multiplication", "Supported: 2π, 2(3+4), 5sin(30).", "2(4) = 8"),
                            Triple("2ⁿᵈ Button", "Swaps keys to alternate scientific functions.", "sin ⇄ sin⁻¹, √ ⇄ x²")
                        )
                    )
                }

                item {
                    GuideCategoryCard(
                        category = "Memory Operations",
                        items = listOf(
                            Triple("MS (Memory Store)", "Stores current result or expression into memory.", "MS"),
                            Triple("MR (Memory Recall)", "Recalls stored memory value into expression.", "MR"),
                            Triple("M+ / M−", "Adds to or subtracts from stored memory.", "M+, M−"),
                            Triple("MC (Memory Clear)", "Clears the memory register.", "MC")
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideCategoryCard(
    category: String,
    items: List<Triple<String, String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = category,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            items.forEachIndexed { index, (name, desc, example) ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = name,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = example,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = desc,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
