package com.emrecura.canvassimpleagame


import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private lateinit var joystick: JoystickView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // GameView ve Joystick'i bağlama
        gameView = findViewById(R.id.gameView)
        joystick = findViewById(R.id.joystick)

        // Joystick hareketlerini dinle
        joystick.onMove = { dx, dy ->
            val angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()
            val strength = sqrt(dx * dx + dy * dy) * 100 // Kuvveti yüzde olarak hesapla
            gameView.movePlayer(angle, strength*2)
        }
    }
}



