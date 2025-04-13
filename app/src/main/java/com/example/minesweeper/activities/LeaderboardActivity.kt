package com.example.minesweeper.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.minesweeper.R
import com.example.minesweeper.model.Difficulty
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        // Configurer la toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Classement"

        // Initialiser ViewPager et TabLayout
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        // Configurer l'adaptateur
        val pagerAdapter = LeaderboardPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // Connecter TabLayout avec ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Tous"
                1 -> Difficulty.EASY.label
                2 -> Difficulty.MEDIUM.label
                3 -> Difficulty.HARD.label
                else -> "Inconnu"
            }
        }.attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Adaptateur pour le ViewPager
    private inner class LeaderboardPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ScoresFragment.newInstance(null) // Tous les scores
                1 -> ScoresFragment.newInstance(Difficulty.EASY)
                2 -> ScoresFragment.newInstance(Difficulty.MEDIUM)
                3 -> ScoresFragment.newInstance(Difficulty.HARD)
                else -> throw IllegalArgumentException("Position invalide: $position")
            }
        }
    }
}