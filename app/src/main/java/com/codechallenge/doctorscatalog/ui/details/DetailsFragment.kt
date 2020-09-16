package com.codechallenge.doctorscatalog.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import coil.api.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.codechallenge.doctorscatalog.R
import com.codechallenge.doctorscatalog.databinding.DetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.details_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = requireArguments().getString("name", "")
        val address = requireArguments().getString("address", "")
        val picture = requireArguments().getString("picture")
        val binding = DetailsFragmentBinding.bind(view)
        binding.photoIv.load(picture) {
            scale(Scale.FIT)
            placeholder(R.drawable.doctor_icon)
            fallback(R.drawable.doctor_icon)
            transformations(RoundedCornersTransformation(16F))
        }
        binding.nameTv.text = name
        binding.addressTv.text = address
    }
}