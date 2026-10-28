package com.example.data.model

import com.example.data.local.HistoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CalculationHistoryItem(
    val id: Long = 0,
    val expression: String,
    val result: String,
    val rawValue: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val angleMode: String = "DEG"
) {
    val formattedTime: String
        get() {
            val sdf = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

    fun toEntity(): HistoryEntity = HistoryEntity(
        id = id,
        expression = expression,
        result = result,
        rawValue = rawValue,
        timestamp = timestamp,
        angleMode = angleMode
    )

    companion object {
        fun fromEntity(entity: HistoryEntity): CalculationHistoryItem = CalculationHistoryItem(
            id = entity.id,
            expression = entity.expression,
            result = entity.result,
            rawValue = entity.rawValue,
            timestamp = entity.timestamp,
            angleMode = entity.angleMode
        )
    }
}
