package com.kmj.ssgssg.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.kmj.ssgssg.R
import com.kmj.ssgssg.base.LoadingDialog
import com.kmj.ssgssg.base.StampDialog
import com.kmj.ssgssg.databinding.FragmentMainBinding
import com.kmj.ssgssg.extension.viewBinding
import com.kmj.ssgssg.ui.diary.DiaryActivity
import com.kmj.ssgssg.ui.login.LoginActivity
import com.kmj.ssgssg.ui.setting.SettingActivity
import com.kmj.ssgssg.ui.stamp.StampActivity
import java.text.SimpleDateFormat
import java.util.*


/**
 * Fragment의 onViewCreated 와 onCreateView의 차이는 뭐일지 공부하기
 *
 *
 * */
//onCrerateView에서 view가 정상적으로 만들어져 반환값으로 넘어왔을때 fragment의 lifecycle이 생성됨
//view의 lifecycle이 initalize되었기 때문에 view의 초기값이나 리사이클러뷰는 onViewCreated에서 설정해주는게 좋음

class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val drawingListAdapter: DrawingListAdapter by lazy { DrawingListAdapter() }
    private val viewModel: MainViewModel by activityViewModels()
    private var stampDiaryId = -1

    //사용할때 lazy안에있는거라고 가르켜주는것임
    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_main_host
        )

    companion object {
        lateinit var token: String
        lateinit var typeface: Typeface
        var afterMakingDiary = false
        var needToLogout = false

        fun makeDirayDate(date: String): String {
            var tmp = date.substring(0, date.indexOf("T"))
            tmp = tmp.replace("-", ".")

            val dateFormat = SimpleDateFormat("yyyy.MM.dd")
            var d = dateFormat.parse(tmp)
            val calendar = Calendar.getInstance()
            calendar.time = d
            lateinit var dayOfWeek: String
            when (calendar.get(Calendar.DAY_OF_WEEK)) {
                1 -> dayOfWeek = "일"
                2 -> dayOfWeek = "월"
                3 -> dayOfWeek = "화"
                4 -> dayOfWeek = "수"
                5 -> dayOfWeek = "목"
                6 -> dayOfWeek = "금"
                7 -> dayOfWeek = "토"

            }
            tmp += " " + dayOfWeek + "요일"
            return tmp
        }


    }

    override fun onResume() {
        viewModel.getMyDiaryList()
        viewModel.getDiaryList()

        val sharedPref = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        val myFont = sharedPref.getInt("font", R.font.uhbeeseulvely2)
        val previousTypeface = typeface
        typeface = ResourcesCompat.getFont(requireContext(), myFont)!!
        if (previousTypeface != typeface) {
            binding.fragmentMainRecyclerview.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                //글꼴이 바뀌면 리사이클러 onbind가 다호출되게 layoutmanager를 다시 넣어준다.
            }
        }




        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        viewModel.getMyDiaryList()
        viewModel.getNewStampedDiary()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "")!!
        val myFont = sharedPref.getInt("font", R.font.uhbeeseulvely2)
        typeface = ResourcesCompat.getFont(requireContext(), myFont)!!


        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        Log.e("onViewCreated", "MainFragment")



        binding.fragmentMainRecyclerview.apply { //apply 객체 반환함,객체의 속성을 건들일때 씀
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = drawingListAdapter
        }


        binding.fragmentMainNothingBtn.setOnClickListener {
            val intent = DiaryActivity.createIntent(
                requireContext(),
                DiaryActivity.VIEW_TYPE_NEW
            )
            startActivity(intent)
        }
        binding.fragmentMainDrawing.setOnClickListener {
            val intent = DiaryActivity.createIntent(
                requireContext(),
                DiaryActivity.VIEW_TYPE_NEW
            )
            startActivity(intent)
        }

        binding.fragmentMainPeople.setOnClickListener {
            val intent = Intent(context, StampActivity::class.java)
            startActivity(intent)


        }
        binding.fragmentMainSetting.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivityForResult(intent, 123)
        }
        viewModel.diaryList.observe(viewLifecycleOwner) {
            if (viewModel.diaryList.value!!.size >= 2) {
                binding.fragmentMainDidivedline.visibility=View.VISIBLE
                binding.fragmentMainNothing.visibility = View.GONE
                stampDiaryId =
                    ((viewModel.diaryList.value!!.get(viewModel.diaryList.value!!.size - 1)) as DrawingListData.Diary).diaryId
            }

            drawingListAdapter.submitList(viewModel.diaryList.value)


            if (afterMakingDiary) {
                val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
                smoothScroller.setTargetPosition(0);
                binding.fragmentMainRecyclerview.layoutManager!!.startSmoothScroll(smoothScroller);
                afterMakingDiary = false
            }

        }
        viewModel.newStampedDiary.observe(viewLifecycleOwner){
            if(it!!.size >0){
                StampDialog(requireContext(),
                    it!![it.size-1],
                    {
                        val intent = DiaryActivity.createIntent(
                        requireContext(),
                        DiaryActivity.VIEW_TYPE_VIEW,
                        it!![it.size-1].diaryId)
                        startActivity(intent)


                    },
                    {}).apply { show() }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 123 && MainFragment.needToLogout) {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
            needToLogout = false
        }


        super.onActivityResult(requestCode, resultCode, data)
    }


}
