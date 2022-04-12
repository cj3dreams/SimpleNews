
package com.twenty2byte.simplenews.view.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.twenty2byte.simplenews.R
import java.util.*

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sharedPreferencesLang: SharedPreferences
    private lateinit var sharedPreferencesApiKey:SharedPreferences
    lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesLang = getSharedPreferences("Language", Context.MODE_PRIVATE)
        sharedPreferencesApiKey = getSharedPreferences("ApiKey", Context.MODE_PRIVATE)
        if (!sharedPreferencesApiKey.contains("ApiKey"))
            sharedPreferencesApiKey.edit().putString("ApiKey", "b50553f762124ec8809bb9af138e4c38").apply()
        if (sharedPreferencesLang.contains("Language")){
            val get = sharedPreferencesLang.getString("Language", "").toString()
            val config = resources.configuration
            val locale = Locale(get)
            Locale.setDefault(locale)
            config.setLocale(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                createConfigurationContext(config)
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        setTheme(R.style.Theme_SimpleNews)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_nav_menu)
        bottomNavigationView.setOnItemSelectedListener(this)
        bottomNavigationView.selectedItemId = R.id.mainHome
        backButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId){
            R.id.mainHome -> fragment = HomeFragment()
            R.id.favorites -> fragment = FavoritesFragment()
            R.id.others -> fragment = OthersFragment()
        }
        supportFragmentManager.popBackStackImmediate()
        if (fragment != null) supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.blink,0)
            .replace(R.id.frgView, fragment).commit()

        return true
    }
}