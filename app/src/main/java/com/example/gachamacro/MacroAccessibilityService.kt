package com.example.gachamacro

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class MacroAccessibilityService : AccessibilityService() {

    companion object {
        var instance: MacroAccessibilityService? = null
            private set
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    fun tap(relativeX: Float, relativeY: Float) {
        val dm = resources.displayMetrics
        val x = dm.widthPixels * relativeX
        val y = dm.heightPixels * relativeY
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 80))
            .build()
        dispatchGesture(gesture, null, null)
    }

    fun swipeRelative(
        startX: Float, startY: Float,
        endX: Float, endY: Float,
        durationMs: Long
    ) {
        val dm = resources.displayMetrics
        val path = Path().apply {
            moveTo(dm.widthPixels * startX, dm.heightPixels * startY)
            lineTo(dm.widthPixels * endX, dm.heightPixels * endY)
        }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, durationMs))
            .build()
        dispatchGesture(gesture, null, null)
    }
}
