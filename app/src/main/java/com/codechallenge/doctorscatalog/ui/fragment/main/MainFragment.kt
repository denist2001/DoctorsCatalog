package com.codechallenge.doctorscatalog.ui.fragment.main

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
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
import com.codechallenge.doctorscatalog.ui.adapter.MainAdapter
import com.codechallenge.doctorscatalog.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment), LifecycleOwner {

    @Inject
    lateinit var mainAdapter: MainAdapter

    private lateinit var binding: MainFragmentBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel by viewModels<MainViewModel>()
    private var lastTime = 0L

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
                    mainAdapter.searchDoctor(query) { index ->
                        linearLayoutManager.scrollToPositionWithOffset(index + 3, 20)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    mainAdapter.searchDoctor(newText) { index ->
                        linearLayoutManager.scrollToPositionWithOffset(index + 3, 20)
                    }
                }
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        mainAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        with(binding.doctorsListRv) {
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
        viewLifecycleOwner.lifecycleScope.launch {
            mainAdapter.loadStateFlow.collectLatest { loadState ->
                val errorMessage = (loadState.refresh as? LoadState.Error)?.error?.message
                if (!errorMessage.isNullOrBlank()) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            @OptIn(FlowPreview::class)
            mainAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect() { binding.doctorsListRv.scrollToPosition(0) }
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
}