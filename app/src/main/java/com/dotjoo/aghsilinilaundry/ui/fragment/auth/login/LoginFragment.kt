package com.dotjoo.aghsilinilaundry.ui.fragment.auth.login

import android.graphics.Paint
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.databinding.FragmentLoginBinding
import com.dotjoo.aghsilinilaundry.ui.activity.AuthActivity
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.ext.showActivity
import com.dotjoo.aghsilinilaundry.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.iv_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    lateinit var parent: AuthActivity
    val mViewModel: AuthViewModel by viewModels()

    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
    }

    private fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.LoginSuccess -> {
                showProgress(false)
                PrefsHelper.saveToken(action.data.token)
                PrefsHelper.saveUserData(action.data.laundry)
                goHome()

            }


            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    showToast(it.substring(3, it.length))
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            else -> {

            }
        }
    }


    private fun onClick() {
        parent = requireActivity() as AuthActivity
        binding.toolbar.tv_title.setText(resources.getString(R.string.login))
        binding.tvForgetpass.setPaintFlags(binding.tvForgetpass.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        binding.btnSignin.setOnClickListener {
            mViewModel.isVaildLogin(
                binding.etLaundryName.text.toString(),
                binding.etPassword.text.toString(),
                "+${binding.countryCodePicker.selectedCountryCode}",
            )
            //  action.data.client?.social= action.social
            // PrefsHelper.saveToken("ction.data.token")
            //  PrefsHelper.saveUserData(action.data)
            //  PrefsHelper.saveToken(action.data.client?.token)
            //    goHome()
        }
        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)

        }
        binding.tvForgetpass.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)

        }
        binding.toolbar.iv_back.setOnClickListener {
            activity?.finish()
        }
    }

    fun goHome() {
        showActivity(MainActivity::class.java, clearAllStack = true)

    }
}