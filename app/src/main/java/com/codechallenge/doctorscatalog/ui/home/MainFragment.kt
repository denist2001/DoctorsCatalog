package com.codechallenge.doctorscatalog.ui.home

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codechallenge.doctorscatalog.R
import com.codechallenge.doctorscatalog.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    @Inject
    lateinit var mainAdapter: MainAdapter

    private lateinit var binding: MainFragmentBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)
        linearLayoutManager = LinearLayoutManager(context)
        setupRecyclerView()
        setupSearchDoctor()
        startObserving()
    }

    private fun setupSearchDoctor() {
        binding.searchDoctorBn.setOnClickListener {
            viewModel.searchDoctorPressed()
        }
        binding.searchDoctorSv.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewLifecycleOwner.lifecycleScope.launch {
                    searchDoctorBy(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewLifecycleOwner.lifecycleScope.launch {
                    searchDoctorBy(newText)
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
        mainAdapter.setClickCollector { doctor, direction ->
            viewModel.addLastVisitedDoctor(doctor)
            findNavController().navigate(direction)
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
            mainAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.doctorsListRv.scrollToPosition(0) }
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showSearchView.observe(
                viewLifecycleOwner,
                { visibility ->
                    binding.searchDoctorSv.visibility = visibility
                }
            )
        }
    }

    private suspend fun searchDoctorBy(query: String?) {
        if (!query.isNullOrEmpty()) {
            val index = mainAdapter.searchDoctor(query)
            if (index >= 0) {
                linearLayoutManager.scrollToPositionWithOffset(index + 3, 20)
            } else {
                Toast.makeText(requireContext(), "No matches found", Toast.LENGTH_LONG).show()
            }
        }
    }
}