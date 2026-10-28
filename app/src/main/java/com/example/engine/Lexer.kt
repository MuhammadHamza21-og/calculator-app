package com.example.engine

class Lexer(private val input: String) {
    private var pos = 0
    private val length = input.length

    fun tokenize(): List<Token> {
        val rawTokens = mutableListOf<Token>()

        while (pos < length) {
            val c = input[pos]
            when {
                c.isWhitespace() -> pos++
                c == '(' -> {
                    rawTokens.add(Token.OpenParen)
                    pos++
                }
                c == ')' -> {
                    rawTokens.add(Token.CloseParen)
                    pos++
                }
                c == ',' -> {
                    rawTokens.add(Token.Comma)
                    pos++
                }
                c == '!' -> {
                    rawTokens.add(Token.PostfixOp("!"))
                    pos++
                }
                c == '%' -> {
                    // Check if it's postfix percent or binary modulo
                    // In calculator standard: if followed by an operand or after a binary op,
                    // but universally % after a number or ) is % (divide by 100).
                    rawTokens.add(Token.PostfixOp("%"))
                    pos++
                }
                c == '^' -> {
                    rawTokens.add(Token.BinaryOp("^", precedence = 4, isRightAssociative = true))
                    pos++
                }
                c == '×' || c == '*' -> {
                    rawTokens.add(Token.BinaryOp("*", precedence = 2))
                    pos++
                }
                c == '÷' || c == '/' -> {
                    rawTokens.add(Token.BinaryOp("/", precedence = 2))
                    pos++
                }
                c == '+' -> {
                    val isUnary = isUnaryContext(rawTokens)
                    if (isUnary) {
                        rawTokens.add(Token.UnaryOp("+", precedence = 3))
                    } else {
                        rawTokens.add(Token.BinaryOp("+", precedence = 1))
                    }
                    pos++
                }
                c == '-' || c == '−' -> {
                    val isUnary = isUnaryContext(rawTokens)
                    if (isUnary) {
                        rawTokens.add(Token.UnaryOp("-", precedence = 3))
                    } else {
                        rawTokens.add(Token.BinaryOp("-", precedence = 1))
                    }
                    pos++
                }
                c == '√' -> {
                    rawTokens.add(Token.Function("sqrt", 1))
                    pos++
                }
                c == '∛' -> {
                    rawTokens.add(Token.Function("cbrt", 1))
                    pos++
                }
                c == 'π' -> {
                    rawTokens.add(Token.Constant("π", MathFunctions.PI))
                    pos++
                }
                c == '.' || c.isDigit() -> {
                    rawTokens.add(readNumber())
                }
                c.isLetter() -> {
                    val word = readWord()
                    when (word.lowercase()) {
                        "pi" -> rawTokens.add(Token.Constant("π", MathFunctions.PI))
                        "e" -> rawTokens.add(Token.Constant("e", MathFunctions.E))
                        "rand" -> rawTokens.add(Token.Number(Math.random(), "rand"))
                        "mod" -> rawTokens.add(Token.BinaryOp("mod", precedence = 2))
                        "npr" -> rawTokens.add(Token.Function("npr", 2))
                        "ncr" -> rawTokens.add(Token.Function("ncr", 2))
                        "gcd" -> rawTokens.add(Token.Function("gcd", 2))
                        "lcm" -> rawTokens.add(Token.Function("lcm", 2))
                        "sin" -> rawTokens.add(Token.Function("sin", 1))
                        "cos" -> rawTokens.add(Token.Function("cos", 1))
                        "tan" -> rawTokens.add(Token.Function("tan", 1))
                        "asin", "arcsin", "sin⁻¹" -> rawTokens.add(Token.Function("asin", 1))
                        "acos", "arccos", "cos⁻¹" -> rawTokens.add(Token.Function("acos", 1))
                        "atan", "arctan", "tan⁻¹" -> rawTokens.add(Token.Function("atan", 1))
                        "sinh" -> rawTokens.add(Token.Function("sinh", 1))
                        "cosh" -> rawTokens.add(Token.Function("cosh", 1))
                        "tanh" -> rawTokens.add(Token.Function("tanh", 1))
                        "asinh", "arcsinh" -> rawTokens.add(Token.Function("asinh", 1))
                        "acosh", "arccosh" -> rawTokens.add(Token.Function("acosh", 1))
                        "atanh", "arctanh" -> rawTokens.add(Token.Function("atanh", 1))
                        "sqrt" -> rawTokens.add(Token.Function("sqrt", 1))
                        "cbrt" -> rawTokens.add(Token.Function("cbrt", 1))
                        "ln" -> rawTokens.add(Token.Function("ln", 1))
                        "log", "log10" -> rawTokens.add(Token.Function("log", 1))
                        "log2" -> rawTokens.add(Token.Function("log2", 1))
                        "abs" -> rawTokens.add(Token.Function("abs", 1))
                        "floor" -> rawTokens.add(Token.Function("floor", 1))
                        "ceil" -> rawTokens.add(Token.Function("ceil", 1))
                        "exp" -> rawTokens.add(Token.Function("exp", 1))
                        else -> throw IllegalArgumentException("Unknown identifier: $word")
                    }
                }
                else -> throw IllegalArgumentException("Unexpected character '$c' at position $pos")
            }
        }

        return insertImplicitMultiplication(rawTokens)
    }

    private fun isUnaryContext(tokens: List<Token>): Boolean {
        if (tokens.isEmpty()) return true
        val last = tokens.last()
        return last is Token.BinaryOp ||
                last is Token.UnaryOp ||
                last is Token.OpenParen ||
                last is Token.Comma
    }

    private fun readNumber(): Token.Number {
        val start = pos
        var hasDot = false

        while (pos < length) {
            val ch = input[pos]
            if (ch == '.') {
                if (hasDot) break
                hasDot = true
                pos++
            } else if (ch.isDigit()) {
                pos++
            } else {
                break
            }
        }

        // Check for scientific notation like 1.2e3 or 1.2E-4
        if (pos < length && (input[pos] == 'e' || input[pos] == 'E')) {
            val nextPos = pos + 1
            if (nextPos < length) {
                val nextCh = input[nextPos]
                if (nextCh.isDigit() || ((nextCh == '+' || nextCh == '-') && nextPos + 1 < length && input[nextPos + 1].isDigit())) {
                    pos++ // consume 'e'
                    if (input[pos] == '+' || input[pos] == '-') pos++ // consume sign
                    while (pos < length && input[pos].isDigit()) {
                        pos++
                    }
                }
            }
        }

        val raw = input.substring(start, pos)
        val value = raw.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number: '$raw'")
        return Token.Number(value, raw)
    }

    private fun readWord(): String {
        val start = pos
        while (pos < length && (input[pos].isLetter() || input[pos].isDigit() || input[pos] == '⁻' || input[pos] == '¹')) {
            pos++
        }
        return input.substring(start, pos)
    }

    private fun insertImplicitMultiplication(tokens: List<Token>): List<Token> {
        if (tokens.size <= 1) return tokens
        val result = mutableListOf<Token>()

        for (i in tokens.indices) {
            val current = tokens[i]
            result.add(current)

            if (i < tokens.size - 1) {
                val next = tokens[i + 1]
                val currentCanMultiply = current is Token.Number ||
                        current is Token.Constant ||
                        current is Token.CloseParen ||
                        current is Token.PostfixOp

                val nextCanBeMultiplied = next is Token.Number ||
                        next is Token.Constant ||
                        next is Token.Function ||
                        next is Token.OpenParen

                if (currentCanMultiply && nextCanBeMultiplied) {
                    result.add(Token.BinaryOp("*", precedence = 2))
                }
            }
        }
        return result
    }
}
