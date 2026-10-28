package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val expression: String,
    val result: String,
    val rawValue: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val angleMode: String = "DEG"
)
