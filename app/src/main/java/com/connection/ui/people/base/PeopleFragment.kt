package com.connection.ui.people.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.connection.R
import com.connection.databinding.FragmentPeopleBinding
import com.connection.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeopleFragment : BaseFragment<FragmentPeopleBinding>() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: PeopleViewModel by viewModels()

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_people

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun observeLiveData() {
        observeNavigation(viewModel.navigationLiveData)
        observeTabs()
        observeSelectedTab()
    }

    private fun observeTabs() {
        dataBinding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.onTabClick(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // TODO("Not yet implemented")
            }
        })
    }

    private fun observeSelectedTab() {
        viewModel.selectedTab.observe(viewLifecycleOwner) { tabPosition ->
            dataBinding.tabs.getTabAt(tabPosition)?.select()
        }
    }
}