package com.jason.propertymanager.ui.auth.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.ActivityRegisterBinding
import com.jason.propertymanager.other.*
import com.jason.propertymanager.ui.auth.UserViewModel
import com.jason.propertymanager.ui.home.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


    }

    private fun init() {
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.registerType.observe(this, {
            when (it) {
                id_tenant -> {
                    supportFragmentManager.beginTransaction()
                        .addToBackStack("navigation")

                        .replace(R.id.frame_container, RegisterTenantFragment())
                        .commit()
                }
                id_vendor, id_manager, id_landlord -> {
                    supportFragmentManager.beginTransaction()
                        .addToBackStack("navigation")
                        .replace(R.id.frame_container, RegisterLandlordFragment())

                        .commit()
                }
                else -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.frame_container, RegisterNavigationFragment())
                        .commitNow()
                }
            }
        })
        userViewModel.currentUser.observe(this, {
            if (it != null) {
                //val user = it[0]
                registerSuccess("email ${it.email} name ${it.name}")
                //loginSuccess("email ${user.email} name ${user.name}")
                Log.d(tag_d, "login success")
                setResult(Activity.RESULT_OK)
                finish()
            }

        })
    }

    private fun registerSuccess(message: String) {
        val welcome = getString(R.string.welcome)
        Toast.makeText(
            applicationContext,
            "$welcome $message",
            Toast.LENGTH_LONG
        ).show()
        startActivity(Intent(this, MainActivity::class.java))
    }
}