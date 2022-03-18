package eg.iti.weather_app.view.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import eg.iti.weather_app.R
import java.util.*

class DialogActivity : AppCompatActivity() {

    private lateinit var ringtone: Ringtone
    var event = " "
    var desc =  " "
    var main= ""
    lateinit var txtEvent: TextView
    lateinit var txtDesc:TextView
    lateinit var imgClose: ImageView
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        dialog = Dialog(this, androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert)
        dialog.setContentView(R.layout.dialog_custom_alarm)

        txtEvent = dialog.findViewById<TextView>(R.id.tv_event)
        txtDesc = dialog.findViewById<TextView>(R.id.tv_desc)
        imgClose = dialog.findViewById<ImageView>(R.id.img_close)

        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        if (intent.extras != null) {
            event = intent.getStringExtra("main").toString()
            txtDesc.text= desc
            txtEvent.text= event
            showDialogAlart(event, desc)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showDialogAlart(events:String, descs:String) {

        ringtone.play()

        imgClose.setOnClickListener {
            if (ringtone.isPlaying) {
                ringtone.stop()
            }
            dialog.dismiss()
            finish()
        }
        txtEvent.visibility = View.VISIBLE
        txtDesc.visibility = View.VISIBLE


        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Objects.requireNonNull(dialog.window)!!
                .setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        }
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}