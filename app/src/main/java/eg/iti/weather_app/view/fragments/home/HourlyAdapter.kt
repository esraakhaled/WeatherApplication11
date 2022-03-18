package eg.iti.weather_app.view.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import eg.iti.weather_app.databinding.ForecastWeatherHourlyItemBinding
import eg.iti.weather_app.db.model.Hourly
import eg.iti.weather_app.utils.dayConverter
import eg.iti.weather_app.utils.setImage

class HourlyAdapter (var hourList: List<Hourly>) :
    RecyclerView.Adapter<HourlyAdapter.ForecatViewHolder>() {

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecatViewHolder {

        val view = ForecastWeatherHourlyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)

        return ForecatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hourList.size
    }

    fun setData(hourList: List<Hourly>, context: Context) {
        this.context = context
        this.hourList = hourList
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: ForecatViewHolder, position: Int) {

        holder.bind(hourList[position])

//        if (position % 2 == 0) {
//            holder.view.cardHourly.setCardBackgroundColor(ContextCompat.getColor(context,
//                R.drawable.background_cloudly_weather));
//        } else {
//            holder.view.cardHourly.setCardBackgroundColor(ContextCompat.getColor(context,
//                R.drawable.background_rainy_weather));
//        }


        val sp = PreferenceManager.getDefaultSharedPreferences(context)

        holder.view.textCelcius.text = sp.getString("cel", "")
        holder.view.texCelcius.text = sp.getString("cel", "")
        holder.view.textWind.text= sp.getString("km","")

    }


    class ForecatViewHolder(var view: ForecastWeatherHourlyItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        val imageview = view.imgForecastItem


        fun bind(forecast: Hourly) {

            view.tvForecastWind.text= forecast.windSpeed.toString()
            view.tvForecastState.text = forecast.weather.get(0).description
            view.tvForecastTemp.text = (forecast.temp).toString()
            view.tvForecastHumidity.text = (Math.round(forecast.humidity)).toString()
            view.tvForecastPressure.text = (Math.round(forecast.pressure)).toString()
            view.tvForecastTime.text = dayConverter((forecast.dt).toLong())
            view.tvForecastFeelsTemp.text = (Math.round(forecast.feelsLike)).toString()
            view.tvVisibility.text = forecast.visibility.toString()
            view.tvMain.text=forecast.feelsLike.toString()

            val url = forecast.weather.get(0).icon
            setImage(imageview, url)


        }
    }


}