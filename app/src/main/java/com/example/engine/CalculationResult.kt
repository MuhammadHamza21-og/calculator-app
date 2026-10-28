package com.example.engine

sealed class CalculationResult {
    data class Success(
        val value: Double,
        val formatted: String,
        val isExactInteger: Boolean = false
    ) : CalculationResult()

    data class Error(val message: String) : CalculationResult()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
}
