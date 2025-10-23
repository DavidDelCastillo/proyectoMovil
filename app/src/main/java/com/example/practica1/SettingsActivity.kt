package com.example.practica1

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : BaseActivity() {

    private lateinit var audioManager: AudioManager
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var backbtn: Button
    private lateinit var audioBtn: Button
    private lateinit var creditsBtn: Button
    private lateinit var resetScoresBtn: Button
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)

        // Botones
        backbtn = findViewById(R.id.backBtn)
        audioBtn = findViewById(R.id.audioBtn)
        creditsBtn = findViewById(R.id.creditsBtn)
        resetScoresBtn = findViewById(R.id.button3)

        backbtn.setOnClickListener { BackButtonPressed() }
        audioBtn.setOnClickListener { AudioButtonPressed() }
        creditsBtn.setOnClickListener { CreditsButtonPressed() }
        resetScoresBtn.setOnClickListener { ResetScoresButtonPressed() }

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
    }

    // --- FUNCIONES DE BOTONES ---

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

    private fun ResetScoresButtonPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reiniciar puntuaciones")
        builder.setMessage("¿Estás seguro de que quieres borrar todos los récords? Esta acción no se puede deshacer.")
        builder.setPositiveButton("Sí") { _, _ ->
            val prefs = getSharedPreferences("Scores", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
            Toast.makeText(this, "Puntuaciones reiniciadas correctamente.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun AudioStop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
