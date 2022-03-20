package eg.iti.weather_app.view.fragments.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import eg.iti.weather_app.R
import eg.iti.weather_app.databinding.FragmentHomeWeatherBinding
import eg.iti.weather_app.db.Resource
import eg.iti.weather_app.db.entity.Weather_Response
import eg.iti.weather_app.db.model.Hourly
import eg.iti.weather_app.utils.dayConverter
import eg.iti.weather_app.utils.setImage
import eg.iti.weather_app.utils.timeConverter
import eg.iti.weather_app.viewmodel.SettingViewModel
import eg.iti.weather_app.viewmodel.WeatherViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_weather.*

import java.util.*


class HomeWeather : Fragment(R.layout.fragment_home_weather) {
    lateinit var binding: FragmentHomeWeatherBinding
    private var hourAdapter: RecyclerView.Adapter<HourlyAdapter.ForecatViewHolder>? = null
    private var layoutManag: RecyclerView.LayoutManager? = null
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var sp: SharedPreferences
    var values: String = ""
    lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        calendar = Calendar.getInstance()

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        binding = FragmentHomeWeatherBinding.inflate(layoutInflater)
        val root = binding.root
        binding.recyclerViewCurrent.isEnabled = false
        context?.let {
            weatherViewModel.getWeatherAPIData(it)
        }

        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    binding.mainContainer.visibility = View.VISIBLE
                    hideProgressBar()
                    it.data?.let {
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })

        weatherViewModel.checkRoom.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.cardHello.visibility = View.VISIBLE
            }
        })

        weatherViewModel.weatherFromRoomLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.mainContainer.visibility = View.VISIBLE
                    it.data?.let {
                        displayDailyWeatherToRecycleView(it.hourly)
                        displayCurrentWeatherToCard(it)
                    }
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    showErrorMessage(it.message)
                }
            }
        })

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = sharedPreferences.edit()
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        binding.tvAddress.text = sp.getString("address", "")
        binding.textCelcius.text = sp.getString("cel", "")
        binding.textWind.text = sp.getString("km", "")


        return root
    }

    private fun displayCurrentWeatherToCard(it1: Weather_Response) {

        binding.tvTemp.text = it1.daily.get(0).temp.day.toString()
        binding.tvTempMax.text = it1.daily.get(0).temp.max.toString()
        binding.tvTempMin.text = it1.daily.get(0).temp.min.toString()
        binding.tvHumidity.text = Math.round(it1.current.humidity).toString()
        binding.tvUpdatedAt.text = dayConverter(it1.current.dt.toLong())
        binding.tvStatus.text = it1.current.weather.get(0).description
        binding.tvWind.text = Math.round(it1.current.windSpeed).toString()
        binding.tvPressure.text = Math.round(it1.current.pressure).toString()
        binding.tvSunrise.text = timeConverter(it1.current.sunrise.toLong())
        binding.tvSunset.text = timeConverter(it1.current.sunset.toLong())
        binding.tvVis.text = it1.current.visibility.toString()//mm
        val url = it1.current.weather.get(0).icon

        setImage(binding.imgCurrentItem, url)

    }


    @SuppressLint("WrongConstant")
    private fun initUI(data: List<Hourly>) {
        context?.let {
            var hourlyAd = HourlyAdapter(data)
            hourlyAd.setData(data, it)
            binding.recyclerViewCurrent.apply {
                layoutManag = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
                layoutManager = layoutManag
                hourAdapter = hourlyAd
                adapter = hourAdapter
            }
        }

    }

    fun displayDailyWeatherToRecycleView(data: List<Hourly>) {
        if (data != null) {
            initUI(data)
        }
    }

    private fun showErrorMessage(message: String?) {
        System.out.println("Error is: " + message)
        binding.progressBar.visibility = View.INVISIBLE
    }


    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

}

