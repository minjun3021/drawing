package com.example.drwaing

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.databinding.FragmentSuccessLottieBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.diary.DiaryActivity
import com.example.drwaing.ui.main.MainActivity
import com.example.drwaing.ui.main.MainFragment
import com.example.drwaing.ui.main.MainViewModel


class SuccessLottieFragment : Fragment(R.layout.fragment_success_lottie) {

    private val binding by viewBinding(FragmentSuccessLottieBinding::bind)

    companion object {
        const val VIEW_LOGIN = "login"
        const val VIEW_REGISTER = "register"
        const val VIEW_MAKING = "making"
        const val WHERE_I_FROM = "from"
    }


    private val mainNavController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_main_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(arguments?.getString(WHERE_I_FROM)){
            VIEW_LOGIN -> binding.fragmentSuccessText.setText("로그인이 완료되었어요")
            VIEW_REGISTER -> binding.fragmentSuccessText.setText("회원가입이 완료되었어요")
            VIEW_MAKING -> binding.fragmentSuccessText.setText("오늘의 일기가 저장되었어요!")
        }


        binding.fragmentSuccessLottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                when (arguments?.getString(WHERE_I_FROM)) {
                    VIEW_LOGIN -> {

                        val intent = Intent(activity, MainActivity::class.java)
                            .putExtra("isFirstLogined", true)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        activity?.finish()
                    }
                    VIEW_REGISTER -> {
                        val intent = Intent(activity, MainActivity::class.java)
                            .putExtra("isFirstLogined", true)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        activity?.finish()
                    }
                    VIEW_MAKING -> {

                        MainFragment.afterMakingDiary = true
                        activity?.finish()
                    }
                }

            }


            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }

        })


    }
}
