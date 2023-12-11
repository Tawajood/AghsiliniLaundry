package com.dotjoo.aghsilinilaundry.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.databinding.DialogBalanceWithdrawSheetBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.fragment.settingFragments.SettingAction
import com.dotjoo.aghsilinilaundry.ui.fragment.settingFragments.SettingViewModel
import com.dotjoo.aghsilinilaundry.util.ToastUtils.Companion.showToast
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class BalanceWithdrawSheetFragment(var onClick: OnClickLoginFirst) : BottomSheetDialogFragment() {

    val mViewModel: SettingViewModel by viewModels()
    private lateinit var binding: DialogBalanceWithdrawSheetBinding
    private lateinit var parent: MainActivity

    companion object {
        fun newInstance(onClick: OnClickLoginFirst): BalanceWithdrawSheetFragment {
            val args = Bundle()
            val f = BalanceWithdrawSheetFragment(onClick)
            f.arguments = args
            return f
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DialogBalanceWithdrawSheetBinding.inflate(inflater)
        parent = requireActivity() as MainActivity
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }

        }

        return binding.root
    }

    fun handleViewState(action: SettingAction) {
        when (action) {
            is SettingAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is SettingAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                } else {
                    showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowBalnaceWithdraw -> {
                action.message?.let { showToast(requireContext(), it) }
                dismiss()
            }

            else -> {

            }
        }
    }


    private fun onClick() {
        binding.btnWithdraw.setOnClickListener {
                 mViewModel.isVaildWithdraw(
                    binding.etAmount.text.toString(),
                    binding.etBank.text.toString(),
                    binding.etAccNumber.text.toString(),
                )
            }
    }


    private fun showProgress(show: Boolean) {
        binding.progressBar.isVisible = show
    }

    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

}