package com.dotjoo.aghsilinilaundry.ui.fragment.main.edit_profile


import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.response.ItemsInService
import com.dotjoo.aghsilinilaundry.data.response.ServiceInLaundry
import com.dotjoo.aghsilinilaundry.data.response.ServiceResponse
import com.dotjoo.aghsilinilaundry.databinding.FragmentEditItemsBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.adapter.laundryInfo.ItemsAdapter
import com.dotjoo.aghsilinilaundry.ui.adapter.laundryInfo.ServiceAdapter
import com.dotjoo.aghsilinilaundry.ui.fragment.main.account.AccountAction
import com.dotjoo.aghsilinilaundry.ui.fragment.main.account.AccountViewModel
import com.dotjoo.aghsilinilaundry.ui.lisener.ItemsInLaundryClickListener
import com.dotjoo.aghsilinilaundry.ui.lisener.ServiceClickListener
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.ext.init
import com.dotjoo.aghsilinilaundry.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditItemsFragment : BaseFragment<FragmentEditItemsBinding>(), ServiceClickListener,
    ItemsInLaundryClickListener {
    companion object {
        const val AddAndUpdate = "AddAndUpdate"

        const val Add = 1
        const val Update = 2

    }

    private var currentList: ArrayList<ItemsInService> = arrayListOf()
    val mViewModel: AccountViewModel by activityViewModels()
    lateinit var parent: MainActivity
    lateinit var adapterServices: ServiceAdapter
    lateinit var adapterItems: ItemsAdapter
     override fun onFragmentReady() {
        onClick()
        initAdapters()
        mViewModel.apply {
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
                binding.tvService.setText(resources.getString(R.string.service) +    mViewModel.currentService?.name)

           mViewModel.currentService?.itemId?.let { it1 ->
                    mViewModel.getItemsInService(
                        it1
                    )
                }
            }

            is AccountAction.ShowItems -> {

                currentList = action.data.items
                loadItems(currentList, mViewModel.urgent)

            }

            is AccountAction.ShowItemsStored -> {


            }


            else -> {

            }
        }
    }

    private fun loadServices(data: ServiceResponse) {

        data.services?.get(0)?.choosen = true
        adapterServices.ordersList = data.services

        adapterServices.notifyDataSetChanged()

    }


    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.fab.setOnClickListener {

            findNavController().navigate(
                R.id.addUpdateItemFragment, bundleOf(AddAndUpdate to Add)
            )
        }
        binding.cardBack.setOnClickListener {
            findNavController().navigateUp()
        }


    }


    private fun initAdapters() {
        adapterServices = ServiceAdapter(this)
        adapterItems = ItemsAdapter(this)
        binding.rvItems.init(requireContext(), adapterItems, 2)
        binding.rvServices.init(requireContext(), adapterServices, 1)

    }

    private fun loadItems(list: ArrayList<ItemsInService>, urgent: Boolean) {
        if (list.size > 0) {
            adapterItems.ordersList = list
            adapterItems.urgent = urgent
            adapterItems.notifyDataSetChanged()
            binding.rvItems.isVisible = true

        } else {
            //       binding.lytEmptyState.isVisible= true
            //     binding.rvItems.isVisible= false

        }


    }

    var orderlist: HashMap<String, ItemsInService> = hashMapOf()
    var count = 0


    override fun onServiceClickLisener(item: ServiceInLaundry) {
        adapterItems.ordersList = arrayListOf()
        adapterItems.notifyDataSetChanged()
   mViewModel.currentService = item
        binding.tvService.setText(resources.getString(R.string.service) + item.name)
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
            R.id.addUpdateItemFragment, bundleOf(AddAndUpdate to Update, "Item" to item)
        )
    }


}
