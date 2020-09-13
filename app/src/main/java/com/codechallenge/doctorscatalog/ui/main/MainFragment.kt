package com.codechallenge.doctorscatalog.ui.main

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.filter
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
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)
        linearLayoutManager = LinearLayoutManager(context)
        setupRecyclerView()
        setupSearchDoctor()
        startObserving()
    }

    private fun setupSearchDoctor() {
        var isSearchEnabled = false
        binding.searchDoctorBn.setOnClickListener { view ->
            isSearchEnabled = !isSearchEnabled
            if (isSearchEnabled) {
                binding.searchDoctorSv.visibility = VISIBLE
            } else {
                binding.searchDoctorSv.visibility = INVISIBLE
            }
        }
        binding.searchDoctorSv.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchDoctor(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // It's bad idea to send requests on each symbol
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        mainAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        with(doctors_list_rv) {
            adapter = mainAdapter
            layoutManager = linearLayoutManager
        }
        mainAdapter.setClickCollector { doctor ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= 1000) {
                lastTime = currentTime
                viewModel.addLastVisitedDoctor(doctor)
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
                mainAdapter.submitData(it.filter { doctor ->
                    !mainAdapter.visitedDoctorList.contains(doctor)
                })
            }
        }
        viewModel.visitedDoctorLiveData.observe(
            viewLifecycleOwner,
            { doctorList ->
                run {
                    viewLifecycleOwner.lifecycleScope.launch {
                        mainAdapter.setVisitedDoctorList(doctorList)
                    }
                }
            })
    }

    private fun searchDoctor(query: String) {
        val snapshot = mainAdapter.snapshot()
        var isFound = false
        for (index in 0..snapshot.lastIndex) {
            val currentDoctor = snapshot[index]
            currentDoctor?.let { doctor ->
                doctor.name?.let { name ->
                    if (name.contains(query, true)) {
                        linearLayoutManager.scrollToPositionWithOffset(index + 3, 20)
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