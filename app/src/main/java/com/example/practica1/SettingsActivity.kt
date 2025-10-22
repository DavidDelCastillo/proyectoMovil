package com.example.practica1

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class SettingsActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var backbtn: Button
    private lateinit var audioBtn: Button
    private lateinit var creditsBtn: Button
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var darkModeSwitch: Switch

    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)

        // Botones existentes
        backbtn = findViewById(R.id.backBtn)
        audioBtn = findViewById(R.id.audioBtn)
        creditsBtn = findViewById(R.id.creditsBtn)

        backbtn.setOnClickListener { BackButtonPressed() }
        audioBtn.setOnClickListener { AudioButtonPressed() }
        creditsBtn.setOnClickListener { CreditsButtonPressed() }

        // Configurar control de volumen
        volumeSeekBar = findViewById(R.id.volumeSeekBar)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volumeSeekBar.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Configurar modo oscuro
        darkModeSwitch = findViewById(R.id.darkModeSwitch)
        val prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        darkModeSwitch.isChecked = isDarkMode

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            prefs.edit { putBoolean("dark_mode", isChecked) }
        }
    }

    private fun BackButtonPressed() {
        AudioStop()
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }

    private fun AudioButtonPressed() {
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

    private fun CreditsButtonPressed() {
        val intent = Intent(this, CreditsActivity::class.java)
        startActivity(intent)
    }

    private fun AudioStop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
