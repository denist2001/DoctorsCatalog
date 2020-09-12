package com.codechallenge.doctorscatalog.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.codechallenge.doctorscatalog.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.details_fragment), LifecycleOwner {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = requireArguments().getString("name", "")
        val address = requireArguments().getString("address", "")
        val picture = requireArguments().getString("picture", "")
    }
}