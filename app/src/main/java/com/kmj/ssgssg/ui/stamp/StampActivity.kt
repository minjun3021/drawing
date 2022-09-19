package com.kmj.ssgssg.ui.stamp

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kmj.ssgssg.R
import com.kmj.ssgssg.base.LoadingDialog
import com.kmj.ssgssg.data.diary.Weather
import com.kmj.ssgssg.databinding.ActivityLoginBinding
import com.kmj.ssgssg.databinding.ActivityStampBinding
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.diary.DiaryActivity
import com.kmj.ssgssg.ui.main.MainFragment
import java.util.*
import kotlin.collections.ArrayList


class StampActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityStampBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

}