package com.dotjoo.aghsilinilaundry.ui.fragment.auth.walkThrough

import com.bumptech.glide.Glide
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.databinding.ItemWalkthrougthBinding


class FirstFragment(var state: Int) : BaseFragment<ItemWalkthrougthBinding>() {


    private fun setUpUi() {

        when (state) {
            0 -> {
             binding.tvTitleSlider.text = getString(R.string.walk_title_1)
                binding.tvContentSlider.text =
                    getString(R.string.walk_msg1)

            }
            1 -> {
                binding.tvTitleSlider.text = getString(R.string.walk_title_2)
                binding.tvContentSlider.text =
                    getString(R.string.walk_msg2)

            }

        }

    }

    override fun onFragmentReady() {
        setUpUi()
    }
}