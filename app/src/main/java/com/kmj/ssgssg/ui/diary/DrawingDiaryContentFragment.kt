package com.kmj.ssgssg.ui.diary

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmj.ssgssg.R
import com.kmj.ssgssg.databinding.FragmentDrawingDiaryContentBinding
import com.kmj.ssgssg.extension.dp
import com.kmj.ssgssg.extension.showDialog
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.view.draw.ColorPickerAdapter
import com.kmj.ssgssg.view.draw.DrawingView

class DrawingDiaryContentFragment : Fragment(R.layout.fragment_drawing_diary_content) {
    private lateinit var callback: OnBackPressedCallback
    private val binding by viewBinding(FragmentDrawingDiaryContentBinding::bind)

    private val viewModel: DrawingViewModel by activityViewModels()

    private val navController : NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_diary_host
        )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {

        binding.drawer.loadBitmap(viewModel.bitmap.value)
        binding.sbSizePicker.apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    binding.drawer.currentSize = (p1 + 10).dp
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }

        val adapter = ColorPickerAdapter {
            binding.drawer.currentColor = it
            binding.radioPencil.isChecked = true
        }

        binding.rvColorPicker.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = null
            this.adapter = adapter
        }

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.radio_eraser -> binding.drawer.drawMode = DrawingView.Mode.ERASE
                R.id.radio_pencil -> binding.drawer.drawMode = DrawingView.Mode.DRAW
            }
        }

        //TODO: onBackPressed

        binding.fragmentDrawingBack.setOnClickListener {
            context?.showDialog {
                title = "변경사항을 저장하지 않으시겠어요?"
                positiveText = "네"
                negativeText = "취소"
                onPositiveClickListener = {
                    navController.popBackStack()
                }
                onNegativeClickListener = {
                    dismiss()
                }
            }
        }
        binding.fragmentDrawingOkay.setOnClickListener {
            viewModel.saveBitmap(binding.drawer.getBitmap())
            navController.popBackStack()
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                context?.showDialog {
                    title = "변경사항을 저장하지 않으시겠어요?"
                    positiveText = "네"
                    negativeText = "취소"
                    onPositiveClickListener = {
                        navController.popBackStack()
                    }
                    onNegativeClickListener = {
                        dismiss()
                    }

                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}