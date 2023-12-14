package com.dotjoo.aghsilinilaundry.ui.fragment.main.orderInfo

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseFragment
import com.dotjoo.aghsilinilaundry.data.response.OrderInfoItem
import com.dotjoo.aghsilinilaundry.data.response.OrderInfoResponse
import com.dotjoo.aghsilinilaundry.databinding.FragmentOrderInfoBinding
import com.dotjoo.aghsilinilaundry.ui.activity.MainActivity
import com.dotjoo.aghsilinilaundry.ui.adapter.OrderInfoItemsAdapter
import com.dotjoo.aghsilinilaundry.ui.dialog.EditBillSheetFragment
import com.dotjoo.aghsilinilaundry.ui.dialog.OnClickLoginFirst
import com.dotjoo.aghsilinilaundry.ui.fragment.main.home.OrderAction
import com.dotjoo.aghsilinilaundry.ui.fragment.main.home.OrderViewModel
import com.dotjoo.aghsilinilaundry.util.Constants
import com.dotjoo.aghsilinilaundry.util.SimpleDividerItemDecoration
import com.dotjoo.aghsilinilaundry.util.ext.hideKeyboard
import com.dotjoo.aghsilinilaundry.util.ext.init
import com.dotjoo.aghsilinilaundry.util.ext.loadImage
import com.dotjoo.aghsilinilaundry.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderInfoFragment : BaseFragment<FragmentOrderInfoBinding>() {
    val mViewModel: OrderViewModel by activityViewModels()
    lateinit var adapter: OrderInfoItemsAdapter
    var list = arrayListOf<OrderInfoItem>()
    var state: String? = null
    override fun onFragmentReady() {
        initAdapters()
        onClick()
        mViewModel.apply {

            mViewModel.orderId?.let { getOrderInfo(it) }
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
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

            is OrderAction.OrderInfo -> {
                loadOrderData(action.data)
            }

            is OrderAction.AcceptOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
            }

            is OrderAction.RejectOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
            }

            is OrderAction.ReciveOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
            }

            is OrderAction.startPrepOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
            }

            is OrderAction.EndPrepOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
            }

            is OrderAction.DeliverOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
                }

            else -> {

            }
        }
    }

    private fun loadOrderData(data: OrderInfoResponse) {
        data?.let {
            binding.lytData.isVisible = true
            adapter.ordersList = it.order?.orderitems!!
            adapter.notifyDataSetChanged()
            binding.tvDeliveryFeesValue.setText(it.order?.delivery + " " + resources.getString(R.string.sr))
            binding.cardCallClient.setOnClickListener {
                data?.order?.customer_phone?.let { it1 -> call(it1) }
            }
            binding.tvStatus.setText(it.order?.progress)
            binding.tvAddressUser.setText(it.order?.address)
            binding.tvAddValue.setText(it.order?.additional_cost + " " + resources.getString(R.string.sr))

            binding.tvClientName.setText(it.order?.customerName)
            binding.tvSubTotalValue.setText(it?.totalItemsPrice + " " + resources.getString(R.string.sr))
            binding.tvTotalValue.setText(it.order?.total + " " + resources.getString(R.string.sr))
            binding.tvTaxValue.setText(it.order?.tax + " " + resources.getString(R.string.sr))
            binding.tvUrgent.isVisible = (it.order?.argent == 1)
      if(it.order?.payment_type==1) binding.tvPayment.setText(R.string.wallet)
            handleStatus(it.order?.progress , it)
            state = it.order?.progress
        }
    }

    private fun handleStatus(progress: String?, orderInfoResponse: OrderInfoResponse) {
        when (progress) {
            Constants.NEW -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.order_status_new))
                binding.tvStatus.setText(resources.getString(R.string.new_order))
                binding.lytNewOrder.isVisible = true
                binding.lytIndelivery.isVisible = false
            }

            Constants.WAITING_DRIVER -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state2))
                binding.tvStatus.setText(resources.getString(R.string.waiting_driver))
                binding.lytNewOrder.isVisible = false
                binding.lytIndelivery.isVisible = true

            }

            Constants.DRIVER_IN_WAY -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state2))
                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))
                binding.lytNewOrder.isVisible = false
                binding.lytIndelivery.isVisible = true

                orderInfoResponse.order?.driver?.let {
                    binding.cardDelivery.isVisible= true
                    binding.tvDriverName.setText(it.name)
                    binding.cardCallDriver.setOnClickListener {
                        orderInfoResponse.order?.driver?.phone?.let { it1 -> call(it1) }
                    }
                }
            }
            Constants.DRIVER_IN_WAY_TO_LAUNDRY -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state5))
                binding.tvStatus.setText(resources.getString(R.string.preparied))
                 binding.lytIndelivery.isVisible = false
                binding.cardDelivery.isVisible = false
                binding.btnStatus.isVisible = true
                binding.btnStatus.setText(resources.getString(R.string.deliver))
                orderInfoResponse.order?.driver?.let {
                    binding.cardDelivery.isVisible= true
                    binding.tvDriverName.setText(it.name)
                    binding.cardCallDriver.setOnClickListener {
                        orderInfoResponse.order?.driver?.phone?.let { it1 -> call(it1) }
                    }
                }
            }

            Constants.DRIVER_RECIVE_FROM_CUSTOMER -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state2))
                binding.lytNewOrder.isVisible = false
                binding.lytIndelivery.isVisible = true

                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))
                orderInfoResponse.order?.driver?.let {
                    binding.cardDelivery.isVisible= true
                    binding.tvDriverName.setText(it.name)
                    binding.cardCallDriver.setOnClickListener {
                        orderInfoResponse.order?.driver?.phone?.let { it1 -> call(it1) }
                    }
                }
            }

            Constants.LAUNDRY_RECIVE -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state3))
                binding.tvStatus.setText(resources.getString(R.string.deliverd_to_laundry))
                binding.lytNewOrder.isVisible = false
                binding.lytIndelivery.isVisible = false
                binding.btnStatus.setText(resources.getString(R.string.start_prep))
                binding.btnStatus.isVisible = true
binding.cardDelivery.isVisible= false
            }

            Constants.START_PREPARE -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state4))
                binding.lytNewOrder.isVisible = false
                binding.lytIndelivery.isVisible = false
                binding.cardDelivery.isVisible = false
                binding.btnStatus.isVisible = true
                binding.btnStatus.setText(resources.getString(R.string.done_prep))
                binding.tvStatus.setText(resources.getString(R.string.preparied_now))

            }

            Constants.END_PREPARED -> {
                binding.lytNewOrder.isVisible = false

                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state5))
                binding.tvStatus.setText(resources.getString(R.string.preparied))
                binding.btnStatus.isVisible = false
                binding.lytIndelivery.isVisible = false
                binding.cardDelivery.isVisible = false
                binding.btnStatus.isVisible = true
                binding.btnStatus.setText(resources.getString(R.string.deliver))
            }

            Constants.DRIVER_RECIVE_FROM_LAUNDRY -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state5))
                binding.lytNewOrder.isVisible = false
                binding.lytIndelivery.isVisible = false

                binding.btnStatus.isVisible = false

                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))
                orderInfoResponse.order?.driver?.let {
                    binding.cardDelivery.isVisible= true
                    binding.tvDriverName.setText(it.name)
                    binding.cardCallDriver.setOnClickListener {
                        orderInfoResponse.order?.driver?.phone?.let { it1 -> call(it1) }
                    }
                }
            }

            Constants.COMPLETED -> {
                binding.lytNewOrder.isVisible = false
                binding.lytIndelivery.isVisible = false
                binding.btnStatus.isVisible = false
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state6))

                binding.tvStatus.setText(resources.getString(R.string.compeleted))
                binding.cardDelivery.isVisible= false
            }

            else -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state6))
                binding.lytNewOrder.isVisible = false

                binding.tvStatus.setText(resources.getString(R.string.compeleted))
            }
        }
    }

    lateinit var parent: MainActivity
    private fun onClick() {
        binding.tvEditBill.setPaintFlags(binding.tvEditBill.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)


        binding.cardBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAgreeNewOrder.setOnClickListener {}
        binding.btnAgreeNewOrder.setOnClickListener {
            mViewModel.orderId?.let { it1 -> mViewModel.acceptOrder(it1) }
        }
        binding.btnDeclineNewOrder.setOnClickListener {
            mViewModel.orderId?.let { it1 -> mViewModel.rejectOrder(it1) }
        }
        binding.btnDoneDelivering.setOnClickListener {
            mViewModel.orderId?.let { it1 -> mViewModel.reciveOrder(it1) }
        }
      binding.tvEditBill.setOnClickListener {
          mViewModel.orderId?.let { it1 ->
              EditBillSheetFragment.newInstance(object : OnClickLoginFirst {
                  override fun onClick(choice: String) {

                      mViewModel.orderId?.let {
                          mViewModel.getOrderInfo(it)
                      }
                  }
              } , it1).show(childFragmentManager, EditBillSheetFragment::class.java.canonicalName)
          }
      }

        binding.btnStatus.setOnClickListener {
            when (state) {
                Constants.LAUNDRY_RECIVE -> {
                    mViewModel.orderId?.let { it1 -> mViewModel.startPrepOrder(it1) }

                }

                Constants.START_PREPARE -> {
                    mViewModel.orderId?.let { it1 -> mViewModel.endPrepOrder(it1) }

                }

                Constants.END_PREPARED -> {
                    mViewModel.orderId?.let { it1 -> mViewModel.deliverOrder(it1) }

                }
                Constants.DRIVER_IN_WAY_TO_LAUNDRY -> {
                    mViewModel.orderId?.let { it1 -> mViewModel.deliverOrder(it1) }

                }
            }
        }
    }

    private fun initAdapters() {
        adapter = OrderInfoItemsAdapter()
        binding.rvOrders.init(requireContext(), adapter, 2)
        binding.rvOrders.addItemDecoration(SimpleDividerItemDecoration(requireContext()))
    }

    fun call(tel: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + tel)
        startActivity(dialIntent)
    }
}