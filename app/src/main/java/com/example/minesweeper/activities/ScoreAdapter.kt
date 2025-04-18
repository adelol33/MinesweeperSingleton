package com.example.minesweeper.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.minesweeper.R
import com.example.minesweeper.model.Score
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScoreAdapter(
    private val context: Context,
    private var scores: List<Score> = emptyList()
) : BaseAdapter() {

    fun updateScores(newScores: List<Score>) {
        scores = newScores
        notifyDataSetChanged()
    }

    override fun getCount(): Int = scores.size

    override fun getItem(position: Int): Score = scores[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_score, parent, false)

        val score = getItem(position)

        val rankTextView = view.findViewById<TextView>(R.id.textRank)
        val nameTextView = view.findViewById<TextView>(R.id.textPlayerName)
        val timeTextView = view.findViewById<TextView>(R.id.textTime)
        val dateTextView = view.findViewById<TextView>(R.id.textDate)

        rankTextView.text = "${position + 1}."
        nameTextView.text = score.playerName
        timeTextView.text = "${score.timePlayed.inWholeSeconds}s"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateTextView.text = dateFormat.format(Date(score.timestamp))

        when (position) {
            0 -> view.setBackgroundResource(R.drawable.leaderboard_gold_bg)
            1 -> view.setBackgroundResource(R.drawable.leaderboard_silver_bg)
            2 -> view.setBackgroundResource(R.drawable.leaderboard_bronze_bg)
            else -> view.setBackgroundResource(R.drawable.leaderboard_normal_bg)
        }

        return view
    }
}