package com.kmj.ssgssg.ui.diary

import android.content.Context
import android.os.Bundle
import android.util.Log
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
    private var isvisible=false
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
        calcBtnState()
        binding.drawer.loadBitmap(viewModel.bitmap.value)

        binding.sbSizePicker.apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    binding.drawer.currentSize = (p1 + 5).dp
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
        binding.drawer.setOnClickListener {
            if (!isvisible){
                binding.forward.visibility=View.VISIBLE
                binding.backward.visibility=View.VISIBLE
                isvisible=true
            }
            calcBtnState()

        }

        binding.backward.setOnClickListener {
            binding.drawer.apply {
                if(modifedPointPosition!=-1) modifedPointPosition--
                calcBtnState()
                invalidate()
            }
        }

        binding.forward.setOnClickListener {

            binding.drawer.apply {
                if(modifedPointPosition!=points.size-1) modifedPointPosition++
                calcBtnState()
                invalidate()
            }


        }


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

    fun calcBtnState(){
        when(binding.drawer.modifedPointPosition)
        {
            -1->{
                binding.backward.setImageResource(R.drawable.ic_backward_disabled)
                binding.forward.setImageResource(R.drawable.ic_forward_enabled)
                }
            binding.drawer.points.size-1 ->{

                binding.backward.setImageResource(R.drawable.ic_backward_enabled)
                binding.forward.setImageResource(R.drawable.ic_forward_disabled)

            }
            else ->{
                binding.backward.setImageResource(R.drawable.ic_backward_enabled)
                binding.forward.setImageResource(R.drawable.ic_forward_enabled)
            }
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