
package com.twenty2byte.simplenews.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.twenty2byte.simplenews.R

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_nav_menu)
        bottomNavigationView.selectedItemId = R.id.mainHome
        bottomNavigationView.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId){
            R.id.mainHome -> fragment = HomeFragment()
            R.id.favorites -> fragment = FavoritesFragment()
            R.id.others -> fragment = OthersFragment()
        }
        if (fragment != null) supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.blink,0)
            .replace(R.id.frgView, fragment).commit()
        return true
    }
}