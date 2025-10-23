package com.example.practica1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button

class SelectDifficultyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_difficulty)

        val btnFacil: Button = findViewById(R.id.btnFacil)
        val btnIntermedio: Button = findViewById(R.id.btnIntermedio)
        val btnDificil: Button = findViewById(R.id.btnDificil)

        btnFacil.setOnClickListener { startQuiz("facil") }
        btnIntermedio.setOnClickListener { startQuiz("intermedio") }
        btnDificil.setOnClickListener { startQuiz("dificil") }
    }

    private fun startQuiz(difficulty: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("difficulty", difficulty)
        startActivity(intent)
    }
}
