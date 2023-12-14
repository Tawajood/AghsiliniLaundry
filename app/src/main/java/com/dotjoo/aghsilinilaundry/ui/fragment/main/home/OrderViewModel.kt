package com.dotjoo.aghsilinilaundry.ui.fragment.main.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.base.BaseViewModel
import com.dotjoo.aghsilinilaundry.data.param.EditOrderBillParam
import com.dotjoo.aghsilinilaundry.data.param.OrderInfoParam
import com.dotjoo.aghsilinilaundry.data.response.AlOrdersResponse
import com.dotjoo.aghsilinilaundry.data.response.Order
import com.dotjoo.aghsilinilaundry.data.response.OrderInfoResponse
import com.dotjoo.aghsilinilaundry.domain.OrderActionsUseCase
import com.dotjoo.aghsilinilaundry.domain.OrderActionsUseCase.OrderTypes.accept
import com.dotjoo.aghsilinilaundry.domain.OrderActionsUseCase.OrderTypes.deliver_order
import com.dotjoo.aghsilinilaundry.domain.OrderActionsUseCase.OrderTypes.end_prep
import com.dotjoo.aghsilinilaundry.domain.OrderActionsUseCase.OrderTypes.recive
import com.dotjoo.aghsilinilaundry.domain.OrderActionsUseCase.OrderTypes.reject
import com.dotjoo.aghsilinilaundry.domain.OrderActionsUseCase.OrderTypes.start_prep
import com.dotjoo.aghsilinilaundry.domain.OrderUseCase
import com.dotjoo.aghsilinilaundry.domain.OrderUseCase.OrderTypes.CURRENT
import com.dotjoo.aghsilinilaundry.domain.OrderUseCase.OrderTypes.NEWw
import com.dotjoo.aghsilinilaundry.domain.OrderUseCase.OrderTypes.PREV
import com.dotjoo.aghsilinilaundry.fcm.FcmUseCase
import com.dotjoo.aghsilinilaundry.util.NetworkConnectivity
import com.dotjoo.aghsilinilaundry.util.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel
@Inject constructor(
    app: Application,
    val useCase: OrderUseCase,
    val useCaseOrderActions: OrderActionsUseCase,
    val useCaseFcm: FcmUseCase,
 ) : BaseViewModel<OrderAction>(app) {

/*    private val _cuurentOrders =
        MutableStateFlow<Resource<ArrayList<Order>>>(Resource.Progress(true))
    val curre00000nt = _cuurentOrders.asStateFlow()

    private val _newOrders = MutableStateFlow<Resource<kotlin.collections.ArrayList<Order>>>(Resource.Progress(true))
    val new = _newOrders.asStateFlow()*/
var orderId:String? =null
init {
    getCurrentOrder()
    getNewOrders()
    getPrevOrders()
}
    fun getCurrentOrder() {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(OrderAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, CURRENT
                ) { res ->


                    when (res) {
                        is Resource.Failure -> {
                            produce(OrderAction.ShowFailureMsg(res.message.toString()))

                        }
                        is Resource.Progress -> {
                            produce(OrderAction.ShowLoading(res.loading))

                        }
                        is Resource.Success -> {
                            produce(OrderAction.CurrentOrders(res.data?.data as AlOrdersResponse))
                            }
                    }


                }
            }

        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getNewOrders() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, NEWw
                ) { res ->
                    when (res) {
                        is Resource.Failure -> {
                            produce(OrderAction.ShowFailureMsg(res.message.toString()))

                        }

                        is Resource.Progress -> {
                            produce(OrderAction.ShowLoading(res.loading))

                        }

                        is Resource.Success -> {
                            produce(OrderAction.NewOrdersOrders(res.data?.data as AlOrdersResponse))

                        }
                    }
                }}
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }    fun getPrevOrders() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, PREV
                ) { res ->
                    when (res) {
                        is Resource.Failure -> {
                            produce(OrderAction.ShowFailureMsg(res.message.toString()))

                        }

                        is Resource.Progress -> {
                            produce(OrderAction.ShowLoading(res.loading))

                        }

                        is Resource.Success -> {
                            produce(OrderAction.PrevOrdersOrders(res.data?.data as AlOrdersResponse))

                        }
                    }
                }}
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

   fun getOrderInfo(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, OrderInfoParam(orderID, null)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.OrderInfo(res.data?.data as OrderInfoResponse))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

   fun acceptOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, accept)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.AcceptOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
   fun rejectOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, reject)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.RejectOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun editBill(orderID: String ,   price :String,   notes :String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, EditOrderBillParam(orderID ,   price  ,   notes)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.BillEdited(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
   fun reciveOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, recive)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.ReciveOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun deliverOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, deliver_order)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.DeliverOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun startPrepOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, start_prep)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.startPrepOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
   fun endPrepOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, end_prep)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.EndPrepOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun updateToken(    ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            useCaseFcm.generateFcmToken()
        }
    }
}






