package com.example.practica1

import android.os.Bundle
import androidx.activity.ComponentActivity

open class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val isDark = sharedPref.getBoolean("darkTheme", false)
        if(isDark) setTheme(R.style.Theme_Practica1_Oscuro)
        else setTheme(R.style.Theme_Practica1)
        super.onCreate(savedInstanceState)
    }
}