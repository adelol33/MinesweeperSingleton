package com.example.minesweeper.game

sealed class CellState {
    object Hidden : CellState()
    object Flagged : CellState()
    data class Revealed(val bombCount: Int) : CellState()
}