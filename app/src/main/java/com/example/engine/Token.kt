package com.example.engine

sealed class Token {
    data class Number(val value: Double, val raw: String) : Token()
    data class Constant(val name: String, val value: Double) : Token()

    data class BinaryOp(
        val symbol: String,
        val precedence: Int,
        val isRightAssociative: Boolean = false
    ) : Token()

    data class UnaryOp(val symbol: String, val precedence: Int = 5) : Token()

    data class PostfixOp(val symbol: String, val precedence: Int = 6) : Token()

    data class Function(val name: String, val argCount: Int = 1) : Token()

    object OpenParen : Token()
    object CloseParen : Token()
    object Comma : Token()

    override fun toString(): String {
        return when (this) {
            is Number -> raw
            is Constant -> name
            is BinaryOp -> symbol
            is UnaryOp -> "u$symbol"
            is PostfixOp -> symbol
            is Function -> "$name("
            OpenParen -> "("
            CloseParen -> ")"
            Comma -> ","
        }
    }
}
