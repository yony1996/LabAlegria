package com.alegria.laboratorio.ui


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.alegria.laboratorio.R
import com.alegria.laboratorio.databinding.ActivityHomeBinding
import com.alegria.laboratorio.ui.fragments.ExamFragment
import com.alegria.laboratorio.ui.fragments.HomeFragment
import com.alegria.laboratorio.ui.fragments.UserFragment
import com.alegria.laboratorio.util.PreferenceHelper
import com.alegria.laboratorio.util.PreferenceHelper.get
import com.alegria.laboratorio.util.PreferenceHelper.set
import com.alegria.laboratorio.util.toast
import com.example.lact.AppoimentFragment

import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView

    private val appoimentFragment = AppoimentFragment()
    private val examFragment = ExamFragment()
    private val homeFragment = HomeFragment()
    private val userFragment = UserFragment()

    private val apiService: com.alegria.laboratorio.io.ApiService by lazy {
        com.alegria.laboratorio.io.ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(appoimentFragment)

        drawer = binding.drawerLayout
        navigationView = binding.navView
        toggle = ActionBarDrawerToggle(this, drawer, R.string.start, R.string.close)
        drawer.addDrawerListener(toggle)

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }



        binding.btnNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.exam -> replaceFragment(examFragment)
                R.id.appoiment -> replaceFragment(appoimentFragment)
                R.id.user -> replaceFragment(userFragment)
            }
            true
        }

        binding.closeSession.setOnClickListener {
            performLogout()
        }


        val ss: String = preferences.getString("name", "").toString()
        val nav = navigationView.getHeaderView(0)
        val name = nav.findViewById<TextView>(R.id.welcome)
        name.text = getString(R.string.saludo, ss)

        val img = navigationView.getHeaderView(0).findViewById<ImageView>(R.id.image)
        val image = preferences.getString("image", "").toString()
        Picasso.with(this).load(image)
            .placeholder(R.drawable.logo2)
            .error(R.drawable.logo2)
            .into(img)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM or ActionBar.DISPLAY_SHOW_HOME or ActionBar.DISPLAY_HOME_AS_UP
        supportActionBar?.setIcon(null)
        navigationView.setNavigationItemSelectedListener(this)


        binding.newAppoiment.setOnClickListener {
            val intent = Intent(this, CreateAppoimentActivity::class.java)
            intent.putExtra("data", "hola")
            startActivity(intent)
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame, fragment)
            transaction.commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> toast("hola")
            R.id.appoiment -> toast("hola2")
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun performLogout() {
        val passport = preferences["passport", ""]
        val call = apiService.postLogout("Bearer $passport")
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreference()
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }
        })
    }

    private fun clearSessionPreference() {
        preferences["passport"] = ""
        preferences["user"] = ""
        preferences["image"] = ""
    }
}


