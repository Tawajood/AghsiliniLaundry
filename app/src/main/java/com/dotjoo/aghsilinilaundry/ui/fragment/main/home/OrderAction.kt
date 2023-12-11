package com.dotjoo.aghsilinilaundry.ui.fragment.main.home

import com.dotjoo.aghsilinilaundry.base.Action
import com.dotjoo.aghsilinilaundry.data.response.AlOrdersResponse
import com.dotjoo.aghsilinilaundry.data.response.OrderInfoResponse

sealed class OrderAction : Action {

    data class ShowLoading(val show: Boolean) : OrderAction()
    data class ShowFailureMsg(val message: String?) : OrderAction()


    data class CurrentOrders(val data: AlOrdersResponse) : OrderAction()
    data class NewOrdersOrders(val data: AlOrdersResponse) : OrderAction()
    data class PrevOrdersOrders(val data: AlOrdersResponse) : OrderAction()
    data class OrderInfo(val data: OrderInfoResponse) : OrderAction()
    data class AcceptOrder(val msg: String) : OrderAction()
    data class RejectOrder(val msg: String) : OrderAction()
    data class ReciveOrder(val msg: String) : OrderAction()
    data class startPrepOrder(val msg: String) : OrderAction()
    data class EndPrepOrder(val msg: String) : OrderAction()
    data class DeliverOrder(val msg: String) : OrderAction()


}
