package com.android.secondhand.useritems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.secondhand.R
import com.android.secondhand.models.Item
import com.squareup.picasso.Picasso


class UserItemsRecyclerViewAdapter(val items: List<Item>): RecyclerView.Adapter<UserItemsRecyclerViewAdapter.ItemViewHolder>() {

    // myListener : ItemsFragment
    var myListener:ShowUserItemClickListener? = null
    interface ShowUserItemClickListener{
        fun onItemClick(item: Item)
    }

    fun setListener(listener : ShowUserItemClickListener){
        myListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_card, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemImage = itemView.findViewById<ImageView>(R.id.itemImage)
        val itemName = itemView.findViewById<TextView>(R.id.itemName)
        val itemPrice = itemView.findViewById<TextView>(R.id.itemPrice)

        init{
            itemView.setOnClickListener {
                myListener?.onItemClick(items[adapterPosition])
            }
        }

        fun bind(position: Int) {
            itemName.text = items[position].name
            itemPrice.text = items[position].price.toString()

            val url = (items[position].resources?.get(0)?.resourceLink
                ?: "")
            Picasso.get().load(url).error(R.mipmap.ic_launcher).fit().centerInside().into(itemImage)
        }
    }
}