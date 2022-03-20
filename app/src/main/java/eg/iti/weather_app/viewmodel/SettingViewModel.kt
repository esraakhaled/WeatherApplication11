package eg.iti.weather_app.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var SP: SharedPreferences
    val addressLiveData = MutableLiveData<String?>()
    val latLiveData = MutableLiveData<Double>()
    val lonLiveData = MutableLiveData<Double>()
    val tempLiveData = MutableLiveData<String>()
    val windLiveData = MutableLiveData<String>()
    val languageLiveData = MutableLiveData<String>()


    fun setAddressData(address: String?) {
        addressLiveData.postValue(address)
    }

    fun getAddressData(): MutableLiveData<String?> {
        return addressLiveData
    }


    fun setLatData(lat: Double) {
        latLiveData.postValue(lat)
    }

    fun getLatData(): MutableLiveData<Double> {
        return latLiveData
    }

    fun setLonData(lon: Double) {
        lonLiveData.postValue(lon)
    }

    fun getLonData(): MutableLiveData<Double> {
        return lonLiveData
    }


    fun getTempData(): MutableLiveData<String> {
        return tempLiveData
    }


    fun getWindData(): MutableLiveData<String> {
        return windLiveData
    }

    fun getLanguageData(): MutableLiveData<String> {
        return languageLiveData
    }

    fun writeDataWeatherInSharedPreference(
        lat: String,
        lon: String,
        address: String,
        context: Context,
    ) {
        SP = PreferenceManager.getDefaultSharedPreferences(context)
        var editor = SP.edit()
        editor.putString("lat", lat)
        editor.putString("lon", lon)
        editor.putString("address", address)
        editor.commit()
    }

    fun writeTempDegreeInSharedPreference(
        tempDegree: String,
        status: String,
        windDegree: String,
        degree: String,
        context: Context
    ) {
        SP = PreferenceManager.getDefaultSharedPreferences(context)
        var editor = SP.edit()
        editor.putString("temp", tempDegree)
        editor.putString("cel", status)
        editor.putString("wind", windDegree)
        editor.putString("km", degree)
        editor.commit()
    }

    fun writeLanguageInSharedPreference(language: String, context: Context) {
        SP = PreferenceManager.getDefaultSharedPreferences(context)
        var editor = SP.edit()
        editor.putString("lang", language)
        editor.commit()
    }

}