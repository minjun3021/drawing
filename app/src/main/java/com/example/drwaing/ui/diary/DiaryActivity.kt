package com.example.drwaing.ui.diary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }
}