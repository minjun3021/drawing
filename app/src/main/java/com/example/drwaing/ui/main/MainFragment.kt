package com.example.drwaing.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentMainBinding
import com.example.drwaing.extension.viewBinding
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

    //사용할때 lazy안에있는거라고 가르켜주는것임
    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_main_host
        )


    var drawingList: ArrayList<DrawingListData> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.e(navController.backQueue.size.toString(),"mainFragment")

        Log.e("onViewCreated","MainFragment")
        drawingList.add(DrawingListData.Header)
        for (i in 1..20) {
            drawingList.add(DrawingListData.DrawingData(i.toString(), "", "",Random().nextInt()))
        }



        binding.fragmentMainRecyclerview.apply { //apply 객체 반환함,객체의 속성을 건들일때 씀
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = drawingListAdapter.apply {
                submitList(drawingList)
            }

        }
        binding.fragmentMainDrawing.setOnClickListener{
            navController.navigate(R.id.action_mainFragment_to_makingDiaryFragment)
        }

        binding.fragmentMainPeople.setOnClickListener {

            var list: ArrayList<DrawingListData> = ArrayList()
            list.add(DrawingListData.Header)
            for (i in  1..20) {
                var randomNum : Int =Random().nextInt(4)+1
                list.add(DrawingListData.DrawingData(randomNum.toString(), "", "",randomNum))

            }

              drawingListAdapter.submitList(list)

        }



    }


}

