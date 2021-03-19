package com.rowicka.newthings.contacts.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.rowicka.newthings.contacts.model.ContactsInfo
import com.rowicka.newthings.contacts.phoneToColor
import com.rowicka.newthings.databinding.ItemContactBinding
import com.rowicka.newthings.utils.toPx


class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    private var contacts: List<ContactsInfo> = arrayListOf()
    private var onClickListener: OnRecyclerListener? = null

    fun setList(newList: List<ContactsInfo>) {
        contacts = newList
        notifyDataSetChanged()
    }

    fun setClickListener(listener: OnRecyclerListener) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contacts[position], onClickListener)
    }

    override fun getItemCount(): Int = contacts.size

    class ContactsViewHolder(itemView: ItemContactBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val image = itemView.itemContactImage
        private val name = itemView.itemContactName


        fun bind(item: ContactsInfo, onClickListener: OnRecyclerListener?) {
            itemView.setOnClickListener {
                onClickListener?.onClick(adapterPosition)
            }

            name.text = item.name

            item.photoUri?.let { setImageFromUri(it) }
                ?: setInitials(item.name, item.phone.phoneToColor())
        }

        private fun setImageFromUri(uri: Uri) {
            image.setImageURI(uri)
        }

        private fun setInitials(name: String, color: Int) {
            val nameList = name.split(" ").map { it[0] }

            val drawable = TextDrawable
                .builder()
                .beginConfig()
                .toUpperCase()
                .fontSize(20.toPx())
                .endConfig()
                .buildRound(
                    nameList.joinToString(""),
                    color
                )

            image.setImageDrawable(drawable)
        }
    }
}