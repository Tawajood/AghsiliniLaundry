package com.dotjoo.aghsilinilaundry.ui.lisener

import com.dotjoo.aghsilinilaundry.data.response.ItemsInService
import com.dotjoo.aghsilinilaundry.data.response.Order
import com.dotjoo.aghsilinilaundry.data.response.ServiceInLaundry

interface OnOrderClickListener {
    fun onItemsClickLisener(item: Order)

}
interface ServiceClickListener {
    fun onServiceClickLisener(item: ServiceInLaundry)

}

interface ItemsInLaundryClickListener {
    fun onItemsClickLisener(item: ItemsInService)
    fun onEditItemsClickLisener(item: ItemsInService )

}