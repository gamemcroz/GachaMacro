package com.example.gachamacro

import android.graphics.Bitmap
import kotlin.math.abs
import kotlin.math.min

object TemplateMatcher {
    /*
     * 1차 프로토타입용 경량 템플릿 매칭.
     * 실제 게임 화면에서는 카드 내부의 캐릭터 영역만 비교하도록
     * ROI(관심영역)를 추가하고, 여러 크기/밝기 보정 단계를 적용하는 것이 좋습니다.
     */
    fun similarity(slot: Bitmap, template: Bitmap): Double {
        val w = 32
        val h = 48
        val a = Bitmap.createScaledBitmap(slot, w, h, true)
        val b = Bitmap.createScaledBitmap(template, w, h, true)

        var diff = 0L
        var maxDiff = 0L
        for (y in 0 until h) {
            for (x in 0 until w) {
                val pa = a.getPixel(x, y)
                val pb = b.getPixel(x, y)
                val dr = abs(((pa shr 16) and 255) - ((pb shr 16) and 255))
                val dg = abs(((pa shr 8) and 255) - ((pb shr 8) and 255))
                val db = abs((pa and 255) - (pb and 255))
                diff += dr + dg + db
                maxDiff += 765
            }
        }
        return 1.0 - min(1.0, diff.toDouble() / maxDiff.toDouble())
    }
}
