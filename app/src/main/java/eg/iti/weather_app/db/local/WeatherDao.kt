package eg.iti.weather_app.db.local

import androidx.lifecycle.LiveData
import androidx.room.*
import eg.iti.weather_app.db.entity.AlarmEntity
import eg.iti.weather_app.db.entity.FavouriteData
import eg.iti.weather_app.db.entity.Weather_Response

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weather: Weather_Response)

    @Query("SELECT * FROM  weatherData")
    fun getAllWeathers(): Weather_Response


    ////////////////////////////////////////////////////////////
    //Favourite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavWeatherData(fav : FavouriteData)

    @Query("SELECT * FROM favouriteData")
    fun getFavWetherData(): List<FavouriteData>

    @Query("select * from favouriteData where id in (:num)")
    fun getDataById(num: Int): FavouriteData

    @Delete
    fun deleteFav(Fav: FavouriteData)

    /////////////////////////////////////////////////////////////
    //=====================================================================
    //Alarm
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alertDatabase: AlarmEntity)

    @Query("select * from alarm_table")
    fun getAlarm(): LiveData<MutableList<AlarmEntity>>

    @Delete
    fun deleteAlarm(alertDatabase: AlarmEntity)

}