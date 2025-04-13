package com.example.minesweeper.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.minesweeper.R
import com.example.minesweeper.model.Difficulty
import com.example.minesweeper.model.Leaderboard
import com.example.minesweeper.model.Score

class ScoresFragment : Fragment() {
    private lateinit var difficulty: Difficulty
    private lateinit var scoreAdapter: ScoreAdapter
    private lateinit var emptyView: TextView
    private lateinit var listView: ListView

    companion object {
        private const val ARG_DIFFICULTY = "difficulty"

        fun newInstance(difficulty: Difficulty): ScoresFragment {
            return ScoresFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DIFFICULTY, difficulty.name)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val difficultyName = arguments?.getString(ARG_DIFFICULTY) ?: Difficulty.EASY.name
        difficulty = Difficulty.valueOf(difficultyName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.listViewScores)
        emptyView = view.findViewById(R.id.textEmptyScores)

        // Initialiser l'adaptateur
        scoreAdapter = ScoreAdapter(requireContext())
        listView.adapter = scoreAdapter

        // Charger les scores
        loadScores()
    }

    override fun onResume() {
        super.onResume()
        // Recharger les scores à chaque fois que le fragment devient visible
        loadScores()
    }

    private fun loadScores() {
        val leaderboard = Leaderboard(requireContext())
        val scores = leaderboard.getScores(difficulty)

        scoreAdapter.updateScores(scores)

        // Afficher un message si aucun score
        if (scores.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }
    }
}