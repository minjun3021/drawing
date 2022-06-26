package com.example.drwaing.ui.main

import android.graphics.drawable.Drawable
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.drwaing.R
import com.example.drwaing.data.diary.Weather
import com.example.drwaing.databinding.ItemRecyclerDrawingBinding
import com.example.drwaing.databinding.ItemRecyclerFirstTitleBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.diary.DiaryActivity
import kotlin.math.log
import com.example.drwaing.view.draw.UIKit.getResources


/**
 * ListAdapter + DiffUtil 공부하기
 * DiffUtil.ItemCallback에 있는 두가지 메소드 (itemSame/contentsSame) 차이 공부하기 면접에서 많이 물어봄
 *
 * ViewType 공부하기
 */

//기존의 recyclerview adapter에서 데이터셋이 변경되었을때 notifydatasetchanged()로 알려주곤하는데
//그러면 하나의 데이터만 바꼈을 경우라도 전체다 바뀌어 불필요한 작업이이루어지는데
//Diffutil은 바뀐 position만 recyclerview에게 알려주어서 데이터를 변경한다
//itemSame은 같은 포지션에 있는 데이터를 비교했을때 동일한 아이템이 아닐때(false) 데이터 변경이 필요하므로 recyclerview에게 알려주고
//아이템이 동일하더라도(true) 안에 데이터가 다를수도있으므로 true일때 contentsSame 메서드가 실행된다.

class DrawingListAdapter :
    ListAdapter<DrawingListData, RecyclerView.ViewHolder>(diffutil) {
    /*
    처음에는 data를 관리하기위하여 arraylist를 만들었지만
    ListAdapter + Diffutil은 아이템리스트를 알아서 관리하기때문에 선언이 필요없다
    getItem은 ListAdapter의 매서드이다.

    이것들은 필요가 없게 되었다.

    private var drawingList: ArrayList<DrawingListData> = ArrayList()
    override fun getItemCount(): Int {
        Log.e(drawingList.size.toString(),"asdf")
        return drawingList.size
    }*/


    //class DrawingListAdapter가 메모리에 올리갈때 같이 만들어지는 객체를 companion object라함
    companion object {
        val diffutil = object : DiffUtil.ItemCallback<DrawingListData>() {

            override fun areItemsTheSame(
                oldItem: DrawingListData,
                newItem: DrawingListData,
            ): Boolean {

                if (oldItem is DrawingListData.Header && newItem is DrawingListData.Header)
                    return true
                else if (oldItem is DrawingListData.Diary && newItem is DrawingListData.Diary) {
                    return oldItem.diaryId == newItem.diaryId
                } else return false

            }

            override fun areContentsTheSame(
                oldItem: DrawingListData,
                newItem: DrawingListData,
            ): Boolean =
                (oldItem is DrawingListData.Diary && newItem is DrawingListData.Diary
                        && oldItem.imageUrl == newItem.imageUrl)


        }
    }


    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is DrawingListData.Diary -> 0
        is DrawingListData.Header -> 1

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            // TODO : Header xml 구현
            1 -> HeaderViewHolder(
                ItemRecyclerFirstTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> DiaryViewHolder(
                ItemRecyclerDrawingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var item = getItem(position)
        when (holder) {
            is DiaryViewHolder -> {
                holder.onBindView(item as DrawingListData.Diary)

            }

            is HeaderViewHolder -> {
                holder.onBindView(item as DrawingListData.Header)

            }
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<DrawingListData>,
        currentList: MutableList<DrawingListData>,
    ) {
        Log.e("onCurrentListChanged", "adapter")
        super.onCurrentListChanged(previousList, currentList)
    }

    inner class DiaryViewHolder(val binding: ItemRecyclerDrawingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {

                val intent = DiaryActivity.createIntent(
                    binding.root.context,
                    DiaryActivity.VIEW_TYPE_VIEW,
                    (currentList.get(adapterPosition) as DrawingListData.Diary).diaryId
                )
                binding.root.context.startActivity(intent)
            }
        }

        fun onBindView(item: DrawingListData.Diary) {
            Glide.with(binding.root).load(item.imageUrl)
                .transform(CenterCrop())
                .into(binding.recylerItemDrawing)
            binding.recylerItemDrawingDate.setText(MainFragment.makeDirayDate(item.createdDate))

            lateinit var weatherDrawable: Drawable
            when (item.weather) {
                Weather.SUN.name -> weatherDrawable =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.ic_sunny_selected)!!
                Weather.CLOUD.name -> weatherDrawable =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.ic_cloudy_selected)!!
                Weather.RAIN.name -> weatherDrawable =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.ic_rainy_selected)!!
                Weather.SNOW.name -> weatherDrawable =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.ic_snowy_selected)!!
            }
            binding.recylerItemDrawingWeather.setImageDrawable(weatherDrawable)

        }
    }

    class HeaderViewHolder(val binding: ItemRecyclerFirstTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(item: DrawingListData.Header) {

        }
    }
}

