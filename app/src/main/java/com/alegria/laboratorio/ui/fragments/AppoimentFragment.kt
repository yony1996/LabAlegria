package com.example.lact

import android.annotation.SuppressLint
import android.widget.Toast


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alegria.laboratorio.databinding.FragmentAppoimentBinding
import com.alegria.laboratorio.model.Appoiment
import com.alegria.laboratorio.ui.Adapters.AppoimentAdapter
import com.alegria.laboratorio.util.PreferenceHelper
import com.alegria.laboratorio.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppoimentFragment : Fragment() {
    private var _binding: FragmentAppoimentBinding? = null
    private val binding get() = _binding!!
    private val appointmentAdapter = AppoimentAdapter()


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
        _binding = FragmentAppoimentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAppointments()
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            loadAppointments()
        }
        binding.mRviAppoiment.layoutManager =
            LinearLayoutManager(requireContext().applicationContext)
        binding.mRviAppoiment.adapter = appointmentAdapter

    }

    private fun loadAppointments() {
        val passport = preferences["passport", ""]
        val call = apiService.getAppointments("Bearer $passport")
        call.enqueue(object : Callback<ArrayList<Appoiment>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ArrayList<Appoiment>>,
                response: Response<ArrayList<Appoiment>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.isEmpty()) {
                        binding.mRviAppoiment.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
                    } else {
                        binding.mRviAppoiment.visibility = View.VISIBLE
                        binding.noData.visibility = View.GONE
                        response.body()?.let {
                            appointmentAdapter.appointments = it
                            appointmentAdapter.notifyDataSetChanged()
                        }
                    }


                }
            }

            override fun onFailure(call: Call<ArrayList<Appoiment>>, t: Throwable) {
                Toast.makeText(context, "${t.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }
        })


    }
}