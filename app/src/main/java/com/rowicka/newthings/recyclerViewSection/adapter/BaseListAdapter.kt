package com.rowicka.newthings.recyclerViewSection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.rowicka.newthings.databinding.ItemHeaderItemBinding
import com.rowicka.newthings.databinding.ItemProductBinding
import com.rowicka.newthings.recyclerViewSection.model.BinType
import java.time.format.DateTimeFormatter

const val ITEM_PRODUCT = 0
const val ITEM_HEADER = 1

class BaseListAdapter : RecyclerView.Adapter<BaseListAdapter.Holder>() {

    private val binItems = arrayListOf<BinType>()

    fun setList(list: ArrayList<BinType>) {
        binItems.addAll(list)
    }

    abstract class Holder(viewBinding: ViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        abstract fun bind(item: BinType)
    }

    class BinViewHolder(val binding: ItemProductBinding) : Holder(binding) {

        override fun bind(item: BinType) {
            val binItem = (item as BinType.Bin).value
            binding.name.text = binItem.productName
            binding.amount.text = binItem.amount.toString()
        }
    }

    class DateViewHolder(val binding: ItemHeaderItemBinding) : Holder(binding) {
        override fun bind(item: BinType) {
            val date = (item as BinType.Date).date
            binding.headerText.text = date.format(DateTimeFormatter.BASIC_ISO_DATE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return when (viewType) {
            ITEM_PRODUCT -> {
                val binding = ItemProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                BinViewHolder(binding)
            }
            else -> {
                val binding = ItemHeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DateViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = binItems.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(binItems[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (binItems[position]) {
            is BinType.Bin -> ITEM_PRODUCT
            else -> ITEM_HEADER
        }
    }
}
