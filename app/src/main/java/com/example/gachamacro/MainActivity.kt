package com.example.gachamacro

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import java.io.InputStream

class MainActivity : Activity() {

    private lateinit var basePreview: ImageView
    private lateinit var status: TextView
    private var baseBitmap: Bitmap? = null
    private val templates = mutableListOf<Pair<String, Bitmap>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
    }

    private fun buildUi() {
        val scroll = ScrollView(this)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 28, 28, 28)
        }
        scroll.addView(root)

        root.addView(title("Gacha Macro"))
        root.addView(text("1차 프로토타입\n10칸 결과 분석 + 캐릭터 이미지 등록 + 조건 판정 + 터치/스와이프 기반 구조"))

        val baseBtn = Button(this).apply {
            text = "베이스 이미지 선택"
            setOnClickListener { pickBaseImage() }
        }
        root.addView(baseBtn)

        basePreview = ImageView(this).apply {
            adjustViewBounds = true
            setBackgroundColor(0xFFECEFF1.toInt())
        }
        root.addView(basePreview, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 420
        ))

        val addBtn = Button(this).apply {
            text = "캐릭터 이미지 추가"
            setOnClickListener { pickTemplateImage() }
        }
        root.addView(addBtn)

        val analyzeBtn = Button(this).apply {
            text = "10칸 이미지 분석"
            setOnClickListener { analyze() }
        }
        root.addView(analyzeBtn)

        root.addView(Button(this).apply {
            text = "접근성 서비스 설정 열기"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        })

        root.addView(Button(this).apply {
            text = "화면 캡처 권한 요청"
            setOnClickListener {
                val mgr = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                startActivityForResult(mgr.createScreenCaptureIntent(), 100)
            }
        })

        root.addView(Button(this).apply {
            text = "테스트 터치: 중앙"
            setOnClickListener {
                MacroAccessibilityService.instance?.tap(0.5f, 0.5f)
            }
        })

        root.addView(Button(this).apply {
            text = "테스트 스와이프: 왼쪽 → 오른쪽"
            setOnClickListener {
                MacroAccessibilityService.instance?.swipeRelative(0.2f, 0.5f, 0.8f, 0.5f, 500)
            }
        })

        status = text("상태: 대기")
        root.addView(status)

        root.addView(text(
            "사용 흐름\n" +
            "① 베이스 결과 화면 이미지를 선택\n" +
            "② 캐릭터 이미지를 여러 개 등록\n" +
            "③ 분석 실행\n" +
            "④ 각 캐릭터의 10칸 검출 개수 확인\n" +
            "⑤ 이후 매크로 조건과 실제 화면 캡처를 연결"
        ))

        setContentView(scroll)
    }

    private fun pickBaseImage() {
        startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }, 10)
    }

    private fun pickTemplateImage() {
        startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE
)
        }, 11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        val uri = data?.data ?: return
        val bitmap = contentResolver.openInputStream(uri)?.use(InputStream::readBytes)?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        } ?: return

        when (requestCode) {
            10 -> {
                baseBitmap = bitmap
                basePreview.setImageBitmap(bitmap)
                status.text = "상태: 베이스 이미지 선택됨 (${bitmap.width}×${bitmap.height})"
            }
            11 -> {
                val name = "캐릭터 ${templates.size + 1}"
                templates += name to bitmap
                status.text = "상태: $name 등록됨 (총 ${templates.size}개)"
            }
        }
    }

    private fun analyze() {
        val base = baseBitmap
        if (base == null) {
            toast("먼저 베이스 이미지를 선택하세요.")
            return
        }
        if (templates.isEmpty()) {
            toast("캐릭터 이미지를 하나 이상 등록하세요.")
            return
        }

        val slots = SlotAnalyzer.splitTenSlots(base)
        val lines = templates.map { (name, template) ->
            val count = slots.count { slot ->
                TemplateMatcher.similarity(slot, template) >= 0.72
            }
            "$name: $count개"
        }
        status.text = "분석 결과\n" + lines.joinToString("\n")
    }

    private fun title(s: String) = TextView(this).apply {
        text = s
        textSize = 28f
        setPadding(0, 0, 0, 18)
    }

    private fun text(s: String) = TextView(this).apply {
        text = s
        textSize = 16f
        setPadding(0, 8, 0, 16)
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}
