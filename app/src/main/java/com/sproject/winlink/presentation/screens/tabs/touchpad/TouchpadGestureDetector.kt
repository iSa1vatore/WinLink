package com.sproject.winlink.presentation.screens.tabs.touchpad

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

open class TouchpadGestureDetector : View.OnTouchListener {

    private var pressStartTime: Long = 0

    private var pressedX = 0f
    private var pressedY = 0f

    private var prevMoveX = 0f
    private var prevMoveY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                pressStartTime = System.currentTimeMillis()

                pressedX = e.x
                pressedY = e.y
            }
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> {
                val pressDuration = System.currentTimeMillis() - pressStartTime

                if (pressDuration < MAX_CLICK_DURATION && distance(
                        pressedX,
                        pressedY,
                        e.x,
                        e.y
                    ) < MAX_CLICK_DISTANCE
                ) {

                    if (e.pointerCount == 1) onLeftButtonClick()
                    if (e.pointerCount == 2) onRightButtonClick()
                }

                prevMoveX = 0f
                prevMoveY = 0f
            }
            MotionEvent.ACTION_MOVE -> {
                if (prevMoveY == 0f) {
                    setPrevMousePosition(e.x, e.y)
                } else {
                    val x = e.x - prevMoveX
                    val y = e.y - prevMoveY

                    setPrevMousePosition(e.x, e.y)

                    if (e.pointerCount == 1) onMove(x.toInt(), y.toInt())
                    if (e.pointerCount == 2) onScroll(x.toInt(), y.toInt())
                }
            }
        }

        return true
    }

    open fun onLeftButtonClick() {}

    open fun onRightButtonClick() {}

    open fun onMove(x: Int, y: Int) {}

    open fun onScroll(x: Int, y: Int) {}

    private fun setPrevMousePosition(x: Float, y: Float) {
        prevMoveX = x
        prevMoveY = y
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x1 - x2
        val dy = y1 - y2
        val distanceInPx = sqrt((dx * dx + dy * dy).toDouble()).toFloat()
        return pxToDp(distanceInPx)
    }

    private fun pxToDp(px: Float): Float {
        return px / Resources.getSystem().displayMetrics.density
    }

    companion object {
        private const val MAX_CLICK_DURATION = 1000
        private const val MAX_CLICK_DISTANCE = 15
    }
}
