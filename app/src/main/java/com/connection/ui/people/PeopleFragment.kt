package com.connection.ui.people

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentPeopleBinding
import com.connection.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeopleFragment : BaseFragment<FragmentPeopleBinding>() {

    private val viewModel: PeopleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPeopleRecyclerView()
        observeLiveData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_people

    private fun initPeopleRecyclerView() {
        dataBinding.recyclerViewPeoples.apply {
            adapter = PeopleAdapter()
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeLiveData() {
        observeUiLiveData()
        observeNavigation(viewModel.navigationLiveData)
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
            (dataBinding.recyclerViewPeoples.adapter as PeopleAdapter)
                .submitList(uiLiveData.peoples)
        }
    }
}