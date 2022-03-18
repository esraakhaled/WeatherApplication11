package eg.iti.weather_app.view.fragments.seven_days

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import eg.iti.weather_app.databinding.SevenDayCardBinding
import eg.iti.weather_app.db.model.Daily
import eg.iti.weather_app.utils.dayConverter
import eg.iti.weather_app.utils.setImage
import eg.iti.weather_app.utils.timeConverter

class DailyAdapter(var daysList: List<Daily>) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {
    lateinit var sp: SharedPreferences
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {

        val view = SevenDayCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)

        return DailyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    fun setData(daily: List<Daily>, context: Context){
        this.daysList= daily
        this.context= context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {

        holder.bind(daysList[position])
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        holder.view.textCelcius.text = sp.getString("cel", "")
        holder.view.texCelcius.text = sp.getString("cel", "")
        holder.view.textWind.text= sp.getString("km","")


    }



    class DailyViewHolder(var view: SevenDayCardBinding) :
        RecyclerView.ViewHolder(view.root) {
        private val imageview = view.imgForecastItem


        fun bind(forecast: Daily) {

            view.tvForecastState.text = forecast.weather.get(0).description
            view.tvForecastTemp.text = (Math.round(forecast.temp.day)).toString()
            view.tvForecastHumidity.text = (Math.round(forecast.humidity)).toString()
            view.tvForecastPressure.text = (Math.round(forecast.pressure)).toString()
            view.tvForecastTime.text = dayConverter((forecast.dt).toLong())
            view.tvForecastFeelsTemp.text = (Math.round(forecast.feelsLike.day)).toString()
            view.tvForecastWind.text = (Math.round(forecast.windSpeed)).toString()
            view.tvSunrise.text = timeConverter(forecast.sunrise.toLong())
            view.tvSunset.text = timeConverter(forecast.sunset.toLong())

            val url = forecast.weather.get(0).icon
            setImage(imageview, url)


        }
    }


}
