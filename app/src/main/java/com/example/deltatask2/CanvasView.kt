package com.example.deltatask2

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.lang.Math.*

private const val STROKE_WIDTH = 10f

class CanvasView(context: Context) : View(context) {

    lateinit var curCanvas: Canvas
    lateinit var curBitmap: Bitmap


    var centerX = 0f
    var centerY = 0f
    var centerYMin = 0f
    var secRadius = 500f
    var secHandLength = 400f
    var minRadius = 200f
    var minHandLength = 150f

    var currSecondAngle = 0.0
    var savedSecondAngle = 0.0
    var currMinAngle = 0.0
    var savedMinAngle = 0.0

    var markerLinesSec = mutableListOf<MutableList<Float>>()
    var markerLinesMin = mutableListOf<MutableList<Float>>()
    var secHand = mutableListOf<Float>(0f, -secHandLength)
    var minHand = mutableListOf<Float>(0f, -minHandLength)

    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorAccent, null)


    // Set up the paint with which to draw.
    private val clockPaint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    private val handPaint = Paint().apply {
        color = ResourcesCompat.getColor(resources,android.R.color.white, null)
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    init{

        var angle = 0.0
        for(i in (1..60)){
            angle += 6.0
            val tempList = mutableListOf<Float>(
                ((secRadius-15)* kotlin.math.cos(toRadians(angle))).toFloat(),
                ((secRadius-15)* kotlin.math.sin(toRadians(angle))).toFloat(),
                ((secRadius)* kotlin.math.cos(toRadians(angle))).toFloat(),
                ((secRadius)* kotlin.math.sin(toRadians(angle))).toFloat())
            markerLinesSec.add(tempList)
        }
        var minAngle = 0.0
        for(i in (1..12)){

            Log.d("hi",minAngle.toString())
            val tempList = mutableListOf<Float>(
                ((minRadius-10)* kotlin.math.cos(toRadians(minAngle))).toFloat(),
                ((minRadius-10)* kotlin.math.sin(toRadians(minAngle))).toFloat(),
                ((minRadius)* kotlin.math.cos(toRadians(minAngle))).toFloat(),
                ((minRadius)* kotlin.math.sin(toRadians(minAngle))).toFloat())
            markerLinesMin.add(tempList)
            minAngle += 30.0
        }
    }


    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        curBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        curCanvas = Canvas(curBitmap)
        centerY = height.toFloat()/2
        centerX = width.toFloat()/2
        centerYMin = centerY - 250f


    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.rgb(33, 33, 33))
        for(i in markerLinesSec){
            canvas.drawLine(centerX+i[0],centerY+i[1],centerX+i[2],centerY+i[3],clockPaint)
        }
        for(i in markerLinesMin){
            canvas.drawLine(centerX+i[0],centerYMin+i[1],centerX+i[2],centerYMin+i[3],clockPaint)
        }
        //canvas.drawLine(centerX,centerY,centerX+200,centerY+200,paint)
        //canvas.drawLine((centerX + 180* kotlin.math.cos(30.0)).toFloat(),(centerY + 180* kotlin.math.sin(30.0)).toFloat(),(centerX + 200* kotlin.math.cos(30.0)).toFloat(),(centerY + 200* kotlin.math.sin(30.0)).toFloat(),paint)
        canvas.drawCircle(centerX, centerY,secRadius,clockPaint)
        canvas.drawCircle(centerX,centerYMin,minRadius,clockPaint)
        //Drawing second hand
        canvas.drawCircle(centerX,centerY,10f,handPaint)
        canvas.drawLine(centerX,centerY,secHand[0]+centerX,secHand[1]+centerY,handPaint)
        //Drawing minute hand
        canvas.drawCircle(centerX,centerYMin,5f,handPaint)
        canvas.drawLine(centerX,centerYMin,minHand[0]+centerX,minHand[1]+centerYMin,handPaint)
    }

    fun tick(time: Long){

        var secAngle = ((time.toDouble()/60000.0)*360.0)
        var minAngle = ((time.toDouble()/900000.0)*360.0)

        currSecondAngle = secAngle + savedSecondAngle
        secAngle += savedSecondAngle
        drawSecondHand(secAngle)
        currMinAngle = minAngle + savedMinAngle
        minAngle += savedMinAngle
        drawMinuteHand(minAngle)
    }

    fun drawSecondHand(angle: Double){
        secHand.clear()
        secHand.add((secHandLength* kotlin.math.cos(toRadians(angle-90))).toFloat())
        secHand.add((secHandLength* kotlin.math.sin(toRadians(angle-90))).toFloat())
        invalidate()
    }
    fun drawMinuteHand(angle: Double){
        minHand.clear()
        minHand.add((minHandLength* kotlin.math.cos(toRadians(angle-90))).toFloat())
        minHand.add((minHandLength* kotlin.math.sin(toRadians(angle-90))).toFloat())
        invalidate()
    }
    fun saveTime(){
        savedSecondAngle = currSecondAngle
        savedMinAngle = currMinAngle
    }
    fun resetTime(){
        savedSecondAngle = 0.0
        savedMinAngle = 0.0
        secHand = mutableListOf<Float>(0f, -secHandLength)
        minHand = mutableListOf<Float>(0f, -minHandLength)
        invalidate()
    }

}