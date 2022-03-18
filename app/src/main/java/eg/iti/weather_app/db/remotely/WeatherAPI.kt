package eg.iti.weather_app.db.remotely

import eg.iti.weather_app.db.entity.FavouriteData
import eg.iti.weather_app.db.entity.Weather_Response
import eg.iti.weather_app.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    //ar  en lang

    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") lat: String = "30.489772",
        @Query("lon") lon: String = "-99.771335",
        @Query("units") units: String = "metric",//metric: Celsius, imperial: Fahrenheit.
        @Query("lang") lang: String = "ar",  //metric: metre/sec, imperial: miles/hour
        // @Query("exclude") exclude: String="current",
        @Query("APPID") app_id: String = API_KEY,
    ): Response<Weather_Response>




    @GET("onecall")
    suspend fun getFavData(
        @Query("lat") lat: String = "31.25654",
        @Query("lon") lon: String = "32.28411",
        @Query("units") units: String = "metric",//metric: Celsius, imperial: Fahrenheit.
       @Query("lang") lang: String = "ar",  //metric: metre/sec, imperial: miles/hour
      // @Query("exclude") exclude: String="current",
        @Query("APPID") app_id: String = API_KEY,
    ): Response<FavouriteData>


}