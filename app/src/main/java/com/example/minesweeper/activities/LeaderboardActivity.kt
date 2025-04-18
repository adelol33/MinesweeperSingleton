package com.example.minesweeper.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.minesweeper.R
import com.example.minesweeper.model.Difficulty
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        val adapter = LeaderboardPagerAdapter(this)
        viewPager.adapter = adapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Facile"
                1 -> "Moyen"
                2 -> "Difficile"
                else -> "Expert"
            }
        }.attach()
    }

    class LeaderboardPagerAdapter(activity: AppCompatActivity) :
        androidx.viewpager2.adapter.FragmentStateAdapter(activity) {

        private val difficulties = Difficulty.values()

        override fun getItemCount(): Int = difficulties.size

        override fun createFragment(position: Int): androidx.fragment.app.Fragment {
            return ScoresFragment.newInstance(difficulties[position])
        }
    }
}
