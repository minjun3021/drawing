package com.example.drwaing.ui.diary

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.drwaing.R
import com.example.drwaing.data.diary.Weather
import com.example.drwaing.databinding.FragmentShareDiaryBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.main.MainFragment
import com.example.drwaing.ui.stamp.StampData
import java.util.ArrayList


class ShareDiaryFragment : Fragment(R.layout.fragment_share_diary) {
    private val binding by viewBinding(FragmentShareDiaryBinding::bind)
    private val viewModel: DrawingViewModel by activityViewModels()
    var list: ArrayList<StampData> = ArrayList()

    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_diary_host
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        submitStampData()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
        Log.e("check", "cehck")
        Log.e(binding.fragmentShareStampsController.width.toString(),
            binding.fragmentShareStampsController.height.toString())
//
//        val stickerAssetUri: Uri = Uri.parse(R.drawable.ic_rainy_enabled.toString())
//        val sourceApplication = "com.example.drwaing"
//
//// Instantiate implicit intent with ADD_TO_STORY action,
//// sticker asset, and background colors
//
//// Instantiate implicit intent with ADD_TO_STORY action,
//// sticker asset, and background colors
//        val intent = Intent("com.instagram.share.ADD_TO_STORY")
//        intent.putExtra("source_application", sourceApplication)
//
//        intent.setType("image/*")
//        intent.putExtra("interactive_asset_uri", stickerAssetUri)
//        intent.putExtra("top_background_color", "#EDEDED")
//        intent.putExtra("bottom_background_color", "#EDEDED")
//
//        val activity: Activity? = activity
//        activity!!.grantUriPermission("com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        if (activity!!.packageManager.resolveActivity(intent, 0) != null) {
//            activity!!.startActivityForResult(intent, 0)
    }

    fun initView() {
        binding.fragmentShareFake.post {
            binding.fragmentShareFake.background = DiaryActivity.createBitmap(requireContext(),
                binding.fragmentShareFake.width,
                binding.fragmentShareFake.lineHeight)
        }
        binding.fragmentShareContent.post {
            binding.fragmentShareContent.background = DiaryActivity.createBitmap(requireContext(),
                binding.fragmentShareContent.width,
                binding.fragmentShareContent.lineHeight)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity?.getWindow();
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window?.setStatusBarColor(Color.parseColor("#EDEDED"))

        }
    }

    private fun observe() {

        viewModel.diary.observe(viewLifecycleOwner) {
            binding.fragmentShareDate.text =
                MainFragment.makeDirayDate(viewModel.diary.value!!.createdDate)
            Glide.with(this)
                .load(viewModel.diary.value!!.imageUrl)
                .into(binding.fragmentShareDrawing)

            binding.fragmentShareContent.text = viewModel.diary.value!!.content
            setIcon()
            binding.fragmentShareStampsController.post{doStamp() }
        }


    }

    fun submitStampData() {
        list.add(StampData("GRAL", R.drawable.ic_stamp_gral, R.drawable.ic_stamp96_gral))
        list.add(StampData("DOTHIS", R.drawable.ic_stamp_dothis, R.drawable.ic_stamp96_dothis))
        list.add(StampData("GOOD", R.drawable.ic_stamp_good, R.drawable.ic_stamp96_good))
        list.add(StampData("GREATJOB",
            R.drawable.ic_stamp_greatjob,
            R.drawable.ic_stamp96_greatjob))
        list.add(StampData("PERFECT", R.drawable.ic_stamp_perfect, R.drawable.ic_stamp96_perfect))
        //TODO 얘네들이 들어가면 오류뜸 왜그런진 모르겟음
//        list.add(StampData("OH",R.drawable.ic_stamp_oh,R.drawable.ic_stamp96_oh))
//        list.add(StampData("ZZUGUL",R.drawable.ic_stamp_zzugul,R.drawable.ic_stamp96_zzugul))
        list.add(StampData("HUNDRED", R.drawable.ic_stamp_hundred, R.drawable.ic_stamp96_hundred))
        list.add(StampData("HOENG", R.drawable.ic_stamp_hoeng, R.drawable.ic_stamp96_hoeng))
        list.add(StampData("INTERESTING",
            R.drawable.ic_stamp_interesting,
            R.drawable.ic_stamp96_interesting))
        list.add(StampData("LOL", R.drawable.ic_stamp_lol, R.drawable.ic_stamp96_lol))
        list.add(StampData("ZZANG", R.drawable.ic_stamp_zzang, R.drawable.ic_stamp96_zzang))
    }

    private fun setIcon() {
        var resource: Int = 0
        when (viewModel.diary.value!!.weather) {
            Weather.SUN.name -> resource = R.drawable.ic_sunny_selected
            Weather.CLOUD.name -> resource = R.drawable.ic_sunny_selected
            Weather.RAIN.name -> resource = R.drawable.ic_rainy_selected
            Weather.SNOW.name -> resource = R.drawable.ic_snowy_selected
        }

        binding.fragmentShareWeather.setImageDrawable(ContextCompat.getDrawable(
            requireContext(),
            resource))
    }

    fun doStamp() {

        if (viewModel.diary.value?.stampList?.size != 0) {
            val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                64f,
                requireContext().resources.displayMetrics)

            for (i in 0..viewModel.diary.value?.stampList!!.size - 1) {

                val haveToStamp = viewModel.diary.value!!.stampList.get(i)
                Log.e(haveToStamp.x.toString(), haveToStamp.y.toString())

                var stamp = View.inflate(context, R.layout.stamp, null)
                for (i in 0..list.size - 1) {
                    if (list.get(i).stampTag == haveToStamp.stampType) {
                        stamp.setBackgroundResource(list.get(i).stamp96ID)
                        Log.e("check", list.get(i).stampTag)
                        break
                    }
                }

                stamp.x = (haveToStamp.x * binding.fragmentShareStampsController.width).toFloat()
                stamp.y = (haveToStamp.y * binding.fragmentShareStampsController.height).toFloat()

                    if(stamp.x>= binding.fragmentShareStampsController.width-pixels){
                        stamp.x=binding.fragmentShareStampsController.width-pixels
                    }
                    if(stamp.y>= binding.fragmentShareStampsController.height-pixels){
                        stamp.y=binding.fragmentShareStampsController.height-pixels
                    }

                binding.fragmentShareStampsController.addView(stamp)
                stamp.updateLayoutParams {

                    height = pixels.toInt()
                    width = pixels.toInt()


                }


            }
        }
    }
}
