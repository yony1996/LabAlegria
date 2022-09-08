package com.alegria.laboratorio.ui.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alegria.laboratorio.R
import com.alegria.laboratorio.databinding.RowAppoimentBinding
import com.alegria.laboratorio.model.Appoiment

class AppoimentAdapter : RecyclerView.Adapter<AppoimentAdapter.ViewHolder>() {
    var appointments = ArrayList<Appoiment>()

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val binding = RowAppoimentBinding.bind(view)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(turno: Appoiment) {
            binding.tvNAppoiment.text= "Turno # ${turno.id}"
            binding.typeExam.text = turno.type
            binding.hora.text = turno.scheduledTime
            binding.fecha.text = turno.scheduledDate
            if (turno.status == "1") {
                binding.ImVStatus.setBackgroundResource(R.drawable.shape_circle_orange)
            } else if(turno.status == "0") {
                binding.ImVStatus.setBackgroundResource(R.drawable.shape_circle_red)
            }else{
                binding.ImVStatus.setBackgroundResource(R.drawable.shape_circle_green)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_appoiment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appoiment = appointments[position]
        holder.bind(appoiment)
    }

    override fun getItemCount() = appointments.size
}