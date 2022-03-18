package eg.iti.weather_app.view.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController

import androidx.navigation.ui.setupWithNavController
import eg.iti.weather_app.R
import kotlinx.android.synthetic.main.activity_weather.*


class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        bottomNavigationView.setupWithNavController(navFragment.findNavController())
    }


}