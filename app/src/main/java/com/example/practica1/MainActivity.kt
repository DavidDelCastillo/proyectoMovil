package com.example.practica1

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

data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)


class MainActivity : ComponentActivity() {

    private fun loadQuestionsFromJson(): List<Question> {
        val inputStream = assets.open("questions.json")
        val reader = InputStreamReader(inputStream)
        val questionType = object : TypeToken<List<Question>>() {}.type
        return Gson().fromJson(reader, questionType)
    }
    private lateinit var questionText: TextView
    private lateinit var questionNumber: TextView
    private lateinit var buttons: List<Button>

    private var currentQuestionIndex = 0
    private var questions: List<Question> = listOf()

    private var answered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionText = findViewById(R.id.questionText)
        questionNumber = findViewById(R.id.questionNumber)

        val btn1: Button = findViewById(R.id.btn1)
        val btn2: Button = findViewById(R.id.btn2)
        val btn3: Button = findViewById(R.id.btn3)
        val btn4: Button = findViewById(R.id.btn4)
        buttons = listOf(btn1, btn2, btn3, btn4)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (!answered) {
                    checkAnswer(index)
                }
            }
        }

        questions = try {
            loadQuestionsFromJson().shuffled().take(5)
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }

        if (questions.isNotEmpty()) {
            showQuestion()
        } else {
            questionText.text = "No se pudieron cargar las preguntas."
        }
    }

    private fun showQuestion() {
        val q = questions[currentQuestionIndex]
        questionText.text = q.text
        questionNumber.text = "Pregunta ${currentQuestionIndex + 1}"
        answered = false

        buttons.forEachIndexed { index, button ->
            button.text = q.options[index]
            button.setBackgroundColor(Color.parseColor("#E0E0E0"))
            button.isEnabled = true
        }
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_SPACE && answered) {
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                showQuestion()
            } else {
                questionText.text = "¡Has terminado el quiz! 🎉"
                questionNumber.text = ""
                buttons.forEach {
                    it.text = ""
                    it.isEnabled = false
                    it.setBackgroundColor(Color.WHITE)
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
