package com.rowicka.newthings.contacts

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.rowicka.newthings.contacts.model.ContactsInfo
import com.rowicka.newthings.databinding.ItemContactBinding
import com.rowicka.newthings.utils.toPx


class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    private var contacts: List<ContactsInfo> = arrayListOf()
    private var onClickListener: (Int) -> Unit = {}

    fun setList(newList: List<ContactsInfo>) {
        contacts = newList
        notifyDataSetChanged()
    }

    fun setClickListener(listener: (Int) -> Unit){
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(contacts[position], position, onClickListener)
    }

    override fun getItemCount(): Int = contacts.size

    class ContactsViewHolder(itemView: ItemContactBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val generator = ColorGenerator.DEFAULT

        private val image = itemView.itemContactImage
        private val name = itemView.itemContactName


        fun bind(item: ContactsInfo, position: Int, listener: (Int) -> Unit) {
            itemView.setOnClickListener {
                listener.invoke(position)
            }

            name.text = item.name

            item.photoUri?.let { setImageFromUri(it) }
                ?: setInitials(item.name)
        }

        private fun setImageFromUri(uri: Uri) {
            image.setImageURI(uri)
        }

        private fun setInitials(name: String) {
            val nameList = name.split(" ").map {
                return@map if (it[0].isLetter()) {
                    it[0]
                } else {
                    ""
                }
            }

            val drawable = TextDrawable
                .builder()
                .beginConfig()
                .toUpperCase()
                .fontSize(20.toPx())
                .endConfig()
                .buildRound(
                    nameList.joinToString(""),
                    generator.randomColor
                )

            image.setImageDrawable(drawable)
        }
    }
}