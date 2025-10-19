package com.example.practica1

import android.os.Bundle
import android.transition.Scene
import android.transition.TransitionManager
import android.widget.Button
import androidx.activity.ComponentActivity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.ui.graphics.findFirstRoot
import android.content.Intent


//Metodos
class MainMenu : ComponentActivity(){

    private lateinit var startBtn : Button;
    private lateinit var scoresBtn : Button;
    private lateinit var settingsBtn : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)

        startBtn = findViewById(R.id.StartBtn)
        scoresBtn = findViewById(R.id.ScoresBtn)
        settingsBtn = findViewById(R.id.SettingsBtn)

        startBtn.setOnClickListener{StartButtonPressed();}
        scoresBtn.setOnClickListener{ScoresButtonPressed();}
        settingsBtn.setOnClickListener{SettingsButtonPressed();}
    }

    private fun StartButtonPressed(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun ScoresButtonPressed(){
        val intent = Intent(this, ScoresActivity::class.java)
        startActivity(intent)
    }

    private fun SettingsButtonPressed(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

}