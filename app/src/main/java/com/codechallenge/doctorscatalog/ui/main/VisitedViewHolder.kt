package com.codechallenge.doctorscatalog.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.codechallenge.doctorscatalog.R
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import com.codechallenge.doctorscatalog.databinding.HeaderDoctorItemBinding
import com.codechallenge.doctorscatalog.utils.map.throttleFirst
import com.codechallenge.doctorscatalog.utils.view.clicks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class VisitedViewHolder(itemView: View, val clickFlowCollector: (doctor: Doctor) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(
            parent: ViewGroup,
            clickFlowCollector: (doctor: Doctor) -> Unit
        ): VisitedViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.header_doctor_item, parent, false)
            return VisitedViewHolder(view, clickFlowCollector)
        }
    }

    fun bind(visitedDoctorList: List<Doctor>) = with(itemView) {
        if (visitedDoctorList.isEmpty()) return@with
        val binding = HeaderDoctorItemBinding.bind(itemView)
        //1st doctor
        val doctor = visitedDoctorList[0]
        setupShortInfo(doctor, binding.doctor1Iv, binding.doctor1Name)
        //2nd doctor
        if (visitedDoctorList.size > 1) {
            val doctor = visitedDoctorList[1]
            setupShortInfo(doctor, binding.doctor2Iv, binding.doctor2Name)
        }
        //3rd doctor
        if (visitedDoctorList.size > 2) {
            val doctor = visitedDoctorList[2]
            setupShortInfo(doctor, binding.doctor3Iv, binding.doctor3Name)
        }
    }

    private fun setupShortInfo(doctor: Doctor, icon: ImageView, name: TextView) {
        icon.load(doctor.picture) {
            scale(Scale.FIT)
            placeholder(R.drawable.doctor_icon)
            fallback(R.drawable.doctor_icon)
            transformations(CircleCropTransformation())
        }
        icon.clicks().throttleFirst(1000L).onEach {
            clickFlowCollector(doctor)
        }.launchIn(MainScope())
        name.text = doctor.name
    }
}