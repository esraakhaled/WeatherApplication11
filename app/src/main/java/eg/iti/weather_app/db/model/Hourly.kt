package eg.iti.weather_app.db.model

import com.google.gson.annotations.SerializedName
import eg.iti.weather_app.db.WeatherXX

data class Hourly(
    @SerializedName("clouds")
    val clouds: Double,
    @SerializedName("dew_point")
    val dewPoint: Double,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("humidity")
    val humidity: Double,
    @SerializedName("pop")
    val pop: Double,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("uvi")
    val uvi: Double,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind_deg")
    val windDeg: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
)