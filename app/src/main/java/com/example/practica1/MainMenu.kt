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
import android.content.SharedPreferences
import androidx.core.content.edit


//Metodos
class MainMenu : BaseActivity(){

    private lateinit var startBtn : Button;
    private lateinit var scoresBtn : Button;
    private lateinit var settingsBtn : Button;

    private lateinit var themeBtn : Button;

    private var isDark: Boolean = false;
    private lateinit var sharedPref: SharedPreferences;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        isDark = sharedPref.getBoolean("darkTheme", false)

        setContentView(R.layout.main_menu)

        startBtn = findViewById(R.id.StartBtn)
        scoresBtn = findViewById(R.id.ScoresBtn)
        settingsBtn = findViewById(R.id.SettingsBtn)
        themeBtn = findViewById(R.id.ThemeBtn)

        startBtn.setOnClickListener{StartButtonPressed();}
        scoresBtn.setOnClickListener{ScoresButtonPressed();}
        settingsBtn.setOnClickListener{SettingsButtonPressed();}
        themeBtn.setOnClickListener { ThemeButtonPressed(); }
    }

    private fun StartButtonPressed(){
        val intent = Intent(this, SelectDifficultyActivity::class.java)
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

    private fun ThemeButtonPressed(){
        val newTheme = !isDark
        sharedPref.edit { putBoolean("darkTheme", newTheme) }
        recreate()
    }

}