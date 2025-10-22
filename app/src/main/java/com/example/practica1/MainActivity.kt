package com.example.practica1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.TextView
import android.graphics.Color
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import android.media.MediaPlayer
import android.widget.ImageView
import android.graphics.BitmapFactory
import android.view.View

data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val image: String? = null,
    val audio: String? = null,
    val difficulty: String
)

class MainActivity : ComponentActivity() {

    private fun loadQuestionsFromJson(): List<Question> {
        val inputStream = assets.open("questions.json")
        val reader = InputStreamReader(inputStream)
        val questionType = object : TypeToken<List<Question>>() {}.type
        return Gson().fromJson(reader, questionType)
    }

    private var chronometer: QuizTimer? = null
    private lateinit var questionImage: ImageView
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var questionText: TextView
    private lateinit var questionNumber: TextView
    private lateinit var timerLabel: TextView
    private lateinit var buttons: List<Button>
    private lateinit var btnNext: Button

    private var playerScore: Int = 0
    private var currentQuestionIndex = 0
    private var questions: List<Question> = listOf()
    private var answered = false
    private var quizFinished = false

    // NUEVO → Guardar dificultad para registrar puntuación
    private var selectedDifficulty: String = "facil"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionImage = findViewById(R.id.questionImage)
        questionText = findViewById(R.id.questionText)
        questionNumber = findViewById(R.id.questionNumber)
        timerLabel = findViewById(R.id.timerLabel)

        btnNext = findViewById(R.id.btn0)
        val btn1: Button = findViewById(R.id.btn1)
        val btn2: Button = findViewById(R.id.btn2)
        val btn3: Button = findViewById(R.id.btn3)
        val btn4: Button = findViewById(R.id.btn4)
        buttons = listOf(btn1, btn2, btn3, btn4)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (!answered && !quizFinished) {
                    checkAnswer(index)
                }
            }
        }

        btnNext.setOnClickListener {
            if (!quizFinished) {
                if (answered) {
                    goToNextQuestion()
                }
            } else {
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
            }
        }

        loadQuiz()
    }

    private fun loadQuiz() {
        // MODIFICADO → Guardamos dificultad como propiedad
        selectedDifficulty = intent.getStringExtra("difficulty") ?: "facil"

        questions = try {
            loadQuestionsFromJson()
                .filter { it.difficulty.equals(selectedDifficulty, ignoreCase = true) }
                .shuffled()
                .take(5)
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }

        currentQuestionIndex = 0
        quizFinished = false
        playerScore = 0

        chronometer = QuizTimer { seconds ->
            val minutes = seconds / 60
            val secs = seconds % 60
            timerLabel.text = String.format("%02d:%02d", minutes, secs)
        }

        if (questions.isNotEmpty()) {
            chronometer?.start()
            showQuestion()
            btnNext.text = "Siguiente"
        } else {
            questionText.text = "No se pudieron cargar las preguntas de dificultad '$selectedDifficulty'."
        }
    }

    private fun showQuestion() {
        val q = questions[currentQuestionIndex]
        questionText.text = q.text
        questionNumber.text = "Pregunta ${currentQuestionIndex + 1}"
        answered = false

        // --- IMAGEN ---
        if (q.image != null) {
            try {
                val inputStream = assets.open("MultimediaAssets/${q.image}")
                val bitmap = BitmapFactory.decodeStream(inputStream)
                questionImage.setImageBitmap(bitmap)
                questionImage.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
                questionImage.visibility = View.GONE
            }
        } else {
            questionImage.visibility = View.GONE
        }

        // --- AUDIO ---
        stopAudio()

        if (!q.audio.isNullOrEmpty()) {
            try {
                val afd = assets.openFd("MultimediaAssets/${q.audio}")
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                mediaPlayer?.prepare()
                mediaPlayer?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        buttons.forEachIndexed { index, button ->
            button.text = q.options[index]
            button.setBackgroundColor(Color.parseColor("#E0E0E0"))
            button.isEnabled = true
        }
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun checkAnswer(selectedIndex: Int) {
        val q = questions[currentQuestionIndex]
        answered = true

        if (selectedIndex == q.correctIndex) {
            playerScore += 10 // puntuación del jugador
        }

        buttons.forEachIndexed { index, button ->
            button.isEnabled = false
            if (index == q.correctIndex) {
                button.setBackgroundColor(Color.parseColor("#4CAF50")) // Verde
            } else if (index == selectedIndex) {
                button.setBackgroundColor(Color.parseColor("#F44336")) // Rojo
            }
        }
    }

    private fun goToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            showQuestion()
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        chronometer?.stop()
        questionText.text = "¡Has terminado el quiz! 🎉"

        val totalScore = playerScore - (chronometer?.getElapsedTime() ?: 0)
        questionNumber.text = "Puntuación: $totalScore"

        // --- GUARDAR PUNTUACIÓN MÁXIMA POR DIFICULTAD ---
        val prefs = getSharedPreferences("Scores", MODE_PRIVATE)
        val key = when (selectedDifficulty.lowercase()) {
            "facil" -> "max_easy"
            "media" -> "max_medium"
            "dificil" -> "max_hard"
            else -> "max_easy"
        }

        val currentMax = prefs.getInt(key, 0)
        if (totalScore > currentMax) {
            prefs.edit().putInt(key, totalScore).apply()
        }

        // --- LIMPIAR PANTALLA ---
        buttons.forEach {
            it.text = ""
            it.isEnabled = false
            it.setBackgroundColor(Color.WHITE)
        }

        btnNext.text = "Volver al menú"
        quizFinished = true
        stopAudio()
        questionImage.setImageDrawable(null)
        questionImage.visibility = View.GONE
    }

    private fun restartQuiz() {
        loadQuiz()
    }
}
