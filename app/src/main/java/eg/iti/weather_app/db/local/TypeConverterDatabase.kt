package eg.iti.weather_app.db.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eg.iti.weather_app.db.model.*
import java.lang.reflect.Type

class TypeConverterDatabase {
    @TypeConverter
    fun fromCurrent(currentValues: Current?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toCurrent(currentString: String?): Current? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Current>() {}.type
        return gson.fromJson<Current>(currentString, type)
    }
//===================================================================


//===============================================================================

    @TypeConverter
    fun fromDaily(currentValues: List<Daily?>?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toDaily(currentString: String?): List<Daily?>? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily?>?>(currentString, type)
    }


//=======================================================================

    @TypeConverter
    fun fromTemp(currentValues: Temp?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Temp>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toTemp(currentString: String?): Temp? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<Temp>() {}.type
        return gson.fromJson<Temp>(currentString, type)
    }
//=======================================================================

    @TypeConverter
    fun fromFeelsLike(currentValues: FeelsLike?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<FeelsLike>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toFeelsLike(currentString: String?): FeelsLike? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<FeelsLike>() {}.type
        return gson.fromJson<FeelsLike>(currentString, type)
    }

//=======================================================================

    @TypeConverter
    fun fromHourly(currentValues: List<Hourly?>?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly?>?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toHourly(currentString: String?): List<Hourly?>? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly?>?>() {}.type
        return gson.fromJson<List<Hourly?>?>(currentString, type)
    }


//============================================================================


//=====================================================================================

    @TypeConverter
    fun fromWeather(currentValues: List<Weather?>?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather?>?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toWeather(currentString: String?): List<Weather?>? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather?>?>() {}.type
        return gson.fromJson<List<Weather?>?>(currentString, type)
    }

//////////////////////////

    @TypeConverter
    fun fromAlert(currentValues: List<Alert?>?): String? {
        if (currentValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Alert?>?>() {}.type
        return gson.toJson(currentValues, type) //json
    }

    @TypeConverter
    fun toAlert(currentString: String?): List<Alert?>? {
        if (currentString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Alert?>?>() {}.type
        return gson.fromJson<List<Alert?>?>(currentString, type)
    }


}
