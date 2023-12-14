package com.dotjoo.aghsilinilaundry.ui.fragment.auth.register


 import com.dotjoo.aghsilinilaundry.base.BaseFragment
   import com.dotjoo.aghsilinilaundry.databinding.FragmentWaitingActivationBinding
  import com.dotjoo.aghsilinilaundry.ui.activity.AuthActivity
 import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaitingActivationFragment : BaseFragment<FragmentWaitingActivationBinding>() {
     lateinit var parent: AuthActivity
    private fun onclick() {
        parent = requireActivity() as AuthActivity
      /*   mViewModel.order_id?.let {
            binding.tvId.setText(resources.getString(R.string.orderid_1234)+  mViewModel.order_id)
        }*/

    }




    override fun onFragmentReady() {
        onclick()
        binding.animationView.playAnimation()

     }

}