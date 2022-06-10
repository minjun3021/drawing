package com.example.drwaing.ui.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentDrawingDiaryContentBinding
import com.example.drwaing.databinding.FragmentSetFontBinding
import com.example.drwaing.extension.viewBinding

class SetFontFragment : Fragment(R.layout.fragment_set_font), View.OnClickListener {
    private val binding by viewBinding(FragmentSetFontBinding::bind)

    lateinit var  lastChecked : ImageView
    lateinit var sharedPref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    private val navController : NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_setting_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView(){
        sharedPref=requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        editor=sharedPref.edit()

        when(sharedPref.getInt("font",R.font.uhbeeseulvely2)){
            R.font.uhbeeseulvely2->{
                lastChecked = binding.setFontSelectedUhbeeseulvely2
                binding.setFontSelectedUhbeeseulvely2.visibility=View.VISIBLE
            }
            R.font.kyobohandwriting2019->{
                lastChecked = binding.setFontSelectedKyobohandwriting2019
                binding.setFontSelectedKyobohandwriting2019.visibility=View.VISIBLE
            }
            R.font.ssshinb7regular->{
                lastChecked = binding.setFontSelectedSsshinb7regular
                binding.setFontSelectedSsshinb7regular.visibility=View.VISIBLE
            }
            R.font.dovemayo->{
                lastChecked = binding.setFontSelectedDovemayo
                binding.setFontSelectedDovemayo.visibility=View.VISIBLE
            }
            R.font.uhbeenamsoyoung->{
                lastChecked = binding.setFontSelectedUhbeenamsoyoung
                binding.setFontSelectedUhbeenamsoyoung.visibility=View.VISIBLE
            }
            R.font.uhbeequeenj->{
                lastChecked = binding.setFontSelectedUhbeequeenj
                binding.setFontSelectedUhbeequeenj.visibility=View.VISIBLE
            }
            R.font.uhbeedongkyung->{
                lastChecked = binding.setFontSelectedUhbeedongkyung
                binding.setFontSelectedUhbeedongkyung.visibility=View.VISIBLE
            }
            R.font.uhbeepuding->{
                lastChecked = binding.setFontSelectedUhbeepuding
                binding.setFontSelectedUhbeepuding.visibility=View.VISIBLE
            }
            R.font.uhbeehambold->{
                lastChecked = binding.setFontSelectedUhbeehambold
                binding.setFontSelectedUhbeehambold.visibility=View.VISIBLE
            }

        }



        binding.setFontUhbeeseulvely2.setOnClickListener(this)
        binding.setFontKyobohandwriting2019.setOnClickListener(this)
        binding.setFontSsshinb7regular.setOnClickListener(this)
        binding.setFontDovemayo.setOnClickListener(this)
        binding.setFontUhbeenamsoyoung.setOnClickListener(this)
        binding.setFontUhbeequeenj.setOnClickListener(this)
        binding.setFontUhbeedongkyung.setOnClickListener(this)
        binding.setFontUhbeepuding.setOnClickListener(this)
        binding.setFontUhbeehambold.setOnClickListener(this)
        binding.fragmentFontBack.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        lastChecked.visibility=View.INVISIBLE

        when(v?.id){
            R.id.set_font_uhbeeseulvely2->{
                lastChecked=binding.setFontSelectedUhbeeseulvely2
                binding.setFontSelectedUhbeeseulvely2.visibility=View.VISIBLE
                editor.putInt("font",R.font.uhbeeseulvely2)

            }
            R.id.set_font_kyobohandwriting2019->{
                lastChecked=binding.setFontSelectedKyobohandwriting2019
                binding.setFontSelectedKyobohandwriting2019.visibility=View.VISIBLE
                editor.putInt("font",R.font.kyobohandwriting2019)
            }
            R.id.set_font_ssshinb7regular->{
                lastChecked=binding.setFontSelectedSsshinb7regular
                binding.setFontSelectedSsshinb7regular.visibility=View.VISIBLE
                editor.putInt("font",R.font.ssshinb7regular)

            }
            R.id.set_font_dovemayo->{
                lastChecked=binding.setFontSelectedDovemayo
                binding.setFontSelectedDovemayo.visibility=View.VISIBLE
                editor.putInt("font",R.font.dovemayo)

            }
            R.id.set_font_uhbeenamsoyoung->{
                lastChecked=binding.setFontSelectedUhbeenamsoyoung
                binding.setFontSelectedUhbeenamsoyoung.visibility=View.VISIBLE
                editor.putInt("font",R.font.uhbeenamsoyoung)
            }
            R.id.set_font_uhbeequeenj->{
                lastChecked=binding.setFontSelectedUhbeequeenj
                binding.setFontSelectedUhbeequeenj.visibility=View.VISIBLE
                editor.putInt("font",R.font.uhbeequeenj)
            }
            R.id.set_font_uhbeedongkyung->{
                lastChecked=binding.setFontSelectedUhbeedongkyung
                binding.setFontSelectedUhbeedongkyung.visibility=View.VISIBLE
                editor.putInt("font",R.font.uhbeedongkyung)

            }
            R.id.set_font_uhbeepuding->{
                lastChecked=binding.setFontSelectedUhbeepuding
                binding.setFontSelectedUhbeepuding.visibility=View.VISIBLE
                editor.putInt("font",R.font.uhbeepuding)
            }
            R.id.set_font_uhbeehambold->{
                lastChecked=binding.setFontSelectedUhbeehambold
                binding.setFontSelectedUhbeehambold.visibility=View.VISIBLE
                editor.putInt("font",R.font.uhbeehambold)
            }
            R.id.fragment_font_back->
            {
                navController.popBackStack()
            }

        }
        editor.commit()

    }
}
