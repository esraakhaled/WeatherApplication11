package eg.iti.weather_app.db.model

import com.google.gson.annotations.SerializedName
import eg.iti.weather_app.db.WeatherX

data class Daily(
    @SerializedName("clouds")
    val clouds: Double,
    @SerializedName("dew_point")
    val dewPoint: Double,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike,
    @SerializedName("humidity")
    val humidity: Double,
    @SerializedName("pop")
    val pop: Double,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("rain")
    val rain: Double,
    @SerializedName("snow")
    val snow: Double,
    @SerializedName("sunrise")
    val sunrise: Double,
    @SerializedName("sunset")
    val sunset: Double,
    @SerializedName("temp")
    val temp: Temp,
    @SerializedName("uvi")
    val uvi: Double,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("wind_deg")
    val windDeg: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
)