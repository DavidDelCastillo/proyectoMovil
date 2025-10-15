package com.example.practica1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Button
import android.graphics.Color
import android.widget.Button

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn1: Button = findViewById(R.id.btn1)
        val btn2: Button = findViewById(R.id.btn2)
        val btn3: Button = findViewById(R.id.btn3) // ✅ Correcta
        val btn4: Button = findViewById(R.id.btn4)

        val botones = listOf(btn1, btn2, btn3, btn4)
        val correcta = btn3

        botones.forEach { boton ->
            boton.setOnClickListener {
                if (boton == correcta) {
                    boton.setBackgroundColor(Color.parseColor("#4CAF50")) // Verde
                } else {
                    boton.setBackgroundColor(Color.parseColor("#F44336")) // Rojo
                }

                // Desactiva todos los botones después de elegir
                botones.forEach { it.isEnabled = false }
            }
        }
    }
}
