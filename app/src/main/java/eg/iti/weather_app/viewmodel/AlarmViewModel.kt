package eg.iti.weather_app.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eg.iti.weather_app.db.Repository
import eg.iti.weather_app.db.Resource
import eg.iti.weather_app.db.entity.AlarmEntity
import eg.iti.weather_app.db.entity.Weather_Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private var repo: Repository

    val weatherFromRoomLiveData = MutableLiveData<Resource<Weather_Response>>()

    init {
        repo = Repository()

    }


    fun getWeatherFromRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getWeatherFromRoom(getApplication())
            weatherFromRoomLiveData.postValue(handleGetWeatherFromRoom(result)!!)
        }
    }

    private fun handleGetWeatherFromRoom(result: Weather_Response): Resource<Weather_Response>? {
        if (result != null) {
            return Resource.Success(result)
        }
        return Resource.Error("Room is empty")
    }


    suspend fun addAlarm(alertDatabase: AlarmEntity, context: Context) {
        return repo.addAlarm(alertDatabase, context)
    }

    fun getAlarm(context: Context): LiveData<MutableList<AlarmEntity>> {
        return repo.getAlarm(context)
    }

    suspend fun deleteAlarm(alertDatabase: AlarmEntity, context: Context) {
        return repo.deleteAlarm(alertDatabase, context)
    }

}