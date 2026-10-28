package com.example.engine

enum class AngleMode {
    DEG,
    RAD;

    fun toggle(): AngleMode = if (this == DEG) RAD else DEG
}

enum class NotationMode {
    AUTO,
    STANDARD,
    SCIENTIFIC,
    ENGINEERING
}
