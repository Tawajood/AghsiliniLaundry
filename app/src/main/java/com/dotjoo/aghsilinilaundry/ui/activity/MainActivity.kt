package com.dotjoo.aghsilinilaundry.ui.activity

 import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseActivity
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.progress = baseShowProgress
        setupNavController()
    }

    fun showBottomNav(isVisible: Boolean) {
        binding.navView.isVisible = isVisible
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        var navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
        binding.navView.itemIconTintList = null




        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }



                R.id.accountFragment -> {
                    if (PrefsHelper.getToken().isNullOrEmpty()) {

                        navController.navigate(R.id.loginFirstBotomSheetFragment)
                    } else {
                        navController.navigate(
                            R.id.accountFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                        )
                    }
                    true
                }

                R.id.moreFragment -> {
                    navController.navigate(
                        R.id.moreFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                    )

                    true
                }

                else -> {
                    true
                }
            }
        }
    }

}