package com.connection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.connection.databinding.FragmentAllTabsBinding
import com.connection.ui.alltabs.AllTabsViewModel
import com.connection.ui.base.BaseFragment

class AllTabsFragment : BaseFragment<FragmentAllTabsBinding>() {

    private val viewModel: AllTabsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutId(): Int = R.layout.fragment_all_tabs
}