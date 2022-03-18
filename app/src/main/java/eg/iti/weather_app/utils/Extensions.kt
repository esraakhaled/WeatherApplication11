package eg.iti.weather_app.utils

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.widget.ImageView
import eg.iti.weather_app.R
import java.text.SimpleDateFormat
import java.util.*

fun setLocale(activity: Activity, languageCode: String?): Unit {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources = activity.resources
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}


fun getAddressGeocoder(lat: Double, lon:Double, context: Context?): String? {
    val geocoder = Geocoder(context)
    val list = geocoder.getFromLocation(lat, lon, 1)
    return list[0].getAddressLine(0)

}



fun timeConverter(time: Long): String {
    var converter = SimpleDateFormat("hh:mm a")
    var convertedTime = converter.format(Date(time*1000))

    return convertedTime
}

fun dayConverter(time: Long) : String{
    var converter = SimpleDateFormat("EEE, d MMM yyyy hh:mm a")
    var convertedDay = converter.format(Date(time*1000))

    return convertedDay
}



fun setImage(imageview: ImageView, url:String?){
    when (url) {
        "01d" -> imageview.setImageResource(R.drawable.ic_01d)
        "01n" -> imageview.setImageResource(R.drawable.ic_01n)
        "02d" -> imageview.setImageResource(R.drawable.ic_02d)
        "02n" -> imageview.setImageResource(R.drawable.ic_02n)
        "03d" -> imageview.setImageResource(R.drawable.ic_03d)
        "03n" -> imageview.setImageResource(R.drawable.ic_03n)
        "04d" -> imageview.setImageResource(R.drawable.ic_04d)
        "04n" -> imageview.setImageResource(R.drawable.ic_04n)
        "09d" -> imageview.setImageResource(R.drawable.ic_09d)
        "09n" -> imageview.setImageResource(R.drawable.ic_09n)
        "10d" -> imageview.setImageResource(R.drawable.ic_10d)
        "10n" -> imageview.setImageResource(R.drawable.ic_10n)
        "11d" -> imageview.setImageResource(R.drawable.ic_11d)
        "11n" -> imageview.setImageResource(R.drawable.ic_11n)
        "13d" -> imageview.setImageResource(R.drawable.ic_13d)
        "13n" -> imageview.setImageResource(R.drawable.ic_13n)
        "50d" -> imageview.setImageResource(R.drawable.ic_50d)
        "50n" -> imageview.setImageResource(R.drawable.ic_50n)
    }




}