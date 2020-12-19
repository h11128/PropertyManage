package com.jason.propertymanager.ui.home

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.User
import com.jason.propertymanager.databinding.ActivityMainBinding
import com.jason.propertymanager.databinding.NavHeaderMainBinding
import com.jason.propertymanager.ui.auth.UserViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var userViewModel: UserViewModel
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        init()

    }

    private fun init() {



        setSupportActionBar(mainBinding.appBarMain.toolbar)


        drawerLayout = mainBinding.drawerLayoutMain
        navView = mainBinding.navViewMain
        val navHeaderMainBinding = NavHeaderMainBinding.bind(navView.getHeaderView(0))

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_document,
                R.id.nav_property,
                R.id.nav_rent,
                R.id.nav_todo,
                R.id.nav_transaction,
                R.id.nav_vendor,
                R.id.nav_tenant
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.currentUser.observe(this, {
            if (it!= null){
                user = it
                navHeaderMainBinding.apply {
                    textUserEmail.text = user.email
                    textUserName.text = user.name
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}