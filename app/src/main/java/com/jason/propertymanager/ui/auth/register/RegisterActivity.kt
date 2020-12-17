package com.jason.propertymanager.ui.auth.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.ActivityRegisterBinding
import com.jason.propertymanager.ui.auth.UserViewModel
import com.jason.propertymanager.other.id_landlord
import com.jason.propertymanager.other.id_manager
import com.jason.propertymanager.other.id_tenant
import com.jason.propertymanager.other.id_vendor

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.registerType.observe(this, {
            when (it) {
                id_tenant -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, RegisterTenantFragment())
                        .commitNow()
                }
                id_vendor, id_manager, id_landlord -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, RegisterLandlordFragment())
                        .commitNow()
                }
                else -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.frame_container, RegisterNavigationFragment())
                        .commitNow()
                }
            }
        })
    }
}