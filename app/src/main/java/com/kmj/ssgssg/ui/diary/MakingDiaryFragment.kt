package com.kmj.ssgssg.ui.diary

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kmj.ssgssg.R
import com.kmj.ssgssg.SuccessLottieFragment
import com.kmj.ssgssg.base.LoadingDialog
import com.kmj.ssgssg.data.diary.Weather
import com.kmj.ssgssg.databinding.FragmentMakingDiaryBinding
import com.kmj.ssgssg.databinding.FragmentShareDiaryBinding
import com.kmj.ssgssg.extension.showDialog
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.main.MainFragment
import com.kmj.ssgssg.ui.stamp.StampData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class MakingDiaryFragment : Fragment(R.layout.fragment_making_diary) {
    lateinit var loadingDialog: LoadingDialog
    private lateinit var callback: OnBackPressedCallback

    private val binding by viewBinding(FragmentMakingDiaryBinding::bind)

    var path: String = ""
    private val viewModel: DrawingViewModel by activityViewModels()
    private var readyToUploadDiary = false

    var list: ArrayList<StampData> = ArrayList()

    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_diary_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()


    }

    private fun initView() {


        binding.fragmentMakingDate.typeface = MainFragment.typeface
        binding.fragmentMakingContent.typeface = MainFragment.typeface
        binding.fragmentMakingDate.text = makeDate()


        when (activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_VIEW_TYPE)) {
            0 -> {//new

                val now = System.currentTimeMillis()
                val date = Date(now)
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val getTime: String = sdf.format(date) + "T"
                binding.fragmentMakingDate.text = MainFragment.makeDirayDate(getTime)

                binding.fragmentMakingDrawing.setOnClickListener {
                    navController.navigate(R.id.action_makingDiaryFragment_to_drawingDiaryContentFragment)
                }
//                binding.fragmentMakingContent.setOnClickListener {
//                    navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
//                }
//                binding.fragmentMakingFake.setOnClickListener {
//                    navController.navigate(R.id.action_makingDiaryFragment_to_typingDiaryContentFragment)
//                }
                binding.fragmentMakingOkay.setOnClickListener {
                    if (readyToUploadDiary) {
                        viewModel.diaryText.value=viewModel.diaryText.value?.replace(" ", "\u00A0") ?: ""
                        val file = bitmapToFile(viewModel.bitmap.value)
                        if (file != null) {
                            viewModel.save(file)
                            loadingDialog = LoadingDialog(requireContext()).apply { show() }

                        }

                    }
                }
                binding.fragmentMakingBack.setOnClickListener {
                    if (viewModel.bitmap.value == null && viewModel.diaryText.value.isNullOrEmpty()) {
                        activity?.finish()
                    } else {
                        context?.showDialog {
                            title = "작성중인 일기가 사라집니다"
                            positiveText = "네"
                            negativeText = "취소"
                            onPositiveClickListener = {
                                activity?.finish()
                            }
                            onNegativeClickListener = {
                                dismiss()
                            }

                        }
                    }
                }

                binding.fragmentMakingContent.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        viewModel.diaryText.value=binding.fragmentMakingContent.text.toString()

                    }
                })
                binding.fragmentMakingSun.setOnClickListener {
                    viewModel.setWeather(Weather.SUN)
                }
                binding.fragmentMakingCloud.setOnClickListener {
                    viewModel.setWeather(Weather.CLOUD)
                }
                binding.fragmentMakingRain.setOnClickListener {
                    viewModel.setWeather(Weather.RAIN)
                }

                binding.fragmentMakingSnow.setOnClickListener {
                    viewModel.setWeather(Weather.SNOW)
                }
                callback = object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (viewModel.bitmap.value == null && viewModel.diaryText.value.isNullOrEmpty()) {
                            activity?.finish()
                        } else {
                            context?.showDialog {
                                title = "작성중인 일기가 사라집니다"
                                positiveText = "네"
                                negativeText = "취소"
                                onPositiveClickListener = {
                                    activity?.finish()
                                }
                                onNegativeClickListener = {
                                    dismiss()
                                }

                            }
                        }
                    }
                }
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
                observeMaking()
            }
            1 -> {//edit 나중에 추가

            }
            2 -> {//view

                binding.fragmentMakingOkay.visibility = View.GONE
                binding.fragmentMakingInstagram.visibility = View.VISIBLE

                binding.fragmentMakingBack.setOnClickListener {
                    requireActivity().finish()
                }

                binding.fragmentMakingContent.apply {
                    isCursorVisible = false
                    isFocusable = false
                    isClickable = false
                    hint=""
                }
                submitStampData()
                observeViewing()
                viewModel.getDiary(activity?.intent?.extras?.getInt(DiaryActivity.EXTRA_DIARY_KEY)!!)


            }
        }





        binding.fragmentMakingFake.post {
            binding.fragmentMakingFake.background = DiaryActivity.createBitmap(
                requireContext(),
                binding.fragmentMakingContent.width,
                binding.fragmentMakingContent.lineHeight
            )
        }
        binding.fragmentMakingContent.post {
            binding.fragmentMakingContent.background = DiaryActivity.createBitmap(
                requireContext(),
                binding.fragmentMakingContent.width,
                binding.fragmentMakingContent.lineHeight
            )
        }

    }

    private fun observeMaking() {

        viewModel.bitmap.observe(viewLifecycleOwner) {

            checkDoEveryThing()
            binding.fragmentMakingDrawing.setImageBitmap(it)

        }
        viewModel.weather.observe(viewLifecycleOwner) {
            changeIcon()

        }

        viewModel.url.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(viewModel.url.value)
                .preload()
        }
        viewModel.timeToNavigate.observe(viewLifecycleOwner) {
            if (viewModel.timeToNavigate.value == true) {
                loadingDialog.cancel()
                navController.navigate(
                    R.id.action_makingDiaryFragment_to_successLottieFragment,
                    bundleOf(SuccessLottieFragment.WHERE_I_FROM to SuccessLottieFragment.VIEW_MAKING)
                )
            }
        }


    }

    private fun observeViewing() {
        viewModel.diary.observe(viewLifecycleOwner) {
            binding.fragmentMakingDate.text =
                MainFragment.makeDirayDate(viewModel.diary.value!!.createdDate)
            Glide.with(this)
                .load(viewModel.diary.value!!.imageUrl)
                .into(binding.fragmentMakingDrawing)

            binding.fragmentMakingContent.apply {
                setText(viewModel.diary.value?.content ?: "")

            }



            setIcon()

            if (viewModel.diary.value?.stampList?.size != 0) {
                val pixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    96f,
                    requireContext().resources.displayMetrics
                )

                for (i in 0..viewModel.diary.value?.stampList!!.size - 1) {

                    val haveToStamp = viewModel.diary.value!!.stampList.get(i)
                    var stamp = View.inflate(context, R.layout.stamp, null)
                    for (i in 0..list.size - 1) {
                        if (list.get(i).stampTag == haveToStamp.stampType) {
                            stamp.setBackgroundResource(list.get(i).stamp96ID)
                            break
                        }
                    }
                    stamp.x = (haveToStamp.x * binding.fragmentMainStampController.width).toFloat()
                    stamp.y = (haveToStamp.y * binding.fragmentMainStampController.height).toFloat()
                    if (stamp.x >= binding.fragmentMainStampController.width - pixels) {
                        stamp.x = binding.fragmentMainStampController.width - pixels
                    }
                    if (stamp.y >= binding.fragmentMainStampController.height - pixels) {
                        stamp.y = binding.fragmentMainStampController.height - pixels
                    }

                    binding.fragmentMainStampController.addView(stamp)
                    stamp.updateLayoutParams {

                        height = pixels.toInt()
                        width = pixels.toInt()


                    }


                }
            }

            binding.shareController.apply {

                fragmentShareDate.typeface = MainFragment.typeface
                fragmentShareContent.typeface = MainFragment.typeface
                var resource = when (viewModel.diary.value!!.weather) {
                    Weather.SUN.name -> R.drawable.ic_sunny_selected
                    Weather.CLOUD.name -> R.drawable.ic_cloudy_selected
                    Weather.RAIN.name -> R.drawable.ic_rainy_selected
                    Weather.SNOW.name -> R.drawable.ic_snowy_selected
                    else -> R.drawable.ic_sunny_selected
                }

                fragmentShareWeather.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        resource
                    )
                )


                fragmentShareContent.background =
                    DiaryActivity.createBitmap(
                        requireContext(),
                        fragmentShareContent.width,
                        fragmentShareContent.lineHeight
                    )


                fragmentShareDate.text =
                    MainFragment.makeDirayDate(viewModel.diary.value!!.createdDate)


                Glide.with(requireContext())
                    .load(viewModel.diary.value!!.imageUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            binding.fragmentMakingInstagram.setOnClickListener {
                                viewToBitmap(binding.shareController.shareView)

                            }
                            return false
                        }
                    })
                    .into(fragmentShareDrawing)

                fragmentShareContent.text = viewModel.diary.value!!.content

            }


        }


    }

    fun viewToBitmap(view: View) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        getImageUri(requireContext(), bitmap)
    }

    fun getImageUri(context: Context, inImage: Bitmap) {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        path = MediaStore.Images.Media.insertImage(context.contentResolver,  inImage, "Title", null)


        shareToInsta(Uri.parse(path))

    }

    fun shareToInsta(uri: Uri) {
        val stickerAssetUri: Uri = uri
        val sourceApplication = "com.example.drwaing"

        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        intent.putExtra("source_application", sourceApplication)
        intent.type = "image/jpeg"
        intent.putExtra("interactive_asset_uri", stickerAssetUri)
        intent.putExtra("top_background_color", "#EDEDED")
        intent.putExtra("bottom_background_color", "#EDEDED")

        val activity: Activity? = activity
        activity!!.grantUriPermission(
            "com.instagram.android",
            stickerAssetUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        if (activity.packageManager.resolveActivity(intent, 0) != null) {
            activity.startActivityForResult(intent, 0)

            Handler(Looper.getMainLooper()).postDelayed({
                context?.contentResolver?.delete(Uri.parse(path), null, null)

            }, 10000)


        } else {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            val screenshotUri = Uri.parse(path)
            sharingIntent.type = "image/jpeg"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
            startActivity(Intent.createChooser(sharingIntent, "쓱쓱 일기 공유"))
            Handler(Looper.getMainLooper()).postDelayed({
                context?.contentResolver?.delete(Uri.parse(path), null, null)

            }, 30000)
        }
    }

    private fun checkDoEveryThing() {
        if (viewModel.weather.value != null && viewModel.bitmap.value != null) {
            binding.fragmentMakingOkay.setTextColor(Color.parseColor("#111111"))
            readyToUploadDiary = true
        }
    }

    private fun changeIcon() {
        checkDoEveryThing()
        binding.fragmentMakingSun.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_sunny_enabled
            )
        )
        binding.fragmentMakingCloud.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_cloudy_enabled
            )
        )
        binding.fragmentMakingRain.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_rainy_enabled
            )
        )
        binding.fragmentMakingSnow.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_snowy_enabled
            )
        )

        when (viewModel.weather.value!!.name) {
            Weather.SUN.name -> binding.fragmentMakingSun.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_sunny_selected
                )
            )
            Weather.CLOUD.name -> binding.fragmentMakingCloud.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_cloudy_selected
                )
            )
            Weather.RAIN.name -> binding.fragmentMakingRain.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_rainy_selected
                )
            )
            Weather.SNOW.name -> binding.fragmentMakingSnow.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_snowy_selected
                )
            )
        }
    }

    private fun setIcon() {
        when (viewModel.diary.value!!.weather) {
            Weather.SUN.name -> binding.fragmentMakingSun.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_sunny_selected
                )
            )
            Weather.CLOUD.name -> binding.fragmentMakingCloud.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_cloudy_selected
                )
            )
            Weather.RAIN.name -> binding.fragmentMakingRain.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_rainy_selected
                )
            )
            Weather.SNOW.name -> binding.fragmentMakingSnow.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_snowy_selected
                )
            )
        }
    }

    fun submitStampData() {
        list.add(StampData("GRAL", R.drawable.ic_stamp_gral, R.drawable.ic_stamp96_gral))
        list.add(StampData("DOTHIS", R.drawable.ic_stamp_dothis, R.drawable.ic_stamp96_dothis))
        list.add(StampData("GOOD", R.drawable.ic_stamp_good, R.drawable.ic_stamp96_good))
        list.add(
            StampData(
                "GREATJOB",
                R.drawable.ic_stamp_greatjob,
                R.drawable.ic_stamp96_greatjob
            )
        )
        list.add(StampData("PERFECT", R.drawable.ic_stamp_perfect, R.drawable.ic_stamp96_perfect))

        list.add(StampData("OH", R.drawable.ic_stamp_oh, R.drawable.ic_stamp96_oh))
        list.add(StampData("ZZUGUL", R.drawable.ic_stamp_zzugul, R.drawable.ic_stamp96_zzugul))
        list.add(StampData("HUNDRED", R.drawable.ic_stamp_hundred, R.drawable.ic_stamp96_hundred))
        list.add(StampData("HOENG", R.drawable.ic_stamp_hoeng, R.drawable.ic_stamp96_hoeng))
        list.add(
            StampData(
                "INTERESTING",
                R.drawable.ic_stamp_interesting,
                R.drawable.ic_stamp96_interesting
            )
        )
        list.add(StampData("LOL", R.drawable.ic_stamp_lol, R.drawable.ic_stamp96_lol))
        list.add(StampData("ZZANG", R.drawable.ic_stamp_zzang, R.drawable.ic_stamp96_zzang))
    }

    private fun makeDate(): String {
        val date = Date()
        val mFormat = SimpleDateFormat("yyyy.MM.dd")
        var time: String = mFormat.format(date)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dayOfWeekNumber = calendar[Calendar.DAY_OF_WEEK]
        when (dayOfWeekNumber) {
            1 -> time += " 일요일"
            2 -> time += " 월요일"
            3 -> time += " 화요일"
            4 -> time += " 수요일"
            5 -> time += " 목요일"
            6 -> time += " 금요일"
            7 -> time += " 토요일"
        }
        return time
    }

    private fun bitmapToFile(bitmap: Bitmap?): File? {
        bitmap ?: return null
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("image", Context.MODE_PRIVATE)
        file = File(file, "${System.currentTimeMillis()}.jpg")

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


    override fun onDetach() {
        super.onDetach()
        if (::callback.isInitialized)
            callback.remove()
    }
}