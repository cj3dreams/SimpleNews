
package com.twenty2byte.simplenews

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.twenty2byte.simplenews.view.HomeFragment

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView = findViewById(R.id.bottom_nav_menu)
        bottomNavigationView.selectedItemId = R.id.mainHome
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId){
            R.id.mainHome -> fragment = HomeFragment()
        }
        if (fragment != null) {
            screenChanger(fragment)
        }
        return true
    }

    private fun screenChanger(fragment: Fragment){
        supportFragmentManager.beginTransaction().
        replace(R.id.frgView, fragment).commit()
    }
}