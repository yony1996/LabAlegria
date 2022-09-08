package com.alegria.laboratorio.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.alegria.laboratorio.io.ApiService
import com.alegria.laboratorio.io.response.LoginResponse
import com.alegria.laboratorio.util.PreferenceHelper
import com.alegria.laboratorio.util.PreferenceHelper.get
import com.alegria.laboratorio.util.PreferenceHelper.set
import com.alegria.laboratorio.util.toast
import com.alegria.laboratorio.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val apiService: ApiService by lazy {
       ApiService.create()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if (preferences["passport", ""].contains(".")) {
            val name = preferences["name", ""]
            val user = preferences["userId", ""]
            val image = preferences["image", ""]
            goToMenuActivity(name, user, image)
        }

        binding.BtnIngresar.setOnClickListener {
            performLogin()
        }
        binding.gotoRegister.setOnClickListener {
            Toast.makeText(this, "Por favor llena tus datos", Toast.LENGTH_LONG).show()

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun performLogin() {
        val email = binding.EtEmail.text.toString()
        val password = binding.EtPassword.text.toString()

        val call = apiService.postLogin(email, password)
        Log.d("call",call.toString())
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse == null) {
                        toast("Se obtuvo una respuesta inesperada del servidor")
                        return
                    }
                    Log.d("TAG", "${loginResponse}")
                    if (loginResponse.success) {
                        createSessionPreference(
                            loginResponse.passport,
                            loginResponse.user.name,
                            loginResponse.user.id.toString(),
                            loginResponse.user.avatar
                        )
                        goToMenuActivity(loginResponse.user.name, loginResponse.user.id.toString(),loginResponse.user.avatar)
                    } else {
                        toast("Las credenciales son incorrectas")
                    }
                } else {
                    toast("Se obtuvo una respuesta inesperada del servidor")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val error = call.toString()
                Log.d("tag", t.printStackTrace().toString())
            }

        })
    }

    private fun goToMenuActivity(name: String,user:String, image: String) {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.putExtra("USER_NAME", name)
        intent.putExtra("USER_IMAGE", image)
        startActivity(intent)
        finish()
    }

    private fun createSessionPreference(token: String, name: String, user: String, image: String) {

        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["passport"] = token
        preferences["name"] = name
        preferences["userId"] = user
        preferences["image"] = image

    }

}