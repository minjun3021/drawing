package com.example.drwaing.ui.diary

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.drwaing.R
import com.example.drwaing.databinding.ActivityDiaryBinding
import com.example.drwaing.extension.viewBinding

class DiaryActivity : AppCompatActivity() {

    private val binding: ActivityDiaryBinding by viewBinding(ActivityDiaryBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    companion object {
        const val VIEW_TYPE_NEW = 0
        const val VIEW_TYPE_EDIT = 1
        const val VIEW_TYPE_VIEW = 2


        const val EXTRA_VIEW_TYPE = "view_type" // 만들기인지 보러 들어온건지  "EDIT" or "VIEW"
        const val EXTRA_DIARY_KEY = "diary_key" // 일기 키 값 viewType == "VIEW" 일때 서버에서 조회

        fun createIntent(
            context: Context,
            viewType: Int,
            diaryKey: Int = VIEW_TYPE_NEW,
        ) = Intent(context, DiaryActivity::class.java)
            .putExtra(EXTRA_VIEW_TYPE, viewType)
            .putExtra(EXTRA_DIARY_KEY, diaryKey)

        fun createBitmap(context: Context, width: Int, lineHeight: Int): Drawable {
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bg_diary_underline_image)
            val result = Bitmap.createBitmap(width, lineHeight, bitmap.config)
            val canvas = Canvas(result)
            canvas.drawBitmap(bitmap, 0f, lineHeight.toFloat() - bitmap.height, null)
            val drawable = BitmapDrawable(context.resources, result)
            drawable.tileModeX = Shader.TileMode.REPEAT
            drawable.tileModeY = Shader.TileMode.REPEAT
            return drawable
        }
    }
}