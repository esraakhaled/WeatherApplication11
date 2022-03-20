package eg.iti.weather_app.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import eg.iti.weather_app.db.model.Alert
import eg.iti.weather_app.db.model.Current
import eg.iti.weather_app.db.model.Daily
import eg.iti.weather_app.db.model.Hourly

@Entity(tableName = "weatherData")
data class Weather_Response(
    @PrimaryKey
    val id: Int,
    @SerializedName("alerts")
    val alerts: List<Alert>?,
    @SerializedName("current")
    val current: Current,
    @SerializedName("daily")
    val daily: List<Daily>,
    @SerializedName("hourly")
    val hourly: List<Hourly>,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Double
)