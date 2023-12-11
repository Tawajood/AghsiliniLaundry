package com.dotjoo.aghsilinilaundry.ui.fragment.auth.splash

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.databinding.FragmentSplashBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.util.ext.showActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay

class SplashFragment : BaseFragment<FragmentSplashBinding>() {


    override fun onFragmentReady() {
        lifecycleScope.launchWhenResumed {
            delay(1500)
            //  Handler(Looper.getMainLooper()).postDelayed({

          //
            if (PrefsHelper.getLoggedinBefore()  ) {
                if (PrefsHelper.getToken().isNullOrEmpty()) { findNavController().navigate(
                    R.id.loginFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
                )}
                else
                    showActivity(MainActivity::class.java, clearAllStack = true)
            } else {

                PrefsHelper.setLoggedInBefore(true)
                findNavController().navigate(
                    R.id.walkThroughFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
                )  }
        }
    }




    override fun onPause() {
        lifecycleScope.cancel()
        super.onPause()
    }

}
