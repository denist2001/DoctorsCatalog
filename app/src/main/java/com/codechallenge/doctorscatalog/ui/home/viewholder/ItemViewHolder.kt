package com.codechallenge.doctorscatalog.ui.home.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.codechallenge.doctorscatalog.R
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import com.codechallenge.doctorscatalog.databinding.DoctorItemBinding
import com.codechallenge.doctorscatalog.ui.home.MainFragmentDirections
import com.codechallenge.doctorscatalog.utils.clicks
import com.codechallenge.doctorscatalog.utils.throttleFirst
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ItemViewHolder(
    itemView: View,
    val clickFlowCollector: (doctor: Doctor, direction: NavDirections) -> Unit
) :
    RecyclerView.ViewHolder(itemView) {

    private var lastTime = 0L

    companion object {
        fun create(
            parent: ViewGroup,
            clickFlowCollector: (doctor: Doctor, direction: NavDirections) -> Unit
        ): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.doctor_item, parent, false)
            return ItemViewHolder(view, clickFlowCollector)
        }
    }

    fun bind(doctor: Doctor?) = with(itemView) {
        val binding = DoctorItemBinding.bind(itemView)
        if (doctor == null) return@with
        binding.pictureIv.load(doctor.picture) {
            scale(Scale.FIT)
            placeholder(R.drawable.doctor_icon)
            fallback(R.drawable.doctor_icon)
            transformations(CircleCropTransformation())
        }
        binding.nameTv.text = doctor.name
        binding.addressTv.text = doctor.address

        clicks().throttleFirst(1000L).onEach {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= 1000) {
                lastTime = currentTime
                val direction = MainFragmentDirections.actionMainFragmentToDetailsFragment(
                    name = doctor.name,
                    address = doctor.address,
                    picture = doctor.picture
                )
                clickFlowCollector(doctor, direction)
            }
        }.launchIn(MainScope())
    }
}