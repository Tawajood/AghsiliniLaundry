package com.dotjoo.aghsilinilaundry.ui.fragment.settingFragments


import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.databinding.FragmentAboutBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back

@AndroidEntryPoint
class AboutFragment : BaseFragment<FragmentAboutBinding>() {
    lateinit var parent: MainActivity
    val mViewModel: SettingViewModel by viewModels()


    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            getAbout()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getAbout()
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }

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
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowAbout -> {
             binding.lytData.isVisible= true
                binding.tvDesc.setText(action.data.terms?.body)
            }


            else -> {

            }
        }
    }


    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.toolbar.card_back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}