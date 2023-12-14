package com.dotjoo.aghsilinilaundry.ui.fragment.main.more

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.databinding.FragmentMoreBinding
import com.dotjoo.aghsilinilaundry.ui.activity.AuthActivity
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.dialog.BalanceWithdrawSheetFragment
import com.dotjoo.aghsilinilaundry.ui.dialog.ChangeDelteAccountSheetFragment
import com.dotjoo.aghsilinilaundry.ui.dialog.OnClickLoginFirst
import com.dotjoo.aghsilinilaundry.ui.fragment.settingFragments.SettingAction
import com.dotjoo.aghsilinilaundry.ui.fragment.settingFragments.SettingViewModel
import com.dotjoo.aghsilinilaundry.util.Constants
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.ext.showActivity
import com.dotjoo.aghsilinilaundry.util.observe
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>() {
    val mViewModel: SettingViewModel by viewModels()

    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            getWallet()
            observe(viewState) {
                handleViewState(it)
            }

        }
        if (PrefsHelper.getLanguage()==Constants.AR) {
            binding.tvLang.text="EN"
        }else{
            binding.tvLang.text="العربية"
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getWallet()
            binding.swiperefreshHome.isRefreshing = false
        }
    }

    @SuppressLint("SetTextI18n")
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
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowBalanceInWallet -> {
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.CEILING
                binding.tvCommestionValue.setText(df.format(action.data.commetion?.toDouble()).toString()+" " + resources.getString(R.string.sr))
                binding.tvProfitValue.setText(df.format(action.data.net_profit?.toDouble()).toString()+" " + resources.getString(R.string.sr))
                binding.tvTotalValue.setText(df.format(action.data.total_sales?.toDouble()).toString()+" " + resources.getString(R.string.sr))
                binding.tvBalanceValue.setText(df.format(action.data.balance?.toDouble()).toString()+" " + resources.getString(R.string.sr))
                binding.lytBalanceDetails.isVisible = true
            }

            else -> {

            }
        }
    }


    lateinit var parent: MainActivity
    private fun onClick() {

        binding.tvLogout.setPaintFlags(binding.tvLogout.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
        binding.tvSetting.setOnClickListener {
            if (PrefsHelper.getLanguage()==Constants.EN) {
                PrefsHelper.setLanguage(Constants.AR)
                showActivity(MainActivity::class.java, clearAllStack = true)
            } else {
                PrefsHelper.setLanguage(Constants.EN)
                showActivity(MainActivity::class.java, clearAllStack = true)
            }
        }

        binding.tvContactus.setOnClickListener {
            findNavController().navigate(R.id.contactUsFragment)
        }
        binding.tvAboutUs.setOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }

        binding.tvTerms.setOnClickListener {
            findNavController().navigate(R.id.termsFragment)
        }
        binding.tvNotifaction.setOnClickListener {
            findNavController().navigate(R.id.notifactionFragment)
        }
        binding.tvIt.setOnClickListener {
            findNavController().navigate(R.id.itFragment)
        }
  binding.tvWithdraw.setOnClickListener {
      BalanceWithdrawSheetFragment.newInstance(object : OnClickLoginFirst {
          override fun onClick(choice: String) {}
      }).show(childFragmentManager, BalanceWithdrawSheetFragment::class.java.canonicalName)
        }

        binding.tvLogout.setOnClickListener {
            PrefsHelper.clear()
            var intent = Intent(activity, AuthActivity::class.java)
            intent.putExtra(Constants.Start, Constants.login)
            activity?.startActivity(intent)
            activity?.finish()
        }

    }

}