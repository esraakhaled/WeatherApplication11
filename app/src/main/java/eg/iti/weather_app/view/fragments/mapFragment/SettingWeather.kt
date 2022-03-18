package eg.iti.weather_app.view.fragments.mapFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import eg.iti.weather_app.R
import eg.iti.weather_app.databinding.FragmentSettingWeatherBinding
import eg.iti.weather_app.db.Resource
import eg.iti.weather_app.utils.getAddressGeocoder
import eg.iti.weather_app.utils.setLocale
import eg.iti.weather_app.viewmodel.FavViewModel
import eg.iti.weather_app.viewmodel.SettingViewModel
import eg.iti.weather_app.viewmodel.WeatherViewModel

class SettingWeather : Fragment(R.layout.fragment_setting_weather) {

    lateinit var sp: SharedPreferences
    lateinit var binding: FragmentSettingWeatherBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var favViewModel: FavViewModel
    lateinit var model: SettingViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ):
            View? {

        sp= PreferenceManager.getDefaultSharedPreferences(context)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        model = ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)

        binding = FragmentSettingWeatherBinding.inflate(layoutInflater)

        context?.let {
            model.writeDataWeatherInSharedPreference(model.getLatData().value.toString(),
                model.getLonData().value.toString(), model.getAddressData().value.toString(),
                it)
        }


        //data on TextViews
        model.getAddressData().observe(viewLifecycleOwner, Observer {
            binding.tvAddressLocation.text = it.toString()
        })
        model.getLatData().observe(viewLifecycleOwner, Observer {
            binding.tvLat.text = it.toString()
        })
        model.getLonData().observe(viewLifecycleOwner, Observer {
            binding.tvLon.text = it.toString()
        })


        binding = FragmentSettingWeatherBinding.inflate(layoutInflater)
        val root = binding.root

        binding.groubLocation.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.btnGps) {
                getLastKnownLocation()
            } else if (checkedId == R.id.btnLocation) {
                loadFragment(MapsFragment())
            }
        }


        binding.groubTemp.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.btnCelicious) {
                context?.let { model.writeTempDegreeInSharedPreference("metric", "°C","metric","m/s", it) }
            } else if (checkedId == R.id.btnKelven) {
                context?.let { model.writeTempDegreeInSharedPreference("imperial", "°F","imperial","Km/h", it) }

            }
        }

        binding.groubWind.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.btnMs) {
                context?.let { model.writeTempDegreeInSharedPreference("metric", "°C","metric","m/s", it) }
            } else if (checkedId == R.id.btnKmh) {
                context?.let { model.writeTempDegreeInSharedPreference("imperial", "°F","imperial","Km/h", it) }
            }
        }

        binding.groubLang.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.btnEnglish) {
                context?.let { model.writeLanguageInSharedPreference("en", it) }
                setLocale(context as Activity, "en")

            } else if (checkedId == R.id.btnArabic) {
                context?.let { model.writeLanguageInSharedPreference("ar", it) }
                setLocale(context as Activity, "ar")
            }
        }

        binding.fbtnLocation.setOnClickListener {
            if (binding.tvLat.text == null || binding.tvLon.text == null) {
                Toast.makeText(context, R.string.toastLoc, Toast.LENGTH_LONG).show()
            } else {

                context?.let {
                    weatherViewModel.getWeatherAPIData(it,
                        sp.getString("lat","").toString(),
                        sp.getString("lon","").toString(),
                        model.getTempData().value.toString(),
                        model.getLanguageData().value.toString())
                }
                weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
                            //showErrorMessage(it.message)
                        }
                    }
                })


            }

        }

        binding.checkFav.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){

                context?.let {
                    favViewModel.getFavAPIData(it,
                        sp.getString("lat","").toString(),
                        sp.getString("lon","").toString(),
                        model.getTempData().value.toString(),
                        model.getLanguageData().value.toString())
                }
                favViewModel.favLiveData.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
                            //showErrorMessage(it.message)
                        }
                    }
                })
            }
        }

        return root
    }

    private fun loadFragment(fragment: Fragment) {
        val fm = fragmentManager
        val fragmentTransaction: FragmentTransaction = fm!!.beginTransaction()
        fragmentTransaction.replace(R.id.relative, fragment)
        fragmentTransaction.commit() // save the changes
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(it,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                model.setLatData(location.latitude)
                model.setLonData(location.longitude)
                val title = getAddressGeocoder(location.latitude, location.longitude, context)
                model.setAddressData(title)
                Toast.makeText(context, "${location.latitude}", Toast.LENGTH_LONG).show()
            }
        }

    }

}