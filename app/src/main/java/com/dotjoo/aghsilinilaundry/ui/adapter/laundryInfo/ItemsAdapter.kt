package com.dotjoo.aghsilinilaundry.ui.adapter.laundryInfo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinilaundry.R
import com.dotjoo.aghsilinilaundry.data.PrefsHelper
import com.dotjoo.aghsilinilaundry.data.response.ItemsInService
import com.dotjoo.aghsilinilaundry.databinding.ItemItemsBinding
import com.dotjoo.aghsilinilaundry.ui.lisener.ItemsInLaundryClickListener
import com.dotjoo.aghsilinilaundry.util.Constants
import com.dotjoo.aghsilinilaundry.util.Constants.EN

class ItemsAdapter(
    private val listener: ItemsInLaundryClickListener
) : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    var _binding: ItemItemsBinding? = null
    var ordersList = mutableListOf<ItemsInService>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    var context: Context? = null
    var urgent: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        context = parent.context
        _binding = ItemItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {

        var currentItem = ordersList[position]
        holder.binding.tvEdit.setPaintFlags(holder.binding.tvEdit.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
if(PrefsHelper.getLanguage()==EN)        holder.binding.tvTitle.setText(currentItem.name?.en.toString())
else       holder.binding.tvTitle.setText(currentItem.name?.ar.toString())
        if (urgent) holder.binding.tvPrice.setText(
            currentItem.argentPrice.toString() +" "+ context?.getString(
                R.string.sr
            )
        )
        else holder.binding.tvPrice.setText(currentItem.price.toString()  +" "+  context?.getString(R.string.sr))
        holder.binding.tvEdit.setOnClickListener {
            listener.onEditItemsClickLisener(currentItem)
        }
     }

    fun updateItem(item: ItemsInService) {
        ordersList.indexOfFirst { item.id == it.id }.takeIf { it > -1 }?.let { pos ->
            ordersList[pos] = item
            notifyItemChanged(pos)
        }
    }

    override fun getItemCount(): Int = ordersList.size

    class ItemsViewHolder(val binding: ItemItemsBinding) : RecyclerView.ViewHolder(binding.root)

}


