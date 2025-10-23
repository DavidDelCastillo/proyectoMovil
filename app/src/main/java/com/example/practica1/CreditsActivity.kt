package com.example.practica1

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class CreditsActivity : BaseActivity() {

    private lateinit var backbtn : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.credits_menu)

        backbtn = findViewById(R.id.creditsBackBtn)
        backbtn.setOnClickListener{BackButtonPressed();}

    }


    private fun BackButtonPressed(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}