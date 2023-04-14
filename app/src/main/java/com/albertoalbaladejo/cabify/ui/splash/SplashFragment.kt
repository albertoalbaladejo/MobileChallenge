package com.albertoalbaladejo.cabify.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.albertoalbaladejo.cabify.R
import com.albertoalbaladejo.cabify.ui.view.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.hide()
        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        view.visibility = View.GONE

        activityScope.launch {
            delay(1500)
            val action =
                SplashFragmentDirections.actionSplashFragmentToProductFragment()
            findNavController().navigate(action)
            view.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}