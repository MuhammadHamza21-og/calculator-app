package com.example.engine

import kotlin.math.*

object MathFunctions {
    const val PI = Math.PI
    const val E = Math.E
    private const val EPSILON = 1e-12

    fun sin(x: Double, mode: AngleMode): Double {
        return if (mode == AngleMode.DEG) {
            val normalized = normalizeDegrees(x)
            when {
                isCloseTo(normalized, 0.0) || isCloseTo(normalized, 180.0) || isCloseTo(normalized, 360.0) -> 0.0
                isCloseTo(normalized, 30.0) || isCloseTo(normalized, 150.0) -> 0.5
                isCloseTo(normalized, 90.0) -> 1.0
                isCloseTo(normalized, 210.0) || isCloseTo(normalized, 330.0) -> -0.5
                isCloseTo(normalized, 270.0) -> -1.0
                else -> kotlin.math.sin(Math.toRadians(x))
            }
        } else {
            val result = kotlin.math.sin(x)
            if (abs(result) < EPSILON) 0.0 else result
        }
    }

    fun cos(x: Double, mode: AngleMode): Double {
        return if (mode == AngleMode.DEG) {
            val normalized = normalizeDegrees(x)
            when {
                isCloseTo(normalized, 90.0) || isCloseTo(normalized, 270.0) -> 0.0
                isCloseTo(normalized, 0.0) || isCloseTo(normalized, 360.0) -> 1.0
                isCloseTo(normalized, 60.0) || isCloseTo(normalized, 300.0) -> 0.5
                isCloseTo(normalized, 120.0) || isCloseTo(normalized, 240.0) -> -0.5
                isCloseTo(normalized, 180.0) -> -1.0
                else -> kotlin.math.cos(Math.toRadians(x))
            }
        } else {
            val result = kotlin.math.cos(x)
            if (abs(result) < EPSILON) 0.0 else result
        }
    }

    fun tan(x: Double, mode: AngleMode): Double {
        return if (mode == AngleMode.DEG) {
            val normalized = normalizeDegrees(x)
            if (isCloseTo(normalized, 90.0) || isCloseTo(normalized, 270.0)) {
                throw ArithmeticException("Undefined: tan($x°)")
            }
            when {
                isCloseTo(normalized, 0.0) || isCloseTo(normalized, 180.0) || isCloseTo(normalized, 360.0) -> 0.0
                isCloseTo(normalized, 45.0) || isCloseTo(normalized, 225.0) -> 1.0
                isCloseTo(normalized, 135.0) || isCloseTo(normalized, 315.0) -> -1.0
                else -> kotlin.math.tan(Math.toRadians(x))
            }
        } else {
            val modPi = (x % PI + PI) % PI
            if (isCloseTo(modPi, PI / 2)) {
                throw ArithmeticException("Undefined: tan($x rad)")
            }
            val result = kotlin.math.tan(x)
            if (abs(result) < EPSILON) 0.0 else result
        }
    }

    fun asin(x: Double, mode: AngleMode): Double {
        if (x < -1.0 - EPSILON || x > 1.0 + EPSILON) {
            throw ArithmeticException("Domain error: asin($x), input must be between -1 and 1")
        }
        val clamped = x.coerceIn(-1.0, 1.0)
        val rad = kotlin.math.asin(clamped)
        return if (mode == AngleMode.DEG) Math.toDegrees(rad) else rad
    }

    fun acos(x: Double, mode: AngleMode): Double {
        if (x < -1.0 - EPSILON || x > 1.0 + EPSILON) {
            throw ArithmeticException("Domain error: acos($x), input must be between -1 and 1")
        }
        val clamped = x.coerceIn(-1.0, 1.0)
        val rad = kotlin.math.acos(clamped)
        return if (mode == AngleMode.DEG) Math.toDegrees(rad) else rad
    }

    fun atan(x: Double, mode: AngleMode): Double {
        val rad = kotlin.math.atan(x)
        return if (mode == AngleMode.DEG) Math.toDegrees(rad) else rad
    }

    fun sinh(x: Double): Double = kotlin.math.sinh(x)
    fun cosh(x: Double): Double = kotlin.math.cosh(x)
    fun tanh(x: Double): Double = kotlin.math.tanh(x)

    fun asinh(x: Double): Double = ln(x + sqrt(x * x + 1.0))

    fun acosh(x: Double): Double {
        if (x < 1.0) {
            throw ArithmeticException("Domain error: acosh($x), input must be ≥ 1")
        }
        return ln(x + sqrt(x * x - 1.0))
    }

    fun atanh(x: Double): Double {
        if (x <= -1.0 || x >= 1.0) {
            throw ArithmeticException("Domain error: atanh($x), input must be in (-1, 1)")
        }
        return 0.5 * ln((1.0 + x) / (1.0 - x))
    }

    fun sqrt(x: Double): Double {
        if (x < 0.0) throw ArithmeticException("Domain error: square root of negative number ($x)")
        return kotlin.math.sqrt(x)
    }

    fun cbrt(x: Double): Double = kotlin.math.cbrt(x)

    fun ln(x: Double): Double {
        if (x <= 0.0) throw ArithmeticException("Domain error: ln($x), input must be > 0")
        return kotlin.math.ln(x)
    }

    fun log10(x: Double): Double {
        if (x <= 0.0) throw ArithmeticException("Domain error: log₁₀($x), input must be > 0")
        return kotlin.math.log10(x)
    }

    fun log2(x: Double): Double {
        if (x <= 0.0) throw ArithmeticException("Domain error: log₂($x), input must be > 0")
        return kotlin.math.log2(x)
    }

    fun logBase(base: Double, x: Double): Double {
        if (base <= 0.0 || isCloseTo(base, 1.0)) {
            throw ArithmeticException("Domain error: log base must be > 0 and ≠ 1")
        }
        if (x <= 0.0) throw ArithmeticException("Domain error: log($base, $x), input must be > 0")
        return kotlin.math.ln(x) / kotlin.math.ln(base)
    }

    fun pow(base: Double, exponent: Double): Double {
        if (isCloseTo(base, 0.0)) {
            if (exponent < 0.0) throw ArithmeticException("Cannot divide by zero (0^$exponent)")
            if (isCloseTo(exponent, 0.0)) return 1.0
            return 0.0
        }
        if (base < 0.0) {
            if (isInteger(exponent)) {
                val sign = if (exponent.toLong() % 2L == 0L) 1.0 else -1.0
                return sign * (-base).pow(exponent)
            } else {
                throw ArithmeticException("Domain error: negative base with non-integer exponent")
            }
        }
        return base.pow(exponent)
    }

    fun factorial(n: Double): Double {
        if (!isInteger(n) || n < 0.0) {
            throw ArithmeticException("Domain error: factorial requires non-negative integer (got $n)")
        }
        val intN = n.toLong()
        if (intN > 170) {
            throw ArithmeticException("Overflow: $intN! exceeds calculator capacity")
        }
        var result = 1.0
        for (i in 2..intN) {
            result *= i
        }
        return result
    }

    fun permutations(n: Double, r: Double): Double {
        if (!isInteger(n) || !isInteger(r) || n < 0 || r < 0 || r > n) {
            throw ArithmeticException("Invalid arguments for nPr($n, $r): require n ≥ r ≥ 0 integers")
        }
        val intN = n.toLong()
        val intR = r.toLong()
        var result = 1.0
        for (i in 0 until intR) {
            result *= (intN - i)
        }
        return result
    }

    fun combinations(n: Double, r: Double): Double {
        if (!isInteger(n) || !isInteger(r) || n < 0 || r < 0 || r > n) {
            throw ArithmeticException("Invalid arguments for nCr($n, $r): require n ≥ r ≥ 0 integers")
        }
        val intN = n.toLong()
        var intR = r.toLong()
        if (intR > intN - intR) {
            intR = intN - intR
        }
        var result = 1.0
        for (i in 1..intR) {
            result = result * (intN - i + 1) / i
        }
        return result
    }

    fun gcd(a: Double, b: Double): Double {
        if (!isInteger(a) || !isInteger(b)) {
            throw ArithmeticException("GCD requires integer arguments")
        }
        var x = abs(a.toLong())
        var y = abs(b.toLong())
        while (y != 0L) {
            val t = y
            y = x % y
            x = t
        }
        return x.toDouble()
    }

    fun lcm(a: Double, b: Double): Double {
        if (!isInteger(a) || !isInteger(b)) {
            throw ArithmeticException("LCM requires integer arguments")
        }
        val x = abs(a.toLong())
        val y = abs(b.toLong())
        if (x == 0L || y == 0L) return 0.0
        val g = gcd(a, b).toLong()
        return (x / g * y).toDouble()
    }

    private fun normalizeDegrees(deg: Double): Double {
        var norm = deg % 360.0
        if (norm < 0.0) norm += 360.0
        return norm
    }

    fun isInteger(v: Double): Boolean {
        return abs(v - round(v)) < EPSILON
    }

    fun isCloseTo(a: Double, b: Double, eps: Double = 1e-9): Boolean {
        return abs(a - b) < eps
    }
}
