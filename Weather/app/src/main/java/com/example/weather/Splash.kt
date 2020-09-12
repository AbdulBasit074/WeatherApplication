package com.example.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.splash.*

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN //For Full Screen (Immursive View )

        /*** Upload Gif Through Glide Library*/
        Glide.with(this).load(R.drawable.l0d).into(two)

        /***Start New Activity After 5 Second Wait*/
        waitingThread()

    }

    private fun waitingThread()
    {
        Thread{ run {

            Thread.sleep(5000)
            startActivity(Intent(applicationContext,Main::class.java))
            finish()
        }}.start()
    }


}