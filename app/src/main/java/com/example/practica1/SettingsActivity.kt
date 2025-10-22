package com.example.practica1

import android.content.Intent
import android.media.MediaPlayer
import android.widget.Button
import android.os.Bundle
import androidx.activity.ComponentActivity



class SettingsActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var backbtn : Button;
    private lateinit var audioBtn : Button;
    private lateinit var creditsBtn : Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)

        backbtn = findViewById(R.id.backBtn)
        backbtn.setOnClickListener{BackButtonPressed();}

        audioBtn = findViewById(R.id.audioBtn)
        audioBtn.setOnClickListener{AudioButtonPressed();}

        creditsBtn = findViewById(R.id.creditsBtn)
        creditsBtn.setOnClickListener{CreditsButtonPressed();}
    }


    private fun BackButtonPressed(){
        AudioStop()

        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }


    private fun AudioButtonPressed(){
        AudioStop()

        try {
            val afd = assets.openFd("MultimediaAssets/minecraft.mp3")
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun CreditsButtonPressed(){

        val intent = Intent(this, CreditsActivity::class.java)
        startActivity(intent)
    }


    private fun AudioStop(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}