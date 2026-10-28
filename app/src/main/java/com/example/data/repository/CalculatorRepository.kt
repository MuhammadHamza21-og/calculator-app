package com.example.data.repository

import com.example.data.local.HistoryDao
import com.example.data.model.CalculationHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculatorRepository(private val historyDao: HistoryDao) {

    fun getHistory(): Flow<List<CalculationHistoryItem>> {
        return historyDao.getAllHistory().map { list ->
            list.map { CalculationHistoryItem.fromEntity(it) }
        }
    }

    suspend fun saveCalculation(
        expression: String,
        result: String,
        rawValue: Double,
        angleMode: String
    ): Long {
        val item = CalculationHistoryItem(
            expression = expression,
            result = result,
            rawValue = rawValue,
            angleMode = angleMode
        )
        return historyDao.insert(item.toEntity())
    }

    suspend fun deleteHistoryItem(id: Long) {
        historyDao.deleteById(id)
    }

    suspend fun clearHistory() {
        historyDao.clearAll()
    }
}
