package eg.iti.weather_app.view.fragments.alarm

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eg.iti.weather_app.R

import eg.iti.weather_app.databinding.FragmentAlertBinding


class AlarmFragment : Fragment(R.layout.fragment_alert) {

    lateinit var binding: FragmentAlertBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {

        binding = FragmentAlertBinding.inflate(layoutInflater)
        return binding.root
    }

}