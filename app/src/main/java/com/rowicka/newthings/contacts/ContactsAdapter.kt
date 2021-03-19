package com.rowicka.newthings.contacts

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
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

    fun setClickListener(listener: (Int) -> Unit) {
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
        private val image = itemView.itemContactImage
        private val name = itemView.itemContactName


        fun bind(item: ContactsInfo, position: Int, listener: (Int) -> Unit) {
            itemView.setOnClickListener {
                listener.invoke(position)
            }

            name.text = item.name

            item.photoUri?.let { setImageFromUri(it) }
                ?: setInitials(item.name, item.phone.toColor())
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

fun String.toColor(): Int {

    var phone = when {
        this.length > 7 -> this.substring(1, 8)
        this.length == 7 -> this.substring(1, 7) + "77"
        this.length == 6 -> this.substring(1, 6) + "666"
        else -> "9999999"
    }

    if (phone.startsWith("0")) {
        phone = phone.replaceFirst("0", "1")
    }

    phone = phone
        .replace(" ", "0")
        .replace("#", "0")
        .replace("*", "0")

    var color = "#${Integer.toString(phone.toInt(), 16)}"

    if (color.length == 7) {
        return Color.parseColor(color)
    }

    while (color.length != 7) {
        color += "0"
    }

    return Color.parseColor(color)
}