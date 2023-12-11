package com.dotjoo.aghsilinilaundry.ui.adapter.laundryInfo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghsilinilaundry.data.response.Laundry
import com.dotjoo.aghsilinilaundry.databinding.ItemRatesBinding

class RateLaundriesAdapter(
 ) : RecyclerView.Adapter< RateLaundriesAdapter. RateLaundriesViewHolder>() {

    var _binding: ItemRatesBinding? = null
    var ordersList = mutableListOf<Laundry>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RateLaundriesViewHolder {
        _binding = ItemRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return  RateLaundriesViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder:  RateLaundriesViewHolder, position: Int) {

        var currentItem = ordersList[position]


    }

    override fun getItemCount(): Int = ordersList.size

    class  RateLaundriesViewHolder(val binding: ItemRatesBinding) :
        RecyclerView.ViewHolder(binding.root)

}


