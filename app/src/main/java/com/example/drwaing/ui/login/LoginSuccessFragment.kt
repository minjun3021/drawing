package com.example.drwaing.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentLoginSuccessBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.main.MainActivity


class LoginSuccessFragment : Fragment(R.layout.fragment_login_success) {

    private val binding by viewBinding(FragmentLoginSuccessBinding::bind)

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