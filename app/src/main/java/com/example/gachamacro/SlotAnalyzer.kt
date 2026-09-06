package com.example.gachamacro

import android.graphics.Bitmap

object SlotAnalyzer {
    fun splitTenSlots(base: Bitmap): List<Bitmap> {
        val marginLeft = (base.width * 0.04f).toInt()
        val marginRight = (base.width * 0.04f).toInt()
        val top = (base.height * 0.08f).toInt()
        val bottom = (base.height * 0.92f).toInt()
        val usableWidth = base.width - marginLeft - marginRight
        val slotWidth = usableWidth / 10

        return (0 until 10).map { i ->
            val left = marginLeft + i * slotWidth
            val right = if (i == 9) base.width - marginRight else left + slotWidth
            Bitmap.createBitmap(base, left, top, (right - left).coerceAtLeast(1), (bottom - top).coerceAtLeast(1))
        }
    }
}
