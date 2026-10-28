package com.example.engine

class ExpressionParser(private val angleMode: AngleMode = AngleMode.DEG) {

    fun parseAndEvaluate(expression: String): Double {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) {
            throw IllegalArgumentException("Empty expression")
        }

        val lexer = Lexer(trimmed)
        val tokens = lexer.tokenize()
        if (tokens.isEmpty()) {
            throw IllegalArgumentException("No tokens found")
        }

        val rpn = toRpn(tokens)
        return evaluateRpn(rpn)
    }

    private fun toRpn(tokens: List<Token>): List<Token> {
        val output = mutableListOf<Token>()
        val opStack = ArrayDeque<Token>()

        for (token in tokens) {
            when (token) {
                is Token.Number -> output.add(token)
                is Token.Constant -> output.add(token)
                is Token.PostfixOp -> {
                    // Postfix operators have immediate precedence on preceding value
                    output.add(token)
                }
                is Token.Function -> {
                    opStack.addLast(token)
                }
                is Token.Comma -> {
                    while (opStack.isNotEmpty() && opStack.last() !is Token.OpenParen) {
                        output.add(opStack.removeLast())
                    }
                    if (opStack.isEmpty()) {
                        throw IllegalArgumentException("Misplaced comma or missing '('")
                    }
                }
                is Token.UnaryOp -> {
                    opStack.addLast(token)
                }
                is Token.BinaryOp -> {
                    while (opStack.isNotEmpty()) {
                        val top = opStack.last()
                        if (top is Token.BinaryOp) {
                            val shouldPop = if (token.isRightAssociative) {
                                token.precedence < top.precedence
                            } else {
                                token.precedence <= top.precedence
                            }
                            if (shouldPop) {
                                output.add(opStack.removeLast())
                            } else {
                                break
                            }
                        } else if (top is Token.UnaryOp) {
                            if (token.precedence <= top.precedence) {
                                output.add(opStack.removeLast())
                            } else {
                                break
                            }
                        } else {
                            break
                        }
                    }
                    opStack.addLast(token)
                }
                is Token.OpenParen -> {
                    opStack.addLast(token)
                }
                is Token.CloseParen -> {
                    var foundOpen = false
                    while (opStack.isNotEmpty()) {
                        val top = opStack.removeLast()
                        if (top is Token.OpenParen) {
                            foundOpen = true
                            break
                        } else {
                            output.add(top)
                        }
                    }
                    if (!foundOpen) {
                        throw IllegalArgumentException("Mismatched parentheses: extra ')'")
                    }
                    if (opStack.isNotEmpty() && opStack.last() is Token.Function) {
                        output.add(opStack.removeLast())
                    }
                }
            }
        }

        while (opStack.isNotEmpty()) {
            val top = opStack.removeLast()
            if (top is Token.OpenParen || top is Token.CloseParen) {
                throw IllegalArgumentException("Mismatched parentheses: unclosed '('")
            }
            output.add(top)
        }

        return output
    }

    private fun evaluateRpn(rpn: List<Token>): Double {
        val stack = ArrayDeque<Double>()

        for (token in rpn) {
            when (token) {
                is Token.Number -> stack.addLast(token.value)
                is Token.Constant -> stack.addLast(token.value)
                is Token.UnaryOp -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing operand for unary '${token.symbol}'")
                    val a = stack.removeLast()
                    val res = when (token.symbol) {
                        "-" -> -a
                        "+" -> a
                        else -> throw IllegalArgumentException("Unknown unary operator: ${token.symbol}")
                    }
                    stack.addLast(res)
                }
                is Token.PostfixOp -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing operand for '${token.symbol}'")
                    val a = stack.removeLast()
                    val res = when (token.symbol) {
                        "!" -> MathFunctions.factorial(a)
                        "%" -> a / 100.0
                        else -> throw IllegalArgumentException("Unknown postfix operator: ${token.symbol}")
                    }
                    stack.addLast(res)
                }
                is Token.BinaryOp -> {
                    if (stack.size < 2) throw IllegalArgumentException("Missing operand for '${token.symbol}'")
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    val res = when (token.symbol) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> {
                            if (MathFunctions.isCloseTo(b, 0.0)) {
                                throw ArithmeticException("Cannot divide by zero")
                            }
                            a / b
                        }
                        "^" -> MathFunctions.pow(a, b)
                        "mod" -> {
                            if (MathFunctions.isCloseTo(b, 0.0)) {
                                throw ArithmeticException("Cannot divide by zero in modulo")
                            }
                            a % b
                        }
                        else -> throw IllegalArgumentException("Unknown binary operator: ${token.symbol}")
                    }
                    stack.addLast(res)
                }
                is Token.Function -> {
                    if (token.argCount == 1) {
                        if (stack.isEmpty()) throw IllegalArgumentException("Missing argument for function '${token.name}'")
                        val a = stack.removeLast()
                        val res = when (token.name.lowercase()) {
                            "sin" -> MathFunctions.sin(a, angleMode)
                            "cos" -> MathFunctions.cos(a, angleMode)
                            "tan" -> MathFunctions.tan(a, angleMode)
                            "asin" -> MathFunctions.asin(a, angleMode)
                            "acos" -> MathFunctions.acos(a, angleMode)
                            "atan" -> MathFunctions.atan(a, angleMode)
                            "sinh" -> MathFunctions.sinh(a)
                            "cosh" -> MathFunctions.cosh(a)
                            "tanh" -> MathFunctions.tanh(a)
                            "asinh" -> MathFunctions.asinh(a)
                            "acosh" -> MathFunctions.acosh(a)
                            "atanh" -> MathFunctions.atanh(a)
                            "sqrt" -> MathFunctions.sqrt(a)
                            "cbrt" -> MathFunctions.cbrt(a)
                            "ln" -> MathFunctions.ln(a)
                            "log" -> MathFunctions.log10(a)
                            "log2" -> MathFunctions.log2(a)
                            "abs" -> kotlin.math.abs(a)
                            "floor" -> kotlin.math.floor(a)
                            "ceil" -> kotlin.math.ceil(a)
                            "exp" -> kotlin.math.exp(a)
                            else -> throw IllegalArgumentException("Unknown function: ${token.name}")
                        }
                        stack.addLast(res)
                    } else if (token.argCount == 2) {
                        if (stack.size < 2) throw IllegalArgumentException("Missing arguments for function '${token.name}'")
                        val b = stack.removeLast()
                        val a = stack.removeLast()
                        val res = when (token.name.lowercase()) {
                            "npr" -> MathFunctions.permutations(a, b)
                            "ncr" -> MathFunctions.combinations(a, b)
                            "gcd" -> MathFunctions.gcd(a, b)
                            "lcm" -> MathFunctions.lcm(a, b)
                            else -> throw IllegalArgumentException("Unknown 2-argument function: ${token.name}")
                        }
                        stack.addLast(res)
                    }
                }
                Token.OpenParen, Token.CloseParen, Token.Comma -> {
                    throw IllegalStateException("Unexpected token in RPN evaluation: $token")
                }
            }
        }

        if (stack.size != 1) {
            throw IllegalArgumentException("Invalid expression syntax")
        }

        val finalValue = stack.removeLast()
        if (finalValue.isNaN()) {
            throw ArithmeticException("Result is Undefined (NaN)")
        }
        if (finalValue.isInfinite()) {
            throw ArithmeticException(if (finalValue > 0) "Infinity (Overflow)" else "-Infinity (Underflow)")
        }

        return finalValue
    }
}
