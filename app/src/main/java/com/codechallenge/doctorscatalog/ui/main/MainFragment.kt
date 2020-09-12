package com.codechallenge.doctorscatalog.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.codechallenge.doctorscatalog.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment), LifecycleOwner {

    private val viewModel by viewModels<MainViewModel>()

}