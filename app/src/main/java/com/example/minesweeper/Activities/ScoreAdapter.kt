package com.example.minesweeper.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minesweeper.R
import com.example.minesweeper.model.Score
import kotlin.time.Duration

class ScoreAdapter(private val scores: List<Score>) :
    RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.playerName)
        val timeTextView: TextView = itemView.findViewById(R.id.playerTime)
        val difficultyTextView: TextView = itemView.findViewById(R.id.playerDifficulty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.nameTextView.text = score.getName()
        holder.timeTextView.text = formatDuration(score.getTime())
        holder.difficultyTextView.text = score.getDifficulty().name
        val rankTextView = holder.itemView.findViewById<TextView>(R.id.playerRank)
        rankTextView.text = (position + 1).toString()

    }

    override fun getItemCount() = scores.size

    private fun formatDuration(duration: Duration): String {
        val seconds = duration.inWholeSeconds
        return "${seconds}s"
    }
}