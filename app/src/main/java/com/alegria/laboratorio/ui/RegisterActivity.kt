package com.alegria.laboratorio.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alegria.laboratorio.io.response.LoginResponse
import com.alegria.laboratorio.util.EcuatorianDocumentValid
import com.alegria.laboratorio.util.PreferenceHelper
import com.alegria.laboratorio.util.PreferenceHelper.set
import com.alegria.laboratorio.util.toast
import com.alegria.laboratorio.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val apiService: com.alegria.laboratorio.io.ApiService by lazy {
        com.alegria.laboratorio.io.ApiService.create()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gotoLogin.setOnClickListener {
            Toast.makeText(this, "Por favor llena tus datos", Toast.LENGTH_LONG).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val genero = arrayOf("Masculino", "Femenino")
        binding.spGender.setAdapter(
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                genero
            )
        )
        binding.btnRegistrarse.setOnClickListener {
            performRegister()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun performRegister() {
        val nui = binding.EtCedula.text.toString()
        val name = binding.EtNombre.text.toString().trim()
        val lastName = binding.EtApellido.text.toString().trim()
        val email = binding.EtEmail.text.toString().trim()
        val phone = binding.EtTelefono.text.toString()
        val age = binding.EtAge.text.toString()
        val gender = if (binding.spGender.selectedItem == "Masculino") {
            "M"
        } else {
            "F"
        };
        val password = binding.EtPassword.text.toString()
        if (EcuatorianDocumentValid(nui)) {
            toast("La cedula ingresada es invalida")
            return
        }
        val call = apiService.postRegister(nui, name, lastName, age, gender, email, phone, password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse == null) {
                        toast("Se obtuvo una respuesta inesperada del servidor")
                        return
                    }
                    if (loginResponse.success) {
                        createSessionPreference(
                            loginResponse.passport,
                            loginResponse.user.name,
                            loginResponse.user.id.toString(),
                            loginResponse.user.avatar
                        )
                        goToMenuActivity()
                    } else {
                        toast("Las credenciales son incorrectas")
                    }
                } else {
                    toast("Porfavor llenar todos los campos")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }

    private fun goToMenuActivity() {
        val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createSessionPreference(token: String, name: String, user: String, image: String) {

        val preferences = PreferenceHelper.defaultPrefs(this@RegisterActivity)
        preferences["passport"] = token
        preferences["name"] = name
        preferences["userId"] = user
        preferences["image"] = image

    }
}