package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AppThemeMode
import com.example.data.model.CalculatorSettings
import com.example.engine.AngleMode
import com.example.engine.NotationMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("scicalc_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<CalculatorSettings> = _settings.asStateFlow()

    private fun loadSettings(): CalculatorSettings {
        val angleModeStr = prefs.getString("angle_mode", AngleMode.DEG.name) ?: AngleMode.DEG.name
        val angleMode = try { AngleMode.valueOf(angleModeStr) } catch (_: Exception) { AngleMode.DEG }

        val precision = prefs.getInt("precision", 10)

        val notationStr = prefs.getString("notation_mode", NotationMode.AUTO.name) ?: NotationMode.AUTO.name
        val notationMode = try { NotationMode.valueOf(notationStr) } catch (_: Exception) { NotationMode.AUTO }

        val thousands = prefs.getBoolean("use_thousands_separator", true)
        val haptic = prefs.getBoolean("haptic_feedback", true)

        val themeStr = prefs.getString("theme_mode", AppThemeMode.DARK.name) ?: AppThemeMode.DARK.name
        val themeMode = try { AppThemeMode.valueOf(themeStr) } catch (_: Exception) { AppThemeMode.DARK }

        return CalculatorSettings(
            angleMode = angleMode,
            precision = precision,
            notationMode = notationMode,
            useThousandsSeparator = thousands,
            hapticFeedback = haptic,
            themeMode = themeMode
        )
    }

    fun updateSettings(newSettings: CalculatorSettings) {
        prefs.edit()
            .putString("angle_mode", newSettings.angleMode.name)
            .putInt("precision", newSettings.precision)
            .putString("notation_mode", newSettings.notationMode.name)
            .putBoolean("use_thousands_separator", newSettings.useThousandsSeparator)
            .putBoolean("haptic_feedback", newSettings.hapticFeedback)
            .putString("theme_mode", newSettings.themeMode.name)
            .apply()
        _settings.value = newSettings
    }

    fun setAngleMode(mode: AngleMode) {
        updateSettings(_settings.value.copy(angleMode = mode))
    }

    fun setThemeMode(mode: AppThemeMode) {
        updateSettings(_settings.value.copy(themeMode = mode))
    }
}
