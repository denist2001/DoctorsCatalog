package com.codechallenge.doctorscatalog.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codechallenge.doctorscatalog.R
import com.codechallenge.doctorscatalog.databinding.HeaderRowBinding

class HeaderViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup): HeaderViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.header_row, parent, false)
            return HeaderViewHolder(view)
        }
    }

    fun bind(header: String) = with(itemView) {
        val binding = HeaderRowBinding.bind(itemView)
        binding.headerName.text = header
    }
}