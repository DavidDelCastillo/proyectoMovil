package com.example.practica1

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ScoresActivity : AppCompatActivity() {

    private lateinit var easyBtn: Button
    private lateinit var mediumBtn: Button
    private lateinit var hardBtn: Button
    private lateinit var scoreText: TextView
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scores_layout)

        easyBtn = findViewById(R.id.easyBtn)
        mediumBtn = findViewById(R.id.mediumBtn)
        hardBtn = findViewById(R.id.hardBtn)
        scoreText = findViewById(R.id.scoreText)
        backBtn = findViewById(R.id.backBtn)

        val prefs = getSharedPreferences("Scores", Context.MODE_PRIVATE)

        // Mostrar por defecto puntuación fácil
        showScore(prefs, "max_easy")

        easyBtn.setOnClickListener { showScore(prefs, "max_easy") }
        mediumBtn.setOnClickListener { showScore(prefs, "max_medium") }
        hardBtn.setOnClickListener { showScore(prefs, "max_hard") }

        backBtn.setOnClickListener { finish() }
    }

    private fun showScore(prefs: android.content.SharedPreferences, key: String) {
        val score = prefs.getInt(key, 0)
        val difficulty = when (key) {
            "max_easy" -> "Fácil"
            "max_medium" -> "Media"
            "max_hard" -> "Difícil"
            else -> ""
        }
        scoreText.text = "Dificultad: $difficulty\nPuntuación máxima: $score"
    }
}
