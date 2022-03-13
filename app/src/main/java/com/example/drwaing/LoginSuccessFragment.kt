package com.example.drwaing

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drwaing.databinding.FragmentLoginSuccessBinding


class LoginSuccessFragment : Fragment() {

    private var mBinding : FragmentLoginSuccessBinding? =null
    private val binding get()=mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding=FragmentLoginSuccessBinding.inflate(inflater,container,false)

        Handler().postDelayed({
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            activity?.finish()

        },1500)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding=null
    }
}