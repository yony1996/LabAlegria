package com.alegria.laboratorio.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alegria.laboratorio.databinding.FragmentExamBinding
import com.alegria.laboratorio.ui.Adapters.ExamAdapter
import com.alegria.laboratorio.util.PreferenceHelper
import com.alegria.laboratorio.util.PreferenceHelper.get
import retrofit2.Call
import com.alegria.laboratorio.model.Result

import retrofit2.Callback
import retrofit2.Response

class ExamFragment : Fragment() {
    private var _binding: FragmentExamBinding? = null
    private val binding get() = _binding!!
    private val examAdapter = ExamAdapter()
    private val apiService: com.alegria.laboratorio.io.ApiService by lazy {
        com.alegria.laboratorio.io.ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadExams()
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            loadExams()
        }
        binding.mRviExam.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.mRviExam.adapter = examAdapter

    }

    private fun loadExams() {
        val passport = preferences["passport", ""]
        val call = apiService.getResult("Bearer $passport")
        call.enqueue(object : Callback<ArrayList<Result>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ArrayList<Result>>,
                response: Response<ArrayList<Result>>
            ) {
                if (response.isSuccessful) {
                    if(response.body()!!.isEmpty()){
                        binding.mRviExam.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
                    }else{
                        binding.mRviExam.visibility = View.VISIBLE
                        binding.noData.visibility = View.GONE
                        response.body()?.let {
                            examAdapter.Exams = it
                            examAdapter.notifyDataSetChanged()
                        }
                    }


                }
            }

            override fun onFailure(call: Call<ArrayList<Result>>, t: Throwable) {
                Toast.makeText(context, "${t.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }
        })


    }
}