package com.android.app.rangeBar

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader

/**
 * Created by Saurabh Gupta on 21/02/23.
 */
/**
 * Class representing the blue connecting line between the two thumbs.
 */
class ConnectingLine(
    y: Float, connectingLineWeight: Float,
    connectingLineColors: ArrayList<Int>?
) {
    // Member Variables ////////////////////////////////////////////////////////
    private val colors: IntArray
    private val positions: FloatArray
    private val paint = Paint()
    private val mY: Float
    // Constructor /////////////////////////////////////////////////////////////
    /**
     * Constructor for connecting line
     *
     * @param y                    the y co-ordinate for the line
     * @param connectingLineWeight the weight of the line
     * @param connectingLineColors the color of the line
     */
    init {

        //Need two colors
        if (connectingLineColors!!.size == 1) {
            connectingLineColors.add(connectingLineColors[0])
        }
        colors = IntArray(connectingLineColors.size)
        positions = FloatArray(connectingLineColors.size)
        for (index in connectingLineColors.indices) {
            colors[index] = connectingLineColors[index]
            positions[index] = index.toFloat() / (connectingLineColors.size - 1)
        }
        paint.strokeWidth = connectingLineWeight
        paint.strokeCap = Paint.Cap.ROUND
        paint.isAntiAlias = true
        mY = y
    }

    private fun getLinearGradient(startX: Float, endX: Float, height: Float): LinearGradient {
        return LinearGradient(
            startX, height, endX, height,
            colors,
            positions,
            Shader.TileMode.REPEAT
        )
    }

    /**
     * Draw the connecting line between the two thumbs in rangebar.
     *
     * @param canvas     the Canvas to draw to
     * @param leftThumb  the left thumb
     * @param rightThumb the right thumb
     */
    fun draw(canvas: Canvas, leftThumb: PinView?, rightThumb: PinView?) {
        paint.shader = getLinearGradient(0f, canvas.width.toFloat(), mY)
        canvas.drawLine(leftThumb!!.x, mY, rightThumb!!.x, mY, paint)
    }

    /**
     * Draw the connecting line between for single slider.
     *
     * @param canvas     the Canvas to draw to
     * @param rightThumb the right thumb
     * @param leftMargin the left margin
     */
    fun draw(canvas: Canvas, leftMargin: Float, rightThumb: PinView?) {
        paint.shader = getLinearGradient(0f, canvas.width.toFloat(), mY)
        canvas.drawLine(leftMargin, mY, rightThumb!!.x, mY, paint)
    }
}