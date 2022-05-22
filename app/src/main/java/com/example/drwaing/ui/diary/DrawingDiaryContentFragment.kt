package com.example.drwaing.ui.diary

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentDrawingDiaryContentBinding
import com.example.drwaing.extension.dp
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.view.draw.ColorPickerAdapter
import com.example.drwaing.view.draw.DrawingView

class DrawingDiaryContentFragment : Fragment(R.layout.fragment_drawing_diary_content) {

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
        binding.drawer.setCurrentBitmap(viewModel.bitmap.value)
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

        binding.fragmentDrawingBack.setOnClickListener {
            //TODO: dialog
            navController.popBackStack()
        }
        binding.fragmentDrawingOkay.setOnClickListener {
            //TODO: Bundle
            viewModel.saveBitmap(binding.drawer.getBitmap())
            navController.popBackStack()
        }
    }

}