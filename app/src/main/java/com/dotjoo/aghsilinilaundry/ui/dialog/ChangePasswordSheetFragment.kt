package com.dotjoo.aghsilinilaundry.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.databinding.DialogChangePassSheetBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.fragment.main.account.AccountAction
import com.dotjoo.aghsilinilaundry.ui.fragment.main.account.AccountViewModel
import com.dotjoo.aghsilinilaundry.util.ToastUtils.Companion.showToast
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class ChangePasswordSheetFragment( ) :
    BottomSheetDialogFragment() {

    val mViewModel: AccountViewModel by viewModels()

    private lateinit var binding: DialogChangePassSheetBinding
    private lateinit var parent: MainActivity

    companion object {
        fun newInstance( ): ChangePasswordSheetFragment {
            val args = Bundle()
            val f = ChangePasswordSheetFragment( )
            f.arguments = args
            return f
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogChangePassSheetBinding.inflate(inflater)
        parent = requireActivity() as MainActivity
        onClick()
        mViewModel.apply {
             observe(viewState) {
                handleViewState(it)
            }

        }

        return binding.root
    }

    fun handleViewState(action: AccountAction) {
        when (action) {
            is AccountAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AccountAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }

            is AccountAction.ChangePassword -> {
                showToast(requireContext(), action.msg)
       dismiss()     }

            else -> {

            }
        }
    }

    private fun showProgress(show: Boolean) {
binding.progressBar.isVisible= show
    }

    private fun onClick() {
        binding.btnSignup.setOnClickListener {
            mViewModel.isValidParamsChangePass(
                binding.etPassword.text.toString(),
                binding.etNewpassword.text.toString(),
                binding.etPasswordConfim.text.toString(),
            )
        }
    }

    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet!!)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

}