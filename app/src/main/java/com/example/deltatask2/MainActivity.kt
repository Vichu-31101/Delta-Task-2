package com.example.deltatask2


import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {
    lateinit var customHandler: Handler
    lateinit var canvas : CanvasView
    var startTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startTime = SystemClock.uptimeMillis()
        canvas = CanvasView(applicationContext)
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val layout = findViewById<ViewGroup>(R.id.rootView)

        params.addRule(RelativeLayout.ABOVE, R.id.start)
        layout.addView(canvas,params)


        Log.d("hi",(canvas.secHandLength).toString())

        customHandler = Handler()
        start.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            customHandler.postDelayed(clockTick,0)
        }
        stop.setOnClickListener {
            canvas.saveTime()
            customHandler.removeCallbacks(clockTick)
        }
        reset.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            canvas.resetTime()
        }
    }

    private var clockTick = object: Runnable {
        override fun run() {
            var timeElapsed = SystemClock.uptimeMillis() - startTime

            canvas.tick(timeElapsed)
            customHandler.postDelayed(this, 0)
        }

    }

}