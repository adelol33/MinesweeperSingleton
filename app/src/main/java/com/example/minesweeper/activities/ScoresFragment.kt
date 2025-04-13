package com.example.minesweeper.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minesweeper.R
import com.example.minesweeper.model.Difficulty
import com.example.minesweeper.model.LeaderboardManager

class ScoresFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var adapter: ScoreAdapter
    private var difficulty: Difficulty? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        emptyView = view.findViewById(R.id.emptyView)

        // Récupérer la difficulté depuis les arguments
        arguments?.let {
            val difficultyName = it.getString(ARG_DIFFICULTY)
            difficulty = if (difficultyName != null) Difficulty.valueOf(difficultyName) else null
        }

        // Configurer RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ScoreAdapter(emptyList())
        recyclerView.adapter = adapter

        // Charger les scores
        loadScores()
    }

    override fun onResume() {
        super.onResume()
        loadScores()
    }

    private fun loadScores() {
        val leaderboardManager = LeaderboardManager(requireContext())
        val scores = if (difficulty != null) {
            leaderboardManager.getScoresByDifficulty(difficulty!!)
        } else {
            leaderboardManager.getAllScores()
        }

        if (scores.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
            adapter.updateScores(scores)
        }
    }

    companion object {
        private const val ARG_DIFFICULTY = "difficulty"

        fun newInstance(difficulty: Difficulty?): ScoresFragment {
            val fragment = ScoresFragment()
            val args = Bundle()
            if (difficulty != null) {
                args.putString(ARG_DIFFICULTY, difficulty.name)
            }
            fragment.arguments = args
            return fragment
        }
    }
}