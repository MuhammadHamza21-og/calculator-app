package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.CalculatorDatabase
import com.example.data.repository.CalculatorRepository
import com.example.data.repository.SettingsManager
import com.example.ui.CalculatorScreen
import com.example.ui.CalculatorViewModel
import com.example.ui.theme.SciCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = CalculatorDatabase.getDatabase(applicationContext)
        val repository = CalculatorRepository(database.historyDao())
        val settingsManager = SettingsManager(applicationContext)
        val viewModelFactory = CalculatorViewModel.Factory(repository, settingsManager)

        setContent {
            val viewModel: CalculatorViewModel = viewModel(factory = viewModelFactory)
            val uiState by viewModel.uiState.collectAsState()

            SciCalcTheme(themeMode = uiState.settings.themeMode) {
                CalculatorScreen(viewModel = viewModel)
            }
        }
    }
}
