package com.dotjoo.aghsilinilaundry.ui.activity

 import android.os.Bundle
 import androidx.activity.viewModels
 import androidx.core.view.isVisible
 import androidx.fragment.app.activityViewModels
 import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseActivity
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.databinding.ActivityMainBinding
 import com.dotjoo.aghsilinilaundry.ui.fragment.main.home.OrderViewModel
 import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    val mViewModel: OrderViewModel by viewModels()
    companion object{
        val MAIN_SCREEN_ACTION ="MAIN_SCREEN_ACTION"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.progress = baseShowProgress
        setupNavController()
        mViewModel.updateToken()
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