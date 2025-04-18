package com.example.minesweeper.game

import com.example.minesweeper.model.*
import kotlin.random.Random

class Board private constructor(private val difficulty: Difficulty) {
    private val cells = initCells()

    private fun initCells(): List<List<Cell>> {
        val cells = buildList {
            for (y in 0 until GameConfig.NUMBER_OF_ROWS) {
                add(buildList {
                    for (x in 0 until GameConfig.NUMBER_OF_COLUMNS) {
                        add(Cell())
                    }
                })
            }
        }

        repeat(difficulty.numberOfBombs) {
            var bombIsSet = false
            while (!bombIsSet) {
                val y = Random.nextInt(0, GameConfig.NUMBER_OF_ROWS)
                val x = Random.nextInt(0, GameConfig.NUMBER_OF_COLUMNS)
                if (!cells[y][x].isBomb()) {
                    val bombType = if (Random.nextBoolean()) ClassicBomb() else AlienBomb()
                    cells[y][x].markAsBomb(bombType)
                    bombIsSet = true
                }
            }
        }
        return cells
    }

    fun getCellState(x: Int, y: Int): CellState {
        val cell = cells[y][x]
        return when {
            cell.isRevealed() -> {
                val bombCount = surroundingBombs(x, y)
                CellState.Revealed(bombCount)
            }
            cell.isFlagged() -> CellState.Flagged
            else -> CellState.Hidden
        }
    }

    fun surroundingBombs(x: Int, y: Int): Int {
        var numberOfBombs = 0
        for (j in y - 1..y + 1) {
            if (j in 0 until GameConfig.NUMBER_OF_ROWS) {
                for (i in x - 1..x + 1) {
                    if (i in 0 until GameConfig.NUMBER_OF_COLUMNS) {
                        if (!(i == x && j == y)) {
                            if (cells[j][i].isBomb()) {
                                numberOfBombs += 1
                            }
                        }
                    }
                }
            }
        }
        return numberOfBombs
    }

    fun reveal(x: Int, y: Int) {
        cells[y][x].reveal()
    }

    fun isRevealed(x: Int, y: Int): Boolean {
        return cells[y][x].isRevealed()
    }

    fun flag(x: Int, y: Int) {
        cells[y][x].flag()
    }

    fun isGameWon(): Boolean {
        for (y in 0 until GameConfig.NUMBER_OF_ROWS) {
            for (x in 0 until GameConfig.NUMBER_OF_COLUMNS) {
                if (!cells[y][x].isBomb() && !cells[y][x].isRevealed()) {
                    return false
                }
            }
        }
        return true
    }

    fun isBomb(x: Int, y: Int): Boolean {
        return cells[y][x].isBomb()
    }

    fun executeBombBehavior(x: Int, y: Int): String? {
        return cells[y][x].executeBombBehavior()
    }

    fun getCell(x: Int, y: Int): Cell {
        return cells[y][x]
    }

    fun revealAllCells() {
        for (y in 0 until GameConfig.NUMBER_OF_ROWS) {
            for (x in 0 until GameConfig.NUMBER_OF_COLUMNS) {
                val cell = getCell(x, y)
                cell.reveal()
            }
        }
    }

    companion object {
        @Volatile
        private var instance: Board? = null

        fun getInstance(difficulty: Difficulty): Board {
            return instance ?: synchronized(this) {
                instance ?: Board(difficulty).also { instance = it }
            }
        }

        fun resetInstance() {
            synchronized(this) {
                instance = null
            }
        }
    }
}