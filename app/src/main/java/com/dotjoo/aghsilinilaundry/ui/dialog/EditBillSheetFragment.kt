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
import com.dotjoo.aghsilinilaundry.databinding.DialogEditBillSheetBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.fragment.main.home.OrderAction
import com.dotjoo.aghsilinilaundry.ui.fragment.main.home.OrderViewModel
import com.dotjoo.aghsilinilaundry.util.ToastUtils
import com.dotjoo.aghsilinilaundry.util.ToastUtils.Companion.showToast
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditBillSheetFragment(var onClick: OnClickLoginFirst, val orderID:String) : BottomSheetDialogFragment() {

    val mViewModel: OrderViewModel by viewModels()
    private lateinit var binding: DialogEditBillSheetBinding
    private lateinit var parent: MainActivity

    companion object {
        fun newInstance(onClick: OnClickLoginFirst , orderID:String): EditBillSheetFragment {
            val args = Bundle()
            val f = EditBillSheetFragment(onClick , orderID)
            f.arguments = args
            return f
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DialogEditBillSheetBinding.inflate(inflater)
        parent = requireActivity() as MainActivity
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }

        }

        return binding.root
    }

    fun handleViewState(action: OrderAction) {
        when (action) {
            is OrderAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is OrderAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                } else {
                    showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }

            is OrderAction.BillEdited -> {
                action.msg?.let { showToast(requireContext(), it) }
                onClick.onClick("")
                dismiss()
            }

            else -> {

            }
        }
    }


    private fun onClick() {
        binding.btnWithdraw.setOnClickListener {
            if(binding.etAmount.text.isNullOrEmpty())   ToastUtils.showToast(requireContext(), resources.getString(R.string.enter_amount))
      else  if(binding.etNote.text.isNullOrEmpty())   ToastUtils.showToast(requireContext(), resources.getString(R.string.enter_notes))
      else  {

                mViewModel.editBill(
                    orderID,
                    binding.etAmount.text.toString(),
                    binding.etNote.text.toString(),
                )
            }
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