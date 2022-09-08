package com.alegria.laboratorio.ui.Adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alegria.laboratorio.R
import com.alegria.laboratorio.databinding.RowExamsBinding
import com.alegria.laboratorio.io.response.UrlResponse
import com.alegria.laboratorio.ui.WebViewActivity
import com.alegria.laboratorio.util.PreferenceHelper
import com.alegria.laboratorio.util.PreferenceHelper.get
import com.alegria.laboratorio.util.download
import com.alegria.laboratorio.util.getDateFormatter
import retrofit2.Call
import com.alegria.laboratorio.model.Result
import retrofit2.Callback
import retrofit2.Response


class ExamAdapter : RecyclerView.Adapter<ExamAdapter.ViewHolder>() {
    var Exams = ArrayList<Result>()


    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val binding = RowExamsBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(exam: Result) {
            binding.tvNExam.text = "Examen # ${exam.id}"
            binding.typeExam.text = exam.type
            binding.medico.text = exam.doctor
            binding.fecha.text = getDateFormatter(exam.createdAt)
            binding.Orden.text = exam.orden
            binding.download.setOnClickListener {

                val apiService: com.alegria.laboratorio.io.ApiService by lazy {
                    com.alegria.laboratorio.io.ApiService.create()
                }
                val preferences by lazy {
                    PreferenceHelper.defaultPrefs(itemView.context.applicationContext)
                }
                val passport = preferences["passport", ""]
                val call = apiService.getUrlPdf("Bearer $passport", exam.id)
                call.enqueue(object : Callback<UrlResponse> {
                    override fun onResponse(
                        call: Call<UrlResponse>,
                        response: Response<UrlResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body().let { data ->

                                if (data != null) {
                                    download(data.url, itemView.context)
                                }

                            }

                        }
                    }

                    override fun onFailure(call: Call<UrlResponse>, t: Throwable) {
                        Toast.makeText(
                            itemView.context,
                            "${t.printStackTrace()}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                })
            }
            binding.preview.setOnClickListener {
                val apiService: com.alegria.laboratorio.io.ApiService by lazy {
                    com.alegria.laboratorio.io.ApiService.create()
                }
                val preferences by lazy {
                    PreferenceHelper.defaultPrefs(it.context.applicationContext)
                }
                val passport = preferences["passport", ""]
                val call = apiService.getUrlPdf("Bearer $passport", exam.id)
                call.enqueue(object : Callback<UrlResponse> {
                    override fun onResponse(
                        call: Call<UrlResponse>,
                        response: Response<UrlResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { data ->
                                val context = it.context
                                val intent = Intent(context, WebViewActivity::class.java)
                                intent.putExtra("url", data.url)
                                context.startActivity(intent)
                            }
                        }
                    }

                    override fun onFailure(call: Call<UrlResponse>, t: Throwable) {
                        Toast.makeText(
                            itemView.context,
                            "${t.printStackTrace()}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                })
            }

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_exams, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exam = Exams[position]
        holder.bind(exam)
    }

    override fun getItemCount() = Exams.size


}

