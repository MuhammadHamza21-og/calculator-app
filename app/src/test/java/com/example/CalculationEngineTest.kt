package com.example

import com.example.engine.AngleMode
import com.example.engine.CalculationEngine
import com.example.engine.CalculationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CalculationEngineTest {

    private lateinit var engineDeg: CalculationEngine
    private lateinit var engineRad: CalculationEngine

    @Before
    fun setUp() {
        engineDeg = CalculationEngine(angleMode = AngleMode.DEG)
        engineRad = CalculationEngine(angleMode = AngleMode.RAD)
    }

    private fun evalDeg(expr: String): Double {
        val res = engineDeg.evaluate(expr)
        assertTrue("Expected success for '$expr', got $res", res is CalculationResult.Success)
        return (res as CalculationResult.Success).value
    }

    private fun evalRad(expr: String): Double {
        val res = engineRad.evaluate(expr)
        assertTrue("Expected success for '$expr', got $res", res is CalculationResult.Success)
        return (res as CalculationResult.Success).value
    }

    @Test
    fun testBasicArithmetic() {
        // 2 + 3 = 5
        assertEquals(5.0, evalDeg("2 + 3"), 1e-9)

        // 10 − 4 = 6
        assertEquals(6.0, evalDeg("10 - 4"), 1e-9)
        assertEquals(6.0, evalDeg("10 − 4"), 1e-9)

        // 6 × 7 = 42
        assertEquals(42.0, evalDeg("6 * 7"), 1e-9)
        assertEquals(42.0, evalDeg("6 × 7"), 1e-9)

        // 20 ÷ 4 = 5
        assertEquals(5.0, evalDeg("20 / 4"), 1e-9)
        assertEquals(5.0, evalDeg("20 ÷ 4"), 1e-9)
    }

    @Test
    fun testRootsAndPowers() {
        // √81 = 9
        assertEquals(9.0, evalDeg("√81"), 1e-9)
        assertEquals(9.0, evalDeg("sqrt(81)"), 1e-9)

        // ∛27 = 3
        assertEquals(3.0, evalDeg("∛27"), 1e-9)
        assertEquals(3.0, evalDeg("cbrt(27)"), 1e-9)

        // 2³ = 8
        assertEquals(8.0, evalDeg("2^3"), 1e-9)

        // Reciprocal & Absolute
        assertEquals(0.25, evalDeg("1 / 4"), 1e-9)
        assertEquals(5.0, evalDeg("abs(-5)"), 1e-9)
    }

    @Test
    fun testLogarithms() {
        // log₁₀(100) = 2
        assertEquals(2.0, evalDeg("log(100)"), 1e-9)

        // ln(e) = 1
        assertEquals(1.0, evalDeg("ln(e)"), 1e-9)
    }

    @Test
    fun testTrigonometryDeg() {
        // sin(30°) = 0.5 in DEG mode
        assertEquals(0.5, evalDeg("sin(30)"), 1e-9)

        // cos(60°) = 0.5 in DEG mode
        assertEquals(0.5, evalDeg("cos(60)"), 1e-9)

        // tan(45°) = 1 in DEG mode
        assertEquals(1.0, evalDeg("tan(45)"), 1e-9)

        // Exact zeros in DEG mode
        assertEquals(0.0, evalDeg("sin(180)"), 1e-9)
        assertEquals(0.0, evalDeg("cos(90)"), 1e-9)
    }

    @Test
    fun testTrigonometryRad() {
        // sin(π / 2) = 1 in RAD mode
        assertEquals(1.0, evalRad("sin(π / 2)"), 1e-9)

        // cos(π) = -1 in RAD mode
        assertEquals(-1.0, evalRad("cos(π)"), 1e-9)
    }

    @Test
    fun testParenthesesAndPrecedence() {
        // (2 + 3) × 4 = 20
        assertEquals(20.0, evalDeg("(2 + 3) * 4"), 1e-9)
        assertEquals(20.0, evalDeg("(2 + 3) × 4"), 1e-9)

        // Operator precedence: 2 + 3 * 4 = 14
        assertEquals(14.0, evalDeg("2 + 3 * 4"), 1e-9)

        // Nested parentheses
        assertEquals(50.0, evalDeg("((2 + 3) * (4 + 6))"), 1e-9)
    }

    @Test
    fun testFactorial() {
        // 5! = 120
        assertEquals(120.0, evalDeg("5!"), 1e-9)

        // 0! = 1
        assertEquals(1.0, evalDeg("0!"), 1e-9)

        // Combined with expression: 5! + 4 = 124
        assertEquals(124.0, evalDeg("5! + 4"), 1e-9)
    }

    @Test
    fun testImplicitMultiplication() {
        // 2π ≈ 6.283185307
        assertEquals(2.0 * Math.PI, evalDeg("2π"), 1e-8)

        // 2(3 + 4) = 14
        assertEquals(14.0, evalDeg("2(3 + 4)"), 1e-9)

        // (2)(3) = 6
        assertEquals(6.0, evalDeg("(2)(3)"), 1e-9)

        // 4sin(30) = 2
        assertEquals(2.0, evalDeg("4sin(30)"), 1e-9)
    }

    @Test
    fun testCombinatoricsAndDiscrete() {
        // nPr(5, 2) = 20
        assertEquals(20.0, evalDeg("npr(5, 2)"), 1e-9)

        // nCr(5, 2) = 10
        assertEquals(10.0, evalDeg("ncr(5, 2)"), 1e-9)

        // GCD(12, 18) = 6
        assertEquals(6.0, evalDeg("gcd(12, 18)"), 1e-9)

        // LCM(4, 6) = 12
        assertEquals(12.0, evalDeg("lcm(4, 6)"), 1e-9)

        // 17 mod 5 = 2
        assertEquals(2.0, evalDeg("17 mod 5"), 1e-9)
    }

    @Test
    fun testDivisionByZeroHandled() {
        // 1 ÷ 0 must be handled correctly
        val res = engineDeg.evaluate("1 / 0")
        assertTrue("Expected error for 1/0, got $res", res is CalculationResult.Error)
        assertEquals("Cannot divide by zero", (res as CalculationResult.Error).message)
    }

    @Test
    fun testInvalidExpressionsHandled() {
        // Empty / Mismatched parentheses / invalid syntax
        assertTrue(engineDeg.evaluate("").isError)
        assertTrue(engineDeg.evaluate("(2 + 3").isError)
        assertTrue(engineDeg.evaluate("2 + * 3").isError)
        assertTrue(engineDeg.evaluate("sin()").isError)
    }

    @Test
    fun testNegativeValuesAndUnaryMinus() {
        assertEquals(-5.0, evalDeg("-5"), 1e-9)
        assertEquals(1.0, evalDeg("-5 + 6"), 1e-9)
        assertEquals(-20.0, evalDeg("4 * -5"), 1e-9)
        assertEquals(25.0, evalDeg("(-5)^2"), 1e-9)
    }

    @Test
    fun testPercentages() {
        // 50% = 0.5
        assertEquals(0.5, evalDeg("50%"), 1e-9)

        // 200 * 15% = 30
        assertEquals(30.0, evalDeg("200 * 15%"), 1e-9)
    }
}
