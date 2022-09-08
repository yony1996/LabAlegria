package com.alegria.laboratorio.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alegria.laboratorio.databinding.FragmentUserBinding
import com.alegria.laboratorio.io.response.InfoResponse
import com.alegria.laboratorio.util.PreferenceHelper
import com.alegria.laboratorio.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadInformation()
    }

    private fun loadInformation() {
        val passport = preferences["passport", ""]
        val call = apiService.getInformation("Bearer $passport")
        call.enqueue(object : Callback<InfoResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<InfoResponse>,
                response: Response<InfoResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        binding.tveEmail.text = it.email
                        binding.tveAge.text = it.age.toString()
                        if (it.gender == "M") {
                            binding.tveGender.text = "Masculino"
                        } else {
                            binding.tveGender.text = "Femenino"
                        }
                        binding.tveName.text = it.name
                        binding.tveLastName.text = it.last_name
                        binding.tvePhone.text = it.phone
                        binding.tveNui.text = it.nui
                    }

                }
            }

            override fun onFailure(call: Call<InfoResponse>, t: Throwable) {
                Toast.makeText(context, "${t.printStackTrace()}", Toast.LENGTH_SHORT).show()
            }
        })


    }
}