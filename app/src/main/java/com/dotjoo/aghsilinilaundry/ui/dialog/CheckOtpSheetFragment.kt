package com.dotjoo.aghsilinilaundry.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
 import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
 import com.dotjoo.aghsilinilaundry.databinding.DialogCheckOtpBinding
 import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghsilinilaundry.ui.fragment.auth.login.AuthViewModel
 import com.dotjoo.aghsilinilaundry.util.ToastUtils
import com.dotjoo.aghsilinilaundry.util.ToastUtils.Companion.showToast
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
 import com.dotjoo.aghsilinilaundry.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

interface OnPhoneCheckedWithOtp{
    fun onClick(country_code: String,phone: String,verifed: Boolean )
}
@AndroidEntryPoint
class CheckOtpSheetFragment(val country_code: String, val phone: String , val onClick:OnPhoneCheckedWithOtp ) :  BottomSheetDialogFragment() {

    private lateinit var binding: DialogCheckOtpBinding
     val mViewModel: AuthViewModel by viewModels()

    companion object {
        fun newInstance(country_code: String, phone: String,onClick:OnPhoneCheckedWithOtp  ): CheckOtpSheetFragment {
            val args = Bundle()
            val f = CheckOtpSheetFragment(country_code ,phone , onClick
            )
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogCheckOtpBinding.inflate(inflater)
         onClick()
        mViewModel.apply {
            mViewModel.checkPhone(country_code, phone)
            observe(viewState) {
                handleViewState(it)
            }

        }

        return binding.root
    }

    fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    ToastUtils.showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }


            is AuthAction.OtpChecked -> {
                 showProgress(false)
                onClick.onClick(country_code, phone, true)
                dismiss()

            }


            is AuthAction.PhoneChecked -> {
                counterDawn()
                showProgress(false)

            }



            else -> {

            }
        }
    }
    private var restTimer: CountDownTimer? = null
    private fun counterDawn() {
        binding.tvResend.isEnabled = false
        binding.tvResend.setTextColor(resources.getColor(R.color.grey_400))
        binding.tvTimer.setTextColor(resources.getColor(R.color.grey_400))

        restTimer = object : CountDownTimer(120000, 1000) {


            override fun onTick(millisUntilFinished: Long) {
                val seconds: Long = millisUntilFinished / 1000 % 60
                val minutes: Long = (millisUntilFinished - seconds) / 1000 / 60
                binding.tvTimer.text = "" + minutes + ":" + seconds
                Log.d(
                    "remainingremaining g", ("" + minutes + ":" + seconds).toString()
                )
            }


            override fun onFinish() {
                binding.tvResend.setText(resources.getString(R.string.resend))
                binding.tvTimer.text = ""
                binding.tvResend.isEnabled = true
                binding.tvResend.setTextColor(resources.getColor(R.color.black))


            }
        }.start()
    }


    private fun showProgress(show: Boolean) {
        binding.progressBar.isVisible= show
    }


    private fun onClick() {
        binding.tvResend.setOnClickListener {
         mViewModel.checkPhone(country_code,phone)
        }
        binding.btnEnterOtp.setOnClickListener {
            if (binding.etOtp.otp.toString()
                    .isNullOrEmpty()
            ) showToast(requireContext(), resources.getString(R.string.msg_empty_otp))
            else {
                mViewModel.otp = binding.etOtp.otp.toString()
                mViewModel.checkOtp(
                    country_code, phone, mViewModel.otp.toString()
                )
            }
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