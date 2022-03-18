package eg.iti.weather_app.view.fragments.alarm

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eg.iti.weather_app.R
import eg.iti.weather_app.db.entity.AlarmEntity
import eg.iti.weather_app.db.model.Hourly
import eg.iti.weather_app.viewmodel.AlarmViewModel
import eg.iti.weather_app.viewmodel.WeatherViewModel
import eg.iti.weather_app.databinding.FragmentAlertBinding
import eg.iti.weather_app.db.Resource
import eg.iti.weather_app.receiver.AlertReceiver
import eg.iti.weather_app.receiver.DialogReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AlarmFragment : Fragment(R.layout.fragment_alert) {
    var myHour: Int? = 0
    var myMin: Int? = 0
    var myHour2: Int? = 0
    var myMin2: Int? = 0
    var myYear: Int? = 0
    var myMon: Int? = 0
    var myDay: Int? = 0

    lateinit var sp: SharedPreferences
    private lateinit var alarmManager: AlarmManager
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var alarmAdapter: Alarmadabter
    private lateinit var alarmList: List<Hourly>

    lateinit var binding: FragmentAlertBinding
    private var notificationOrAlarm = "notification"

    var startTime =""
    var endTime = ""
    var event:String=""


    private fun init() {
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmList = ArrayList()

        binding.recyclaerViewAlarm.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recyclaerViewAlarm.setHasFixedSize(true)
        alarmAdapter= Alarmadabter(requireContext())
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recyclaerViewAlarm)
        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {

        binding = FragmentAlertBinding.inflate(layoutInflater)
        binding.layoutDate.setOnClickListener {
            getDateFrom()
        }
        binding.layoutTimeFrom.setOnClickListener {
            getTimeFrom()
        }

        binding.layoutTimeTo.setOnClickListener {
            getTimeTo()

        }


        binding.radioGroupNOrA.setOnCheckedChangeListener({ group, checkedId ->
            if (checkedId == R.id.notification) {
                notificationOrAlarm = "notification"
            } else {
                notificationOrAlarm = "alarm"
            }
        })
        init()


        alarmViewModel.getWeatherFromRoom()
        alarmViewModel.weatherFromRoomLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let {
                        alarmList = it.hourly
                    }
                }
                is Resource.Error -> {
                }
            }
        })

        getAlarmFromDBToRecyclerView()

        binding.btnAdd.setOnClickListener {
            binding.tvTimeFrom.text = " "
            binding.tvTimeTo.text=""
            binding.tvDate.text = " "
            checkAlarm()
        }


        val events = resources.getStringArray(R.array.main)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, events)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.setAdapter(spinnerAdapter)

        binding.spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long,
            ) {

                var arrayOfEvent = resources.getStringArray(R.array.main)
                event = arrayOfEvent[position]
                val editor =sp.edit()
                editor.putString("main", event)
                editor.commit()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })


        return binding.root
    }



//==================================================================================

    private fun getDateFrom() {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
                binding.tvDate.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                binding.tvDate.visibility = View.VISIBLE

                myMon = monthOfYear + 1
                myYear = year
                myDay = dayOfMonth
            }, year, month, day
            )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun getTimeFrom() {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val datetime = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                //Hour_of_Day
                c[Calendar.HOUR] = h
                c[Calendar.MINUTE] = m
                if (c.timeInMillis >= datetime.timeInMillis) {
                    binding.tvTimeFrom.setText("" + h + ":" + m)

                    binding.tvTimeFrom.visibility = View.VISIBLE

                    myHour = h
                    myMin = m
                    startTime = "$myHour : $myMin"

                } else {
                    Toast.makeText(requireActivity(), R.string.invalidDorT, Toast.LENGTH_LONG)
                        .show()
                    binding.tvTimeFrom.setText(" ")
                    binding.tvTimeFrom.visibility = View.VISIBLE
                }
            }), hour, minute, false)

        timePickerDialog.show()
    }

    private fun getTimeTo() {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val datetime = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                //Hour_of_Day
                c[Calendar.HOUR] = h
                c[Calendar.MINUTE] = m
                if (c.timeInMillis >= datetime.timeInMillis) {
                    binding.tvTimeTo.setText("" + h + ":" + m)
                    endTime = ""
                    binding.tvTimeTo.visibility = View.VISIBLE

                    myHour2 = h
                    myMin2 = m
                    endTime= "$myHour2 : $myMin2"
                } else {
                    Toast.makeText(requireActivity(), "Invalide Date or Time", Toast.LENGTH_LONG)
                        .show()
                    binding.tvTimeTo.setText(" ")
                    binding.tvTimeTo.visibility = View.VISIBLE
                }
            }), hour, minute, false)

        timePickerDialog.show()
    }



    //=========================================Alarm=================================================


    fun checkAlarm(){

        if (myHour != null && myMin != null && myDay != null && myMon != null && myYear != null) {
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")

            val date: String = myDay.toString() + "-" + myMon + "-" + myYear + " " + myHour + ":" + myMin

            val dateLong = sdf.parse(date)!!.time
            if (alarmList.size > 0) {
                for (item in alarmList) {
                    if (dateLong / 1000 > item.dt!!) {
                        if (notificationOrAlarm.equals("notification")) {
                            setCustomNotification(event, myHour!!, myMin!!,myHour2!!,myMin2!!,
                                myDay!!, myMon!!, myYear!!)
                        } else {
                            setCustomAlaram(event,
                                myHour!!, myMin!!,
                                myHour2!!,myMin2!!,
                                myDay!!, myMon!!, myYear!!
                            )
                        }
                        break
                    }
                }
            } else {
                if (notificationOrAlarm.equals("notification")) {
                    setCustomNotification(R.string.dialogAlarm.toString(), myHour!!, myMin!!,myHour2!!, myMin2!!,
                        myDay!!, myMon!!, myYear!!)
                } else {
                    setCustomAlaram(R.string.dialogAlarm.toString(),
                        myHour!!, myMin!!,
                        myHour2!!,myMin2!!,
                        myDay!!, myMon!!, myYear!!)
                }
            }
        } else {
            Toast.makeText(requireActivity(), "Data is empty", Toast.LENGTH_LONG).show()
        }

    }


    private fun getAlarmFromDBToRecyclerView() {
        alarmViewModel.getAlarm(requireContext()).observe(viewLifecycleOwner, {
            it?.let {
                alarmAdapter.fetchData(it, requireContext())
                binding.recyclaerViewAlarm.adapter = alarmAdapter
            }

        })

    }


    private fun addAlarm(requestCode: Int, main: String, date:String,timeFrom: String, timeTo:String) {
        val alarm = AlarmEntity(requestCode, main, date, timeFrom, timeTo)
        CoroutineScope(Dispatchers.IO).launch {
            alarmViewModel.addAlarm(alarm, requireContext())
        }

    }


    private fun setCustomAlaram(main: String, hour: Int, min: Int,
                                hour2: Int, min2: Int,
                                day: Int, month: Int, year: Int, ) {
        val intentDialogueReciever = Intent(context, DialogReceiver::class.java)
        intentDialogueReciever.putExtra("main", main)
        val random = Random()
        val requestCode = random.nextInt(99)
        val pendingIntentDialogueReciever =
            PendingIntent.getBroadcast(context, requestCode, intentDialogueReciever, 0)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DATE] = day
        calendar[Calendar.YEAR] = year
        calendar[Calendar.SECOND] = 0

        val calendar2 = Calendar.getInstance()
        calendar2.set(Calendar.HOUR_OF_DAY, hour2)
        calendar2.set(Calendar.MINUTE, min2)
        calendar2[Calendar.MONTH] = month - 1
        calendar2[Calendar.DATE] = day
        calendar2[Calendar.YEAR] = year
        calendar2[Calendar.SECOND] = 0
        val alarmtime: Long = calendar.timeInMillis
        val alarmtime2: Long = calendar2.timeInMillis
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmtime, alarmtime2,pendingIntentDialogueReciever)
        Toast.makeText(context, "Alarm is Done!", Toast.LENGTH_LONG).show()
        requireActivity().registerReceiver(DialogReceiver(), IntentFilter())
        var date = day.toString() + "_" + month + "_" + year + " " + hour + ":" + min
        var timeFrom ="$hour : $min"
        var timeTo ="$hour2 : $min2"

        addAlarm(requestCode, main, date,  timeFrom, timeTo)
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setCustomNotification( main: String, hour: Int, min: Int,
                               hour2: Int, min2: Int,
                               day: Int, month: Int, year: Int,
    ) {

        val intentAlertReciever = Intent(context, AlertReceiver::class.java)
        intentAlertReciever.putExtra("main", main)
        val random = Random()
        val requestCode = random.nextInt(99)
        val pendingIntentAlertReciever =
            PendingIntent.getBroadcast(context, requestCode, intentAlertReciever, 0)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DATE] = day
        calendar[Calendar.YEAR] = year
        calendar[Calendar.SECOND] = 0

        val calendar2 = Calendar.getInstance()
        calendar2.set(Calendar.HOUR_OF_DAY, hour2)
        calendar2.set(Calendar.MINUTE, min2)
        calendar2[Calendar.MONTH] = month - 1
        calendar2[Calendar.DATE] = day
        calendar2[Calendar.YEAR] = year
        calendar2[Calendar.SECOND] = 0

        val alarmtime: Long = calendar.timeInMillis
        val alarmtime2: Long = calendar2.timeInMillis
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmtime, alarmtime2,
            pendingIntentAlertReciever)
        Toast.makeText(context, R.string.alarmDone, Toast.LENGTH_LONG).show()
        requireActivity().registerReceiver(AlertReceiver(), IntentFilter())
        var date = day.toString() + "/" + month + "/" + year
        var timeFrom="$hour : $min"
        var timeTo="$hour2: $min2"

        addAlarm(requestCode, main, date, timeFrom, timeTo)
    }


    var itemTouchHelper: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(activity).setMessage(R.string.alert)
                    .setPositiveButton(R.string.yes,
                        DialogInterface.OnClickListener { dialog, id ->
                            val alertItemDeleted = alarmAdapter.getItemByVH(viewHolder)
                            cancelAlarm(alertItemDeleted.requestCode)
                            CoroutineScope(Dispatchers.IO).launch {
                                deleteAlarmItemFromDB(alertItemDeleted)
                            }
                            alarmAdapter.removeAlarmItem(viewHolder)
                        })
                    .setNegativeButton(R.string.no,
                        DialogInterface.OnClickListener { dialog, id ->
                            getAlarmFromDBToRecyclerView()
                        }).show()

            }
        }

    suspend fun deleteAlarmItemFromDB(alertDB: AlarmEntity) {
        alarmViewModel.deleteAlarm(alertDB, requireContext())
    }


    fun cancelAlarm(requestCode: Int) {
        val intent = Intent(requireContext(), AlertReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

}