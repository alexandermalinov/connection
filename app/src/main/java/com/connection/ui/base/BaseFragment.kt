package com.connection.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.connection.utils.navigation.Destination
import com.connection.utils.navigation.navigate

abstract class BaseFragment<T: ViewDataBinding> : Fragment() {

    protected lateinit var dataBinding: T

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

    abstract fun getLayoutId(): Int

    protected fun observeNavigation(navigationLiveData: LiveData<Destination>) {
        navigationLiveData.observe(viewLifecycleOwner) { destination ->
            navigate(destination)
        }
    }
}