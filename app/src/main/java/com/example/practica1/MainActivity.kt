package com.example.practica1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.view.KeyEvent
import android.widget.Button
import android.widget.TextView
import android.graphics.Color
import android.transition.Scene
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
    val audio: String? = null
)


class MainActivity : ComponentActivity() {

    private fun loadQuestionsFromJson(): List<Question> {
        val inputStream = assets.open("questions.json")
        val reader = InputStreamReader(inputStream)
        val questionType = object : TypeToken<List<Question>>() {}.type
        return Gson().fromJson(reader, questionType)
    }

    private lateinit var questionImage: ImageView
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var questionText: TextView
    private lateinit var questionNumber: TextView
    private lateinit var buttons: List<Button>
    private lateinit var btnNext: Button


    private var currentQuestionIndex = 0
    private var questions: List<Question> = listOf()
    private var answered = false
    private var quizFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionImage = findViewById(R.id.questionImage)

        questionText = findViewById(R.id.questionText)
        questionNumber = findViewById(R.id.questionNumber)

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
        questions = try {
            loadQuestionsFromJson().shuffled().take(5)
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }

        currentQuestionIndex = 0
        quizFinished = false

        if (questions.isNotEmpty()) {
            showQuestion()
            btnNext.text = "Siguiente"
        } else {
            questionText.text = "No se pudieron cargar las preguntas."
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
        questionText.text = "¡Has terminado el quiz! 🎉"
        questionNumber.text = ""
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
