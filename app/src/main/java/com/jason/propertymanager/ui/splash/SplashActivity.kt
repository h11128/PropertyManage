package com.jason.propertymanager.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jason.propertymanager.databinding.ActivitySplashBinding
import com.jason.propertymanager.ui.auth.login.LoginActivity
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.other.url_logo
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        Picasso.get().load(url_logo).into(binding.imageSplash)
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            delay(3000L)
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

    }
}