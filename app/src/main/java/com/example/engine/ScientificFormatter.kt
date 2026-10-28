package com.example.engine

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToLong

object ScientificFormatter {

    fun format(
        value: Double,
        precision: Int = 10,
        notation: NotationMode = NotationMode.AUTO,
        useThousandsSeparator: Boolean = true
    ): String {
        if (value.isNaN()) return "Error: NaN"
        if (value.isInfinite()) return if (value > 0) "Infinity" else "-Infinity"

        // Snap near-integers
        val roundedInt = value.roundToLong()
        if (abs(value - roundedInt) < 1e-11) {
            return formatInteger(roundedInt, useThousandsSeparator)
        }

        return when (notation) {
            NotationMode.SCIENTIFIC -> formatScientific(value, precision)
            NotationMode.ENGINEERING -> formatEngineering(value, precision)
            NotationMode.STANDARD -> formatStandard(value, precision, useThousandsSeparator)
            NotationMode.AUTO -> {
                val absVal = abs(value)
                if ((absVal >= 1e11 || (absVal in 1e-9..1e-5))) {
                    formatScientific(value, precision)
                } else {
                    formatStandard(value, precision, useThousandsSeparator)
                }
            }
        }
    }

    private fun formatInteger(value: Long, useThousandsSeparator: Boolean): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        symbols.groupingSeparator = ','
        val pattern = if (useThousandsSeparator) "#,##0" else "0"
        return DecimalFormat(pattern, symbols).format(value)
    }

    private fun formatStandard(value: Double, precision: Int, useThousandsSeparator: Boolean): String {
        try {
            val bd = BigDecimal.valueOf(value).setScale(precision, RoundingMode.HALF_UP).stripTrailingZeros()
            val plain = bd.toPlainString()

            val parts = plain.split(".")
            val intPart = parts[0].toLongOrNull()
            val formattedInt = if (intPart != null && useThousandsSeparator) {
                formatInteger(intPart, true)
            } else {
                parts[0]
            }

            return if (parts.size > 1 && parts[1].isNotEmpty()) {
                "$formattedInt.${parts[1]}"
            } else {
                formattedInt
            }
        } catch (_: Exception) {
            val symbols = DecimalFormatSymbols(Locale.US)
            symbols.groupingSeparator = ','
            val pattern = if (useThousandsSeparator) "#,##0.##########" else "0.##########"
            return DecimalFormat(pattern, symbols).format(value)
        }
    }

    private fun formatScientific(value: Double, precision: Int): String {
        if (value == 0.0) return "0"
        val sign = if (value < 0) "-" else ""
        val absVal = abs(value)
        val exp = floor(log10(absVal)).toInt()
        val mantissa = absVal / 10.0.pow(exp.toDouble())

        val bd = BigDecimal.valueOf(mantissa).setScale(minOf(precision, 8), RoundingMode.HALF_UP).stripTrailingZeros()
        val expSuperscript = toSuperscript(exp)
        return "$sign${bd.toPlainString()} × 10$expSuperscript"
    }

    private fun formatEngineering(value: Double, precision: Int): String {
        if (value == 0.0) return "0"
        val sign = if (value < 0) "-" else ""
        val absVal = abs(value)
        var exp = floor(log10(absVal)).toInt()
        // Engineering notation requires exponent to be multiple of 3
        val rem = ((exp % 3) + 3) % 3
        exp -= rem
        val mantissa = absVal / 10.0.pow(exp.toDouble())

        val bd = BigDecimal.valueOf(mantissa).setScale(minOf(precision, 8), RoundingMode.HALF_UP).stripTrailingZeros()
        val expSuperscript = toSuperscript(exp)
        return if (exp == 0) {
            "$sign${bd.toPlainString()}"
        } else {
            "$sign${bd.toPlainString()} × 10$expSuperscript"
        }
    }

    private fun toSuperscript(num: Int): String {
        val str = num.toString()
        val sb = StringBuilder()
        for (c in str) {
            val sup = when (c) {
                '-' -> '⁻'
                '0' -> '⁰'
                '1' -> '¹'
                '2' -> '²'
                '3' -> '³'
                '4' -> '⁴'
                '5' -> '⁵'
                '6' -> '⁶'
                '7' -> '⁷'
                '8' -> '⁸'
                '9' -> '⁹'
                else -> c
            }
            sb.append(sup)
        }
        return sb.toString()
    }
}
