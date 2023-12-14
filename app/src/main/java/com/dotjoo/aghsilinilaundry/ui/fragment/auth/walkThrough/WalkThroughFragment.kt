package com.dotjoo.aghsilinilaundry.ui.fragment.auth.walkThrough

 import android.graphics.Color
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.databinding.FragmentWalkThroughBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.util.ext.showActivity
import com.google.android.material.tabs.TabLayoutMediator

class WalkThroughFragment :  BaseFragment<FragmentWalkThroughBinding>() {

    private var pos = 0
    override fun onFragmentReady() {
        setupViewPager()
        onClick()

    }


    private fun setupViewPager() {


        var circularStatusView = binding.circularStatusView
        circularStatusView.setPortionsCount(2);
        circularStatusView.setPortionsColor(getResources().getColor(R.color.blue) )
             //set all portions color
      //   circularStatusView.setPortionSpacing(0);//set the spacing between portions
    //    circularStatusView.setPortionWidth(0F);//set portion width
        var  blueColorValue = Color.parseColor("#25AAE2")
        var  lightgreyColorValue = Color.parseColor("#F0F0F0")
        circularStatusView.setPortionColorForIndex(1, lightgreyColorValue)
                circularStatusView.setPortionColorForIndex(0, blueColorValue)




          val adapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
            binding.viewPager.currentItem = 0
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                super.onPageSelected(position)
                pos = position

                when (pos) {
                    0 -> {

binding.ivNext.isVisible= true
                        binding.tvStart.isVisible= false
                        circularStatusView.setPortionColorForIndex(1, lightgreyColorValue)
                        circularStatusView.setPortionColorForIndex(0, blueColorValue)
binding.ivWalk.setImageResource( R.drawable.i1)
                    }

                    1 -> {
                        binding.ivNext.isVisible= false
                        binding.tvStart.isVisible= true
                        circularStatusView.setPortionColorForIndex(1, blueColorValue)
                        circularStatusView.setPortionColorForIndex(0, blueColorValue)
                        binding.ivWalk.setImageResource( R.drawable.i2)
                    }


                }
            }
        })
    }

    private fun onClick() {
       binding.lytNext.setOnClickListener {
            if (pos == 0  )
                goNext()
            else
                gotoLMain()
        }
binding.tvSkip.setOnClickListener {
    gotoLMain()

}


    }


    private fun goNext() {
        if (pos == 0  ) {
            pos++
            binding.viewPager.currentItem = binding.viewPager.currentItem + 1
        }
    }

    private fun gotoLMain() {

        if (PrefsHelper.getToken().isNullOrEmpty()) { findNavController().navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
        )}
        else
            showActivity(MainActivity::class.java, clearAllStack = true)


    }
}

