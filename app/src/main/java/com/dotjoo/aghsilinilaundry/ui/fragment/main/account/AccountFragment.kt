package com.dotjoo.aghsilinilaundry.ui.fragment.main.account

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.data.response.ItemsInService
import com.dotjoo.aghsilinilaundry.data.response.ServiceInLaundry
import com.dotjoo.aghsilinilaundry.data.response.ServiceResponse
import com.dotjoo.aghsilinilaundry.databinding.FragmentAccountBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.adapter.laundryInfo.ItemsAdapter
import com.dotjoo.aghsilinilaundry.ui.adapter.laundryInfo.ServiceAdapter
import com.dotjoo.aghsilinilaundry.ui.fragment.main.edit_profile.EditItemsFragment
import com.dotjoo.aghsilinilaundry.ui.lisener.ItemsInLaundryClickListener
import com.dotjoo.aghsilinilaundry.ui.lisener.ServiceClickListener
import com.dotjoo.aghsilinilaundry.util.Constants
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.ext.init
import com.dotjoo.aghsilinilaundry.util.ext.loadImage
import com.dotjoo.aghsilinilaundry.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(), ServiceClickListener,
    ItemsInLaundryClickListener {

    val mViewModel: AccountViewModel by activityViewModels()
    lateinit var parent: MainActivity

    private var currentList:  ArrayList<ItemsInService> = arrayListOf()
    lateinit var adapterServices: ServiceAdapter
    lateinit var adapterItems: ItemsAdapter
    var price = 0.0
    override fun onFragmentReady() {
        onClick()
        initAdapters()
         setupLaundry()

        mViewModel.apply {
            getProfileData()
            getServices()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getServices()
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
    }

    private fun setupLaundry() {
        PrefsHelper.getUserData()?.let {
            binding.tvLaundryName.setText(it.name)
            binding.tvAddress.setText(it.address)
              binding.tvRate.setText(it.rate)
            binding.ivLogo.loadImage(it.logo, isCircular = true)
        }
    }

    fun handleViewState(action: AccountAction) {
        when (action) {
            is AccountAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AccountAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is AccountAction.ShowServices -> {
                loadServices(action.data)
                  mViewModel.currentService = action.data.services.get(0)

                       mViewModel.currentService?.itemId?.let { it1 ->
                        mViewModel.getItemsInService(
                              it1
                        )   }
            }

            is AccountAction.ShowItems -> {

                 currentList = action.data.items
                loadItems(currentList )

            }

            is AccountAction.ShowProfile -> {
                action.data.laundry?.let { PrefsHelper.saveUserData(it) }
                setupLaundry()
            }

            else -> {

            }
        }
    }

    private fun loadServices(data: ServiceResponse) {

        data.services?.get(0)?.choosen = true
        adapterServices.ordersList = data.services

        if(PrefsHelper.getLanguage()==Constants.EN)        binding.tvService.setText(data.services.get(0)?.name+" "+resources.getString(R.string.service)  )
        else             binding.tvService.setText(resources.getString(R.string.service) +   " "+data.services.get(0)?.name)

        adapterServices.notifyDataSetChanged()

    }


    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
        binding.titleRegular.setOnClickListener {
            stateRegular()
        }
        binding.cardBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.titleUrgent.setOnClickListener {
            stateUrgent()
        }
       binding.btnEditProfile.setOnClickListener {
findNavController().navigate(R.id.editProfileFragment)        }
    }

    private fun stateUrgent() {
        binding.titleRegular.background = resources.getDrawable(R.color.white)
        binding.titleUrgent.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleUrgent.setTextColor(resources.getColor(R.color.white))
        binding.titleRegular.setTextColor(resources.getColor(R.color.blue))
         mViewModel.urgent = true
        loadItems(currentList )

        //  loadLaundries(list)
    }

    private fun stateRegular() {
        binding.titleUrgent.background = resources.getDrawable(R.color.white)
        binding.titleRegular.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleRegular.setTextColor(resources.getColor(R.color.white))
        binding.titleUrgent.setTextColor(resources.getColor(R.color.blue))
         mViewModel.urgent = false
        loadItems(currentList )
//   loadLaundries(list)
    }

    private fun initAdapters() {
        adapterServices = ServiceAdapter(this)
        adapterItems = ItemsAdapter(this)

        binding.rvItems.init(requireContext(), adapterItems, 2)
        binding.rvServices.init(requireContext(), adapterServices, 1)

    }

    private fun loadItems(list: ArrayList<ItemsInService> ) {
        if (list.size > 0) {
            adapterItems.ordersList = list
            adapterItems.urgent=  mViewModel.urgent
            adapterItems.notifyDataSetChanged()
            binding.lytEmptyState.isVisible= false
            binding.rvItems.isVisible= true

        } else {

        }


    }



    override fun onServiceClickLisener(item: ServiceInLaundry) {
        adapterItems.ordersList = arrayListOf()
        adapterItems.notifyDataSetChanged()
        if(PrefsHelper.getLanguage()==Constants.EN)        binding.tvService.setText(item?.name +" "+resources.getString(R.string.service)  )
        else           binding.tvService.setText(resources.getString(R.string.service) +   " "+item?.name)


        mViewModel.currentService = item

              mViewModel.currentService?.itemId?.let { it1 ->
                mViewModel.getItemsInService(
                    it1
                )

        }
    }

    override fun onItemsClickLisener(item: ItemsInService) {
     }

    override fun onEditItemsClickLisener(item: ItemsInService) {
        mViewModel.itemsInService = item
        findNavController().navigate(
            R.id.addUpdateItemFragment, bundleOf(EditItemsFragment.AddAndUpdate to EditItemsFragment.Update, "Item" to item)
        )
    }


}