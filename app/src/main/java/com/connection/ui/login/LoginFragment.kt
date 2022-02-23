package com.connection.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.connection.R
import com.connection.databinding.FragmentLoginBinding
import com.connection.ui.base.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutId(): Int = R.layout.fragment_login
}