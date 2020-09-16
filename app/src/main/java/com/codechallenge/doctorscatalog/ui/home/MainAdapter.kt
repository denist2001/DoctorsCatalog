package com.codechallenge.doctorscatalog.ui.home

import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import com.codechallenge.doctorscatalog.ui.home.viewholder.HeaderViewHolder
import com.codechallenge.doctorscatalog.ui.home.viewholder.ItemViewHolder
import com.codechallenge.doctorscatalog.ui.home.viewholder.VisitedViewHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class MainAdapter @Inject constructor() :
    PagingDataAdapter<Doctor, RecyclerView.ViewHolder>(DiffCallback()) {

    var visitedDoctorList: ArrayList<Doctor> = ArrayList()
    private lateinit var clickFlowCollector: (doctor: Doctor, direction: NavDirections) -> Unit

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.HEADER_ROW.ordinal -> (holder as HeaderViewHolder).bind("Recent Doctors")
            ViewType.HEADER_VIEW.ordinal -> (holder as VisitedViewHolder).bind(visitedDoctorList)
            ViewType.DOCTOR_ROW.ordinal -> (holder as HeaderViewHolder).bind("Vivy Doctors")
            else -> (holder as ItemViewHolder).bind(getItem(position - 3))
        }
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.HEADER_ROW.ordinal -> HeaderViewHolder.create(parent)
            ViewType.HEADER_VIEW.ordinal -> VisitedViewHolder.create(parent, clickFlowCollector)
            ViewType.DOCTOR_ROW.ordinal -> HeaderViewHolder.create(parent)
            else -> ItemViewHolder.create(parent, clickFlowCollector)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.HEADER_ROW.ordinal
            1 -> ViewType.HEADER_VIEW.ordinal
            2 -> ViewType.DOCTOR_ROW.ordinal
            else -> ViewType.DOCTOR_VIEW.ordinal
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 3 //header visited doctors + visited doctors + header Vivy doctors
    }

    fun setClickCollector(clickCollector: (doctor: Doctor, direction: NavDirections) -> Unit) {
        clickFlowCollector = clickCollector
    }

    suspend fun setVisitedDoctorList(newVisitedDoctorList: List<Doctor>) {
        val snapshot = ArrayList<Doctor>()
        for (doctor in snapshot()) {
            doctor?.let { snapshot.add(it) }
        }
        visitedDoctorList.clear()
        visitedDoctorList.addAll(newVisitedDoctorList)
        var index = -1
        for (doctor in visitedDoctorList) {
            if (snapshot.contains(doctor)) {
                index = snapshot.indexOf(doctor)
                snapshot.removeAt(index)
            }
        }
        if (index >= 0) {
            submitData(PagingData.from(snapshot))
            notifyItemChanged(index + 3)
        }
    }

    fun searchDoctor(query: String, scrollTo: (position: Int) -> Unit) {
        val snapshot = snapshot()
        var isFound = false
        for (index in 0..snapshot.lastIndex) {
            val currentDoctor = snapshot[index]
            currentDoctor?.let { doctor ->
                doctor.name?.let { name ->
                    if (name.contains(query, true)) {
                        scrollTo.invoke(index)
                        isFound = true
                        return@let
                    }
                }
                if (isFound) return@let
            }
            if (isFound) return
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Doctor>() {
    override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Doctor,
        newItem: Doctor
    ): Boolean {
        return oldItem == newItem
    }
}

enum class ViewType {
    HEADER_ROW,
    HEADER_VIEW,
    DOCTOR_ROW,
    DOCTOR_VIEW
}