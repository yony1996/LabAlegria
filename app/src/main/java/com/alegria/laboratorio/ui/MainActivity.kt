package com.alegria.laboratorio.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.alegria.laboratorio.util.PreferenceHelper
import com.alegria.laboratorio.util.PreferenceHelper.get
import com.alegria.laboratorio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            verification()
        },3000)
    }
    private fun verification(){
        val preferences=PreferenceHelper.defaultPrefs(this)
        if (preferences["passport",""].contains(".")){
            goToMenuActivity()
        }else{
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun goToMenuActivity(){
        val intent=Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}