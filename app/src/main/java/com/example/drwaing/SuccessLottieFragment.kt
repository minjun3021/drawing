package com.example.drwaing

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drwaing.databinding.FragmentSuccessLottieBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.main.MainActivity


class SuccessLottieFragment : Fragment(R.layout.fragment_success_lottie) {

    private val binding by viewBinding(FragmentSuccessLottieBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO : 로띠 전달받아서 Lottie 재생이 끝나면 화면 끄도록 변경
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                val intent = Intent(activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                activity?.finish()
            }
        }, 1500)
    }
}
