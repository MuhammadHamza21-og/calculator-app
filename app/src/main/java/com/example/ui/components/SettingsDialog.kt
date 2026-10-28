package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppThemeMode
import com.example.data.model.CalculatorSettings
import com.example.engine.AngleMode
import com.example.engine.NotationMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    isOpen: Boolean,
    settings: CalculatorSettings,
    onDismiss: () -> Unit,
    onUpdateSettings: (CalculatorSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(scrollState)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Calculator Settings",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Appearance Theme Section
            SettingsSection(title = "Appearance & Theme") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeOptionChip(
                        title = "Dark",
                        isSelected = settings.themeMode == AppThemeMode.DARK,
                        icon = Icons.Default.DarkMode,
                        onClick = { onUpdateSettings(settings.copy(themeMode = AppThemeMode.DARK)) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOptionChip(
                        title = "Light",
                        isSelected = settings.themeMode == AppThemeMode.LIGHT,
                        icon = Icons.Default.LightMode,
                        onClick = { onUpdateSettings(settings.copy(themeMode = AppThemeMode.LIGHT)) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOptionChip(
                        title = "System",
                        isSelected = settings.themeMode == AppThemeMode.SYSTEM,
                        icon = null,
                        onClick = { onUpdateSettings(settings.copy(themeMode = AppThemeMode.SYSTEM)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Angle Unit Section
            SettingsSection(title = "Default Angle Unit") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChoiceChip(
                        title = "Degrees (DEG)",
                        subtitle = "sin(90°) = 1",
                        isSelected = settings.angleMode == AngleMode.DEG,
                        onClick = { onUpdateSettings(settings.copy(angleMode = AngleMode.DEG)) },
                        modifier = Modifier.weight(1f)
                    )
                    ChoiceChip(
                        title = "Radians (RAD)",
                        subtitle = "sin(π/2) = 1",
                        isSelected = settings.angleMode == AngleMode.RAD,
                        onClick = { onUpdateSettings(settings.copy(angleMode = AngleMode.RAD)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Number Notation Section
            SettingsSection(title = "Number Notation Format") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChoiceChip(
                        title = "Auto",
                        subtitle = "Smart switch",
                        isSelected = settings.notationMode == NotationMode.AUTO,
                        onClick = { onUpdateSettings(settings.copy(notationMode = NotationMode.AUTO)) },
                        modifier = Modifier.weight(1f)
                    )
                    ChoiceChip(
                        title = "Standard",
                        subtitle = "1,000,000",
                        isSelected = settings.notationMode == NotationMode.STANDARD,
                        onClick = { onUpdateSettings(settings.copy(notationMode = NotationMode.STANDARD)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ChoiceChip(
                        title = "Scientific",
                        subtitle = "1.0 × 10⁶",
                        isSelected = settings.notationMode == NotationMode.SCIENTIFIC,
                        onClick = { onUpdateSettings(settings.copy(notationMode = NotationMode.SCIENTIFIC)) },
                        modifier = Modifier.weight(1f)
                    )
                    ChoiceChip(
                        title = "Engineering",
                        subtitle = "Multiples of 3",
                        isSelected = settings.notationMode == NotationMode.ENGINEERING,
                        onClick = { onUpdateSettings(settings.copy(notationMode = NotationMode.ENGINEERING)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Decimal Precision Slider
            SettingsSection(title = "Decimal Precision (${settings.precision} decimals)") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Slider(
                        value = settings.precision.toFloat(),
                        onValueChange = { onUpdateSettings(settings.copy(precision = it.toInt())) },
                        valueRange = 2f..14f,
                        steps = 11,
                        modifier = Modifier.fillMaxWidth().testTag("slider_precision")
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "2 decimals", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "8 decimals", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "14 decimals", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Toggles: Thousands separator & Haptic
            SettingsSection(title = "Display & Feedback") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Thousands Separator", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                        Text(text = "Format numbers as 1,234,567", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = settings.useThousandsSeparator,
                        onCheckedChange = { onUpdateSettings(settings.copy(useThousandsSeparator = it)) },
                        modifier = Modifier.testTag("switch_thousands")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Haptic Vibration Feedback", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                        Text(text = "Tactile vibration click on button press", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = settings.hapticFeedback,
                        onCheckedChange = { onUpdateSettings(settings.copy(hapticFeedback = it)) },
                        modifier = Modifier.testTag("switch_haptics")
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun ThemeOptionChip(
    title: String,
    isSelected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = title,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ChoiceChip(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
    }
}
