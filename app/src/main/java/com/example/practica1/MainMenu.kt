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

//Variables

private lateinit var startBtn : Button;
private lateinit var scoresBtn : Button;
private lateinit var settingsBtn : Button;


//Metodos
class MainMenu : ComponentActivity(){

    var scene1: Scene? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)

        //scene1 = Scene.getSceneForLayout(MainMenuRoot, R.layout.main_menu, this);

        startBtn.setOnClickListener{StartButtonPressed();}
        scoresBtn.setOnClickListener{ScoresButtonPressed();}
        settingsBtn.setOnClickListener{SettingsButtonPressed();}
    }

    private fun StartButtonPressed(){
        //TransitionManager.go(scene1)
    }

    private fun ScoresButtonPressed(){}

    private fun SettingsButtonPressed(){}

}