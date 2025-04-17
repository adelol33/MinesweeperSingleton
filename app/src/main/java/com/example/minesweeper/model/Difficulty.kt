package com.example.minesweeper.model

enum class Difficulty(val numberOfBombs: Int, val label: String) {
    EASY(3, "Facile"),
    MEDIUM(10, "Moyen"),
    HARD(15, "Difficile")
}