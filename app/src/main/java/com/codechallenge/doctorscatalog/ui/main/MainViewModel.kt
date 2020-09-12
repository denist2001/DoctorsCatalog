package com.codechallenge.doctorscatalog.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.codechallenge.doctorscatalog.network.RepositoryImpl

class MainViewModel @ViewModelInject constructor(
    val repository: RepositoryImpl
) : ViewModel() {
    // TODO: Implement the ViewModel
}