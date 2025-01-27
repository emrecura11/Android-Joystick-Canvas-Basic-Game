package com.emrecura.canvassimpleagame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.random.Random

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint()
    private var playerX = 200f
    private var playerY = 800f
    private val playerRadius = 50f
    private val moveSpeed = 10f

    private var obstacleX = 1200f
    private var obstacleY = Random.nextInt(300, 1500).toFloat()
    private val obstacleWidth = 100f
    private val obstacleHeight = 200f

    private var isGameOver = false

    fun movePlayer(angle: Float, strength: Float) {
        if (!isGameOver) {
            val angleRad = Math.toRadians(angle.toDouble())
            val deltaX = (moveSpeed * strength / 100 * Math.cos(angleRad)).toFloat()
            val deltaY = (moveSpeed * strength / 100 * Math.sin(angleRad)).toFloat()

            playerX += deltaX
            playerY += deltaY

            // Ekran sınırlarını kontrol et
            playerX = playerX.coerceIn(playerRadius, width.toFloat() - playerRadius)
            playerY = playerY.coerceIn(playerRadius, height.toFloat() - playerRadius)

            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.LTGRAY)

        obstacleX -= 15f
        if (obstacleX + obstacleWidth < 0) {
            obstacleX = width.toFloat()
            obstacleY = Random.nextInt(300, height - 200).toFloat()
        }

        if (isColliding() && playerX - playerRadius <= 0) {
            isGameOver = true
        }

        // Oyuncunun hareketini kısıtla
        if (!isGameOver) {
            if (isColliding()) {
                playerX -= 20f
            }
        }

        paint.color = Color.BLUE
        canvas.drawCircle(playerX, playerY, playerRadius, paint)

        paint.color = Color.RED
        canvas.drawRect(
            obstacleX, obstacleY,
            obstacleX + obstacleWidth, obstacleY + obstacleHeight, paint
        )

        if (isGameOver) {
            paint.color = Color.BLACK
            paint.textSize = 100f
            canvas.drawText("Game Over", width / 2f - 200f, height / 2f, paint)
        } else {
            invalidate()
        }
    }

    private fun isColliding(): Boolean {
        return obstacleX < playerX + playerRadius &&
                obstacleX + obstacleWidth > playerX - playerRadius &&
                playerY + playerRadius > obstacleY &&
                playerY - playerRadius < obstacleY + obstacleHeight
    }
}
