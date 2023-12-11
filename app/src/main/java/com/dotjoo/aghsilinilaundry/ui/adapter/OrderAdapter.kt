package com.dotjoo.aghsilinilaundry.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.data.response.Order
import com.dotjoo.aghsilinilaundry.databinding.ItemOrderBinding
import com.dotjoo.aghsilinilaundry.ui.adapter.OrderType.NEW
import com.dotjoo.aghsilinilaundry.ui.lisener.OnOrderClickListener

object OrderType {
    val NEW = 0
    val CURRNET = 1
    val FINISHED = 2

}

class OrderAdapter(
   private val listener: OnOrderClickListener
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    lateinit var context: Context
    var _binding: ItemOrderBinding? = null
    var ordersList = mutableListOf<Order>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    var type: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        _binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return OrderViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: OrderAdapter.OrderViewHolder, position: Int) {
        var currentItem = ordersList[position]
holder.binding.tvId.setText("#${currentItem.id}")
holder.binding.tvDate.setText("#${currentItem.created_at}")
holder.binding.tvItems.setText("${currentItem.items_count}"+context.resources.getString(R.string.items))
holder.binding.tvUrgent.isVisible=(currentItem.argent==1)
        holder.binding.root.setOnClickListener {
        listener.onItemsClickLisener(currentItem)
        }
        when (type) {
            NEW -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.new_order)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }

            OrderType.CURRNET -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.driver_on_way)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.orange))
            }

            OrderType.FINISHED -> {
                holder.binding.tvStatus.setText(
                    context.resources.getString(R.string.deliverd)
                )
                holder.binding.tvStatus.setTextColor(context.resources.getColor(R.color.blue))
            }
        }
    }


    override fun getItemCount(): Int = ordersList.size

    class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)


}


