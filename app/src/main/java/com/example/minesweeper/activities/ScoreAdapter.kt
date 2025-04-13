package com.example.minesweeper.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minesweeper.R
import com.example.minesweeper.model.Score
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScoreAdapter(private var scores: List<Score>) :
    RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rankTextView: TextView = itemView.findViewById(R.id.playerRank)
        val nameTextView: TextView = itemView.findViewById(R.id.playerName)
        val timeTextView: TextView = itemView.findViewById(R.id.playerTime)
        val difficultyTextView: TextView = itemView.findViewById(R.id.playerDifficulty)
        val dateTextView: TextView? = itemView.findViewById(R.id.playerDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.rankTextView.text = (position + 1).toString()
        holder.nameTextView.text = score.getName()
        holder.timeTextView.text = score.getFormattedTime()
        holder.difficultyTextView.text = score.getDifficulty().label

        // Afficher la date si le TextView existe
        holder.dateTextView?.let {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = Date(score.timestamp)
            it.text = dateFormat.format(date)
        }
    }

    override fun getItemCount() = scores.size

    fun updateScores(newScores: List<Score>) {
        scores = newScores
        notifyDataSetChanged()
    }
}