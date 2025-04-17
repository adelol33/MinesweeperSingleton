package com.example.minesweeper.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.minesweeper.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton = findViewById<Button>(R.id.playButton)
        playButton.setOnClickListener {
            val intent = Intent(this, DifficultySelectActivity::class.java)
            startActivity(intent)
        }

        val leaderboardButton = findViewById<Button>(R.id.leaderboardButton)
        leaderboardButton.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }
}
// choses à faire et demander :
// code :
//    1. ajouter les boutons retour et menu
//    2. permettre de supprimer les scores dans leaderboard (ou en mettre que 10)
//    3. pimper l'app ? (boutons difficulté, écran d'acceuil)
//    4. ajouter commentaires (bcp bcp bcp)
// autre :
//    1. identifier les différents concepts OO dans notre code et expliquer leur utilité
//    2. expliquer ça dans le rapport (lier les concepts OO à nos implémentations ?)
//    3. essayer l'appli sur l'ordi de qqun d'autre pls
//    4. pas oublier d'enlever ces commentaires ci !