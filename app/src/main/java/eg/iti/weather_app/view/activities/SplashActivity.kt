package eg.iti.weather_app.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import eg.iti.weather_app.R
import eg.iti.weather_app.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivitySplashBinding.inflate(layoutInflater)
            setContentView(binding.root)

        rain.alpha=0f
        rain.animate().setDuration(2000).alpha(1f).withEndAction {
            var intent = Intent(this@SplashActivity, WeatherActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }

    }
}