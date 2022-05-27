package com.example.drwaing.ui.diary

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.NetworkInterface
import com.example.drwaing.R
import com.example.drwaing.SuccessLottieFragment
import com.example.drwaing.databinding.FragmentMakingDiaryBinding
import com.example.drwaing.extension.viewBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class MakingDiaryFragment : Fragment(R.layout.fragment_making_diary) {

    private val binding by viewBinding(FragmentMakingDiaryBinding::bind)
    private val viewModel: DrawingViewModel by activityViewModels()

    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_diary_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()

    }

    private fun initView() {
        when(activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_VIEW_TYPE)){
            0->{//new

                binding.fragmentMakingDrawing.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_drawingDiaryContentFragment)
                }
                binding.fragmentMakingContent.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
                }
                binding.fragmentMakingOkay.setOnClickListener {
                    //viewModel.save(part)

                    val file=bitmapToFile(viewModel.bitmap.value)
                    if (file != null) {
                        Log.e("changetofile","Asdf")
                        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                        val part = MultipartBody.Part.createFormData("image",System.currentTimeMillis().toString()+".jpeg",
                            file.asRequestBody("image/jpeg".toMediaTypeOrNull()))
                        com.example.drwaing.Network.api.uploadImage(part).enqueue(object :
                            retrofit2.Callback<NetworkInterface.ImageResponse> {
                            override fun onResponse(
                                call: Call<NetworkInterface.ImageResponse>,
                                response: Response<NetworkInterface.ImageResponse>,
                            ) {
                                if (response.code() == 200) {
                                    Log.e("uploadimage url", response.body()!!.responseMessage)
                                } else {
                                    Log.e("uploadimage anothercode", response.toString())
                                }

                            }

                            override fun onFailure(call: Call<NetworkInterface.ImageResponse>, t: Throwable) {
                                Log.e("uploadimage error", t.toString())
                            }

                        })

                    }

                    navController.navigate(
                        R.id.action_makingDiaryFragment_to_successLottieFragment,
                        bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_MAKING)
                    )
                }
            }
            1->{//edit 나중에 추가

            }
            2-> {//view
                binding.fragmentMakingOkay.visibility=View.GONE
                binding.fragmentMakingInstagram.visibility=View.VISIBLE

                activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_DIARY_KEY)

            }
        }
        binding.fragmentMakingBack.setOnClickListener {
            //TODO dialog 뛰우기
            activity?.finish()
        }

        binding.fragmentMakingContent.post{
            binding.fragmentMakingContent.background=DiaryActivity.createBitmap(requireContext(), binding.fragmentMakingContent.width, binding.fragmentMakingContent.lineHeight)
        }

    }

    private fun observe() {
        viewModel.diaryText.observe(viewLifecycleOwner) {
            binding.fragmentMakingContent.text = it.replace(" ", "\u00A0")
        }

        viewModel.bitmap.observe(viewLifecycleOwner) {
            binding.fragmentMakingDrawing.setImageBitmap(it)

        }


    }
    private fun bitmapToFile(bitmap: Bitmap?): File? {
        bitmap ?: return null
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${System.currentTimeMillis()}.jpeg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }
}