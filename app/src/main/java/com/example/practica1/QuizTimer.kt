package com.example.practica1

import android.os.Handler
import android.os.Looper

class QuizTimer (private val onTick: (Int) -> Unit){
    private var handler: Handler? = null
    private var secondsElapsed = 0
    private var isRunning = false

    private val runnable = object : Runnable {
        override fun run(){
            if (isRunning){
                secondsElapsed++
                onTick(secondsElapsed)
                handler?.postDelayed(this, 1000)
            }
        }
    }

    fun start() {
        if(isRunning) return
        handler = Handler(Looper.getMainLooper())
        isRunning = true
        handler?.post(runnable)
    }

    fun stop(){
        isRunning = false
        handler?.removeCallbacks(runnable)
    }

    fun getElapsedTime(): Int = secondsElapsed
}
