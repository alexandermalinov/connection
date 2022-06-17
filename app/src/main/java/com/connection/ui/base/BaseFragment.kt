package com.connection.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.connection.menu.MenuUiModel
import com.connection.menu.showMenu
import com.connection.navigation.Destination
import com.connection.navigation.navigate

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    protected lateinit var dataBinding: T

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            getLayoutId(),
            container,
            false
        )
        return dataBinding.root
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
    ---------------------------------------------------------------------------------------------*/
    abstract fun getLayoutId(): Int

    /* --------------------------------------------------------------------------------------------
     * Protected
    ---------------------------------------------------------------------------------------------*/
    protected fun observeNavigation(navigationLiveData: LiveData<Destination>) {
        navigationLiveData.observe(viewLifecycleOwner) { destination ->
            navigate(destination)
        }
    }

    protected fun observeMenuLiveData(
        menuLiveData: LiveData<MenuUiModel>,
        menuIcon: View
    ) {
        menuLiveData.observe(viewLifecycleOwner) { menu ->
            requireActivity().showMenu(menu, menuIcon)
        }
    }
}