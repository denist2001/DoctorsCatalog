package com.codechallenge.doctorscatalog.ui.main

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codechallenge.doctorscatalog.R
import com.codechallenge.doctorscatalog.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment), LifecycleOwner {

    @Inject
    lateinit var mainAdapter: MainAdapter

    private lateinit var binding: MainFragmentBinding
    private val viewModel by viewModels<MainViewModel>()
    private var lastTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)

        setupRecyclerView()

        startObserving()
    }

    private fun setupRecyclerView() {
        mainAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        mainAdapter.setVisitedDoctorList(viewModel.getLastVisitedDoctors())//TODO
        with(doctors_list_rv) {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
        }
        mainAdapter.setClickCollector { doctor ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= 1000) {
                lastTime = currentTime
                viewModel.addLastVisitedDoctor(doctor)
                mainAdapter.setVisitedDoctorList(viewModel.getLastVisitedDoctors())
                val direction = MainFragmentDirections.actionMainFragmentToDetailsFragment(
                    name = doctor.name,
                    address = doctor.address,
                    picture = doctor.picture
                )
                findNavController().navigate(direction)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mainAdapter.loadStateFlow.collectLatest { loadState ->
                binding.progressPb.visibility = when (loadState.refresh) {
                    is LoadState.Loading -> VISIBLE
                    is LoadState.NotLoading -> INVISIBLE
                    is LoadState.Error -> INVISIBLE
                }
            }
        }
    }

    private fun startObserving() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.startLoading().collectLatest {
                mainAdapter.submitData(it)
            }
        }
    }
}