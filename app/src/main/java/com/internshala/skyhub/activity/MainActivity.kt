package com.internshala.skyhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.internshala.skyhub.*
import com.internshala.skyhub.fragment.AboutAppFragment
import com.internshala.skyhub.fragment.DashboardFragment
import com.internshala.skyhub.fragment.FavouritesFragment
import com.internshala.skyhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordLayout = findViewById(R.id.coordLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)

        setupToolbar()

        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId) {
                R.id.itmDashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.itmFavourites -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, FavouritesFragment()).commit()
                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.itmProfile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ProfileFragment()).commit()
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.itmAboutApp -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AboutAppFragment()).commit()
                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun openDashboard() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, DashboardFragment())
            .addToBackStack("Dashboard").commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.itmDashboard)
    }

    fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "SkyHub"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragmentScreen = supportFragmentManager.findFragmentById(R.id.frameLayout)

        when(fragmentScreen) {
            !is DashboardFragment -> openDashboard()
            else -> finish()
        }
    }
}