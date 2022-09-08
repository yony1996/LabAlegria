package com.alegria.laboratorio.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.alegria.laboratorio.io.response.SimpleResponse
import com.alegria.laboratorio.model.Exam
import com.alegria.laboratorio.model.HourInterval
import com.alegria.laboratorio.util.PreferenceHelper
import com.alegria.laboratorio.util.PreferenceHelper.get
import com.alegria.laboratorio.util.toast
import com.alegria.laboratorio.R
import com.alegria.laboratorio.databinding.ActivityCreateAppoimentBinding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class CreateAppoimentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAppoimentBinding
    private val apiService: com.alegria.laboratorio.io.ApiService by lazy {
        com.alegria.laboratorio.io.ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAppoimentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.selFecha.setOnClickListener {
            onClickScheduleDate()
        }

        binding.storeAppoiment.setOnClickListener {
            performStoreExam()
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
        loadExams()
        listenDateAndChanges()
    }

    @SuppressLint("StringFormatMatches")
    private fun onClickScheduleDate() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                binding.selFecha.setText(
                    resources.getString(
                        R.string.date_format,
                        year,
                        (monthOfYear + 1).twoDigits(),
                        dayOfMonth.twoDigits()
                    )
                )

                binding.selFecha.error = null

            },
            year,
            month,
            day
        )
        //dpd.datePicker.maxDate = System.currentTimeMillis() + 2000

        dpd.show()
    }

    private fun Int.twoDigits() = if (this >= 10) this.toString() else "0$this"
    private fun listenDateAndChanges() {
        val userId = preferences["userId", ""]
        binding.selFecha.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loadHours(userId.toInt(), binding.selFecha.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    private fun loadHours(userId: Int, date: String) {
        //Toast.makeText(this,"doctor:$doctorId ,date:$date",Toast.LENGTH_LONG).show()
        if (date.isEmpty()) {
            return
        }
        val call = apiService.getHours(date, userId)

        call.enqueue(object : Callback<ArrayList<HourInterval>> {
            override fun onResponse(
                call: Call<ArrayList<HourInterval>>,
                response: Response<ArrayList<HourInterval>>
            ) {
                if (response.isSuccessful) {

                    var turns = response.body()
                    if (turns.isNullOrEmpty()) {
                        binding.spHoras.visibility = View.GONE
                        binding.arrowSp.visibility = View.GONE
                        binding.spalert.visibility = View.VISIBLE
                        binding.storeAppoiment.visibility = View.GONE
                    } else {
                        binding.spHoras.visibility = View.VISIBLE
                        binding.arrowSp.visibility = View.VISIBLE
                        binding.spalert.visibility = View.GONE
                        binding.storeAppoiment.visibility = View.VISIBLE
                    }

                    binding.spHoras.adapter = ArrayAdapter(
                        this@CreateAppoimentActivity, android.R.layout.simple_list_item_1,
                        turns?.toList() ?: emptyList()
                    )

                }
            }

            override fun onFailure(call: Call<ArrayList<HourInterval>>, t: Throwable) {
                toast("${t.printStackTrace()}")
            }
        })


    }

    private fun loadExams() {
        val token = preferences["passport", ""]
        val call = apiService.getExams("Bearer $token")

        call.enqueue(object : Callback<ArrayList<Exam>> {
            override fun onResponse(
                call: Call<ArrayList<Exam>>,
                response: Response<ArrayList<Exam>>
            ) {
                if (response.isSuccessful) {

                    val exams = response.body()
                    binding.spExam.adapter = ArrayAdapter(
                        this@CreateAppoimentActivity, android.R.layout.simple_list_item_1,
                        exams?.toList() ?: emptyList()
                    )
                }
            }

            override fun onFailure(call: Call<ArrayList<Exam>>, t: Throwable) {
                toast("${t.printStackTrace()}")
            }
        })
    }

    private fun performStoreExam() {
        val token = preferences["passport", ""]
        val user_id = preferences["userId", ""]
        val authHeader = "Bearer $token"
        val userId = user_id.toInt()
        val hora = binding.spHoras.selectedItem as HourInterval
        val fecha = binding.selFecha.text.toString()
        val exam_id = binding.spExam.selectedItem as Exam

        val call = apiService.storeAppointment(
            authHeader, userId, exam_id.id, fecha, hora.start
        )
        call.enqueue(object : Callback<SimpleResponse> {
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful) {
                    toast("Se ha guardado el turno")
                    finish()
                } else {
                    toast("Ocurrio un error")

                }
            }

            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }
        })
    }
}