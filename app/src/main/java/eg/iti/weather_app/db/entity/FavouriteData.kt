package eg.iti.weather_app.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import eg.iti.weather_app.db.model.Daily

@Entity(tableName = "favouriteData")

data class FavouriteData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("daily")
    val daily: List<Daily>
)