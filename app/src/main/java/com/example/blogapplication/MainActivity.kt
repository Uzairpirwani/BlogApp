package com.example.blogapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.blogapplication.sharedPreference.AppPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appPreferences = AppPreferences(this)


        Handler(Looper.getMainLooper()).postDelayed({
            if (appPreferences.isLoggedIn == 0){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, DashBoardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)

    }
}