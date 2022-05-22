package com.example.drwaing.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.example.drwaing.R
import com.example.drwaing.databinding.DialogConfirmBinding

class ConfirmDialog(
    context: Context,
    var title: String = "",
    var positiveText: String = "",
    var negativeText: String = "",
    var onPositiveClickListener: () -> Unit = {},
    var onNegativeClickListener: () -> Unit = {},
) : Dialog(context, R.style.Day_CustomDialog) {
    private lateinit var binding: DialogConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.tvTitle.text = title
        binding.tvPositive.text = positiveText
        binding.tvNegative.text = negativeText
        binding.tvPositive.setOnClickListener {
            onPositiveClickListener.invoke()
            dismiss()
        }
        binding.tvNegative.setOnClickListener { onNegativeClickListener.invoke() }
    }
}