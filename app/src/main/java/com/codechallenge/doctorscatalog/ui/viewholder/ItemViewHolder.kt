package com.codechallenge.doctorscatalog.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.codechallenge.doctorscatalog.R
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import com.codechallenge.doctorscatalog.databinding.DoctorItemBinding
import com.codechallenge.doctorscatalog.utils.map.throttleFirst
import com.codechallenge.doctorscatalog.utils.view.clicks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ItemViewHolder(itemView: View, val clickFlowCollector: (doctor: Doctor) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup, clickFlowCollector: (doctor: Doctor) -> Unit): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.doctor_item, parent, false)
            return ItemViewHolder(view, clickFlowCollector)
        }
    }

    fun bind(item: Doctor?) = with(itemView) {
        val binding = DoctorItemBinding.bind(itemView)
        if (item == null) return@with
        binding.pictureIv.load(item.picture) {
            scale(Scale.FIT)
            placeholder(R.drawable.doctor_icon)
            fallback(R.drawable.doctor_icon)
            transformations(CircleCropTransformation())
        }
        binding.nameTv.text = item.name
        binding.addressTv.text = item.address

        clicks().throttleFirst(1000L).onEach {
            clickFlowCollector(item)
        }.launchIn(MainScope())
    }
}