package com.dotjoo.aghsilinilaundry.ui.fragment.main.home

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
 import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.response.Order
 import com.dotjoo.aghsilinilaundry.databinding.FragmentOrderBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.adapter.OrderAdapter
import com.dotjoo.aghsilinilaundry.ui.adapter.OrderType.CURRNET
import com.dotjoo.aghsilinilaundry.ui.adapter.OrderType.FINISHED
import com.dotjoo.aghsilinilaundry.ui.adapter.OrderType.NEW
import com.dotjoo.aghsilinilaundry.ui.lisener.OnOrderClickListener
import com.dotjoo.aghsilinilaundry.util.Resource
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.ext.init
import com.dotjoo.aghsilinilaundry.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding>()
    , OnOrderClickListener {
    lateinit var adapter: OrderAdapter
    val mViewModel: OrderViewModel by activityViewModels()
    var listNew = arrayListOf<Order>()
    var listCurrent = arrayListOf<Order>()
    var listPrev = arrayListOf<Order>()
 var state=NEW
    override fun onFragmentReady() {
        initAdapters()
        onClick()


        mViewModel.apply {
           getCurrentOrder()
           getNewOrders()
           getPrevOrders()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getCurrentOrder()
            mViewModel.getNewOrders()
            mViewModel.getPrevOrders()
            //mViewModel.getAllLaundries(lat,lang)
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
    }

    fun handleViewState(action: OrderAction) {
        when (action) {
            is OrderAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is OrderAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is OrderAction.NewOrdersOrders -> {
                listNew= action.data.orders
            if(state==NEW)    stateNew()

            }
            is OrderAction.CurrentOrders -> {
                listCurrent= action.data.orders
                if(state==CURRNET)    stateCurrent()
             }
   is OrderAction.PrevOrdersOrders -> {
                listPrev= action.data.orders
       if(state==FINISHED)   stateFinished()
            }


            else -> {

            }
        }
    }


    lateinit var parent: MainActivity
    private fun onClick() {
        binding.toolbar.card_back.isVisible = false
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
    }

    private fun initAdapters() {
        binding.toolbar.tv_title.setText(resources.getString(R.string.orders))
        adapter = OrderAdapter(this)
        binding.rvOrders.init(requireContext(), adapter, 2)


        setupClick()
        //observeData()
        //
      //  l


    }

    private fun setupClick() {
        binding.titleCurrent.setOnClickListener {
            stateCurrent()
        }
        binding.titleFinished.setOnClickListener {
            stateFinished()
        }
        binding.titleNew.setOnClickListener {
            stateNew()
        }
    }


    private fun stateCurrent() {
        binding.titleCurrent.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleNew.background = resources.getDrawable(R.color.white)
        binding.titleFinished.background = resources.getDrawable(R.color.white)
        binding.titleCurrent.setTextColor(resources.getColor(R.color.white))
        binding.titleNew.setTextColor(resources.getColor(R.color.blue))
        binding.titleFinished.setTextColor(resources.getColor(R.color.blue))
        loadLaundries(listCurrent, CURRNET)
state= CURRNET

    }

    private fun stateNew() {
        binding.titleCurrent.background = resources.getDrawable(R.color.white)
        binding.titleNew.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleFinished.background = resources.getDrawable(R.color.white)
        binding.titleCurrent.setTextColor(resources.getColor(R.color.blue))
        binding.titleNew.setTextColor(resources.getColor(R.color.white))
        binding.titleFinished.setTextColor(resources.getColor(R.color.blue))
        loadLaundries(listNew, NEW)
       state= NEW // adapter.ordersList = arrayListOf()
        //   loadLaundries(list, NEW)
    }

    private fun stateFinished() {
        binding.titleCurrent.background = resources.getDrawable(R.color.white)
        binding.titleNew.background = resources.getDrawable(R.color.white)
        binding.titleFinished.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleCurrent.setTextColor(resources.getColor(R.color.blue))
        binding.titleNew.setTextColor(resources.getColor(R.color.blue))
        binding.titleFinished.setTextColor(resources.getColor(R.color.white))
     state= FINISHED   //  adapter.ordersList = arrayListOf()
        loadLaundries(listPrev, FINISHED)        //  loadLaundries(list, FINISHED)
    }

    private fun loadLaundries(list: ArrayList<Order>, type: Int) {
        if (list.size > 0) {
            adapter.ordersList = list
            adapter.type = type
            adapter.notifyDataSetChanged()
            binding.lytEmptyState.isVisible= false
        } else {
            adapter.ordersList = list
            adapter.type = type
            adapter.notifyDataSetChanged()
            when (type) {

                NEW -> {
                    binding.tvMsgNoLaundries.setText(resources.getString(R.string.no_orders_new))
                }

                CURRNET -> {
                    binding.tvMsgNoLaundries.setText(resources.getString(R.string.no_orders_current))
                }

                FINISHED -> {
                    binding.tvMsgNoLaundries.setText(resources.getString(R.string.no_orders_before))

                }

            }
            binding.lytEmptyState.isVisible = true
        }

    }

    override fun onItemsClickLisener(item: Order) {
        mViewModel.orderId = item.id

        findNavController().navigate(R.id.orderInfoFragment)
    }

}
