package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ButtonVariant {
    NUMBER,
    OPERATOR,
    SCIENTIFIC,
    ACTION,
    EQUALS,
    ACCENT
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.NUMBER,
    subText: String? = null,
    fontSize: TextUnit = 20.sp,
    minHeight: Dp = 48.dp,
    testTag: String = "btn_$text",
    hapticEnabled: Boolean = true
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "btn_scale"
    )

    val (bgColor, textColor, gradient) = getButtonColors(variant)

    Box(
        modifier = modifier
            .testTag(testTag)
            .scale(scale)
            .defaultMinSize(minWidth = 48.dp, minHeight = minHeight)
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (gradient != null) {
                    Modifier.background(gradient)
                } else {
                    Modifier.background(bgColor)
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if (hapticEnabled) {
                    performHapticFeedback(context)
                }
                onClick()
            }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = if (variant == ButtonVariant.EQUALS || variant == ButtonVariant.OPERATOR) {
                    FontWeight.Bold
                } else {
                    FontWeight.SemiBold
                },
                textAlign = TextAlign.Center
            )
            if (subText != null) {
                Text(
                    text = subText,
                    color = textColor.copy(alpha = 0.6f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun getButtonColors(variant: ButtonVariant): Triple<Color, Color, Brush?> {
    val cs = MaterialTheme.colorScheme
    return when (variant) {
        ButtonVariant.NUMBER -> Triple(
            cs.surfaceVariant,
            cs.onSurface,
            null
        )
        ButtonVariant.OPERATOR -> Triple(
            cs.primaryContainer,
            cs.onPrimaryContainer,
            null
        )
        ButtonVariant.SCIENTIFIC -> Triple(
            cs.secondaryContainer,
            cs.onSecondaryContainer,
            null
        )
        ButtonVariant.ACTION -> Triple(
            cs.error.copy(alpha = 0.18f),
            cs.error,
            null
        )
        ButtonVariant.EQUALS -> Triple(
            cs.primary,
            cs.onPrimary,
            Brush.linearGradient(
                colors = listOf(Color(0xFF0284C7), Color(0xFF00D2FF))
            )
        )
        ButtonVariant.ACCENT -> Triple(
            cs.tertiary.copy(alpha = 0.2f),
            cs.tertiary,
            null
        )
    }
}

private fun performHapticFeedback(context: Context) {
    try {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(20)
            }
        }
    } catch (_: Exception) {
        // Ignored if permissions or hardware missing
    }
}
