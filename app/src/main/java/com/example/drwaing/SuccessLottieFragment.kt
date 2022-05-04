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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.databinding.FragmentSuccessLottieBinding
import com.example.drwaing.extension.viewBinding
import com.example.drwaing.ui.main.MainActivity


class SuccessLottieFragment : Fragment(R.layout.fragment_success_lottie) {

    private val binding by viewBinding(FragmentSuccessLottieBinding::bind)

    companion object {
        val login: String = "Login"
        val making: String = "Making"
        val from: String = "WhereIFrom"
    }


    private val mainNavController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_main_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.fragmentSuccessLottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                if (arguments?.getString(from) == login) {
                    binding.fragmentSuccessText.setText("회원가입이 완료되었어요")
                }
            }

            override fun onAnimationEnd(p0: Animator?) {
                when (arguments?.getString(from)) {
                    login -> {
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        activity?.finish()
                    }
                    making -> { //한번에 mainFragment로 이동
                        mainNavController.backQueue.removeLastOrNull()
                        mainNavController.popBackStack()
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
