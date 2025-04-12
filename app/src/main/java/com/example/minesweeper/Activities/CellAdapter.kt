package com.example.minesweeper.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.minesweeper.R
import com.example.minesweeper.game.Board
import com.example.minesweeper.game.CellState
import com.example.minesweeper.model.GameConfig

class CellAdapter(private val context: Context, private val board: Board) : BaseAdapter() {

    override fun getCount(): Int {
        return GameConfig.NUMBER_OF_ROWS * GameConfig.NUMBER_OF_COLUMNS
    }

    override fun getItem(position: Int): Any {
        val x = position % GameConfig.NUMBER_OF_COLUMNS
        val y = position / GameConfig.NUMBER_OF_COLUMNS
        return board.getCellState(x, y)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.cell_item, parent, false)

        val cellText = view.findViewById<TextView>(R.id.cellText)
        val x = position % GameConfig.NUMBER_OF_COLUMNS
        val y = position / GameConfig.NUMBER_OF_COLUMNS

        when (val cellState = board.getCellState(x, y)) {
            is CellState.Hidden -> {
                cellText.text = ""
                view.setBackgroundResource(R.drawable.cell_background)
            }
            is CellState.Flagged -> {
                cellText.text = "🚩"
                view.setBackgroundResource(R.drawable.cell_background)
            }
            is CellState.Revealed -> {
                when (cellState.bombCount) {
                    0 -> {
                        cellText.text = ""
                        cellText.setBackgroundColor(context.getColor(android.R.color.white))
                    }
                    else -> {
                        cellText.text = cellState.bombCount.toString()
                        cellText.setTextColor(getNumberColor(cellState.bombCount))
                        cellText.setBackgroundColor(context.getColor(android.R.color.white))
                    }
                }
            }
        }

        // Assurez-vous que chaque cellule est carrée
        val dimension = parent?.width?.div(GameConfig.NUMBER_OF_COLUMNS) ?: 40
        view.layoutParams.height = dimension
        view.layoutParams.width = dimension

        return view
    }

    private fun getNumberColor(number: Int): Int {
        return when (number) {
            1 -> context.getColor(android.R.color.holo_blue_dark)
            2 -> context.getColor(android.R.color.holo_green_dark)
            3 -> context.getColor(android.R.color.holo_red_dark)
            4 -> context.getColor(android.R.color.holo_purple)
            5 -> context.getColor(android.R.color.holo_orange_dark)
            6 -> context.getColor(android.R.color.holo_blue_light)
            7 -> context.getColor(android.R.color.black)
            8 -> context.getColor(android.R.color.darker_gray)
            else -> context.getColor(android.R.color.black)
        }
    }
}