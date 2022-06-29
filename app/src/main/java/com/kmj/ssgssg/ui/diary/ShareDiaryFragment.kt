package com.kmj.ssgssg.ui.diary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.kmj.ssgssg.R
import com.kmj.ssgssg.data.diary.Weather
import com.kmj.ssgssg.databinding.FragmentShareDiaryBinding
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.main.MainFragment
import com.kmj.ssgssg.ui.stamp.StampData
import java.io.ByteArrayOutputStream
import java.util.ArrayList


class ShareDiaryFragment : Fragment(R.layout.fragment_share_diary) {
    private val binding by viewBinding(FragmentShareDiaryBinding::bind)
    private val viewModel: DrawingViewModel by activityViewModels()
    var list: ArrayList<StampData> = ArrayList()



    companion object{
        var isUploaded=false
        var path :String =""
    }
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

    }

    fun initView() {

        binding.fragmentShareDate.setTypeface(MainFragment.typeface)
        binding.fragmentShareContent.setTypeface(MainFragment.typeface)


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

        binding.fragmentShareLottie.playAnimation()


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
            binding.fragmentShareStampsController.post { doStamp() }
        }
        binding.fragmentShareToinstagram.setOnClickListener {
            binding.fragmentShareLottie.visibility = View.GONE

            viewToBitmap(binding.shareView)

        }
    }


    fun submitStampData() {
        list.add(StampData("GRAL", R.drawable.ic_stamp_gral, R.drawable.ic_stamp96_gral))
        list.add(StampData("DOTHIS", R.drawable.ic_stamp_dothis, R.drawable.ic_stamp96_dothis))
        list.add(StampData("GOOD", R.drawable.ic_stamp_good, R.drawable.ic_stamp96_good))
        list.add(StampData("GREATJOB",
            R.drawable.ic_stamp_greatjob,
            R.drawable.ic_stamp96_greatjob))
        list.add(StampData("PERFECT",
            R.drawable.ic_stamp_perfect,
            R.drawable.ic_stamp96_perfect))
        //TODO 얘네들이 들어가면 오류뜸 왜그런진 모르겟음
//        list.add(StampData("OH",R.drawable.ic_stamp_oh,R.drawable.ic_stamp96_oh))
//        list.add(StampData("ZZUGUL",R.drawable.ic_stamp_zzugul,R.drawable.ic_stamp96_zzugul))
        list.add(StampData("HUNDRED",
            R.drawable.ic_stamp_hundred,
            R.drawable.ic_stamp96_hundred))
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
            Weather.CLOUD.name -> resource = R.drawable.ic_cloudy_selected
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

                stamp.x =
                    (haveToStamp.x * binding.fragmentShareStampsController.width).toFloat()
                stamp.y =
                    (haveToStamp.y * binding.fragmentShareStampsController.height).toFloat()

                if (stamp.x >= binding.fragmentShareStampsController.width - pixels) {
                    stamp.x = binding.fragmentShareStampsController.width - pixels
                }
                if (stamp.y >= binding.fragmentShareStampsController.height - pixels) {
                    stamp.y = binding.fragmentShareStampsController.height - pixels
                }

                binding.fragmentShareStampsController.addView(stamp)
                stamp.updateLayoutParams {

                    height = pixels.toInt()
                    width = pixels.toInt()


                }


            }
        }








    }

    override fun onResume() {
        if(isUploaded){
            requireContext().getContentResolver().delete(Uri.parse(path),null, null)
            isUploaded=false

        }
        super.onResume()
    }

    fun viewToBitmap(view: View) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        getImageUri(requireContext(),bitmap)
    }
    fun shareToInsta(uri: Uri){
        val stickerAssetUri: Uri = uri
        val sourceApplication = "com.example.drwaing"

        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        intent.putExtra("source_application", sourceApplication)
        intent.setType("image/jpeg")
        intent.putExtra("interactive_asset_uri", stickerAssetUri)
        intent.putExtra("top_background_color", "#EDEDED")
        intent.putExtra("bottom_background_color", "#EDEDED")

        val activity: Activity? = activity
        activity!!.grantUriPermission("com.instagram.android",
            stickerAssetUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION)
        isUploaded=true
        if (activity!!.packageManager.resolveActivity(intent, 0) != null) {
            activity!!.startActivityForResult(intent, 0)

        }
        else{
            val sharingIntent = Intent(Intent.ACTION_SEND);
            val screenshotUri = Uri.parse(path);
            sharingIntent.setType("image/jpeg");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            startActivity(Intent.createChooser(sharingIntent, "쓱쓱 일기 공유"))
        }
    }
    fun getImageUri(context: Context, inImage: Bitmap) {
        val bytes = ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
            inImage,
            "Title",
            null)

        shareToInsta(Uri.parse(path))
    }



}
