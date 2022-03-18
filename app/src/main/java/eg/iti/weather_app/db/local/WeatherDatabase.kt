package eg.iti.weather_app.db.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eg.iti.weather_app.db.entity.AlarmEntity
import eg.iti.weather_app.db.entity.FavouriteData
import eg.iti.weather_app.db.entity.Weather_Response

@Database(
    entities = [Weather_Response::class, FavouriteData::class,  AlarmEntity::class],
    version = 5)
@TypeConverters(TypeConverterDatabase::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {

            return instance ?: synchronized(this) {
                instance ?: createDatabase(context).also { instance = it }
            }

        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "my_weather").build()

    }

}