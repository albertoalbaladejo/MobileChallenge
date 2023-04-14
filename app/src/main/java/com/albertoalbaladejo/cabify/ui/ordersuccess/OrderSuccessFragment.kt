package com.albertoalbaladejo.cabify.ui.ordersuccess

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.databinding.FragmentOrderSuccessBinding
import com.albertoalbaladejo.cabify.ui.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderSuccessFragment : Fragment() {

    private lateinit var binding: FragmentOrderSuccessBinding
    private lateinit var mainActivity: MainActivity

    private var isButtonPulsed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderSuccessBinding.inflate(layoutInflater)

        setViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideBottomNavigationBar()

        binding.backToHomeBtn.setOnClickListener {
            isButtonPulsed = true
            val action =
                OrderSuccessFragmentDirections.actionOrderSuccessFragmentToProductFragment()
            findNavController().navigate(action)
            mainActivity.showBottomNavigationBar()
        }
    }

    private fun setViews() {
        with(binding) {
            orderConstraintGroup.visibility = View.VISIBLE
            redirectHomeTimerTv.text =
                getString(R.string.redirect_home_timer_text, "5")
            countDownTimer.start()
        }
    }

    private val countDownTimer = object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val sec = millisUntilFinished / 1000
            binding.redirectHomeTimerTv.text =
                getString(R.string.redirect_home_timer_text, sec.toString())
        }

        override fun onFinish() {
            if (!isButtonPulsed) {
                val action =
                    OrderSuccessFragmentDirections.actionOrderSuccessFragmentToProductFragment()
                findNavController().navigate(action)
                mainActivity.showBottomNavigationBar()
            }
        }
    }
}