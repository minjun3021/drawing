package com.kmj.ssgssg.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import com.kmj.ssgssg.R
import com.kmj.ssgssg.databinding.DialogConfirmBinding
import com.kmj.ssgssg.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : Dialog(context,R.style.Day_CustomDialog) {
    private lateinit var binding: DialogLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)



    }

    override fun onBackPressed() {
    }
}