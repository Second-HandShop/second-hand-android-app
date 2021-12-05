package com.android.secondhand

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.secondhand.apis.Constant
import com.squareup.picasso.Picasso
import io.swagger.server.models.Item

class ItemsRecyclerViewAdapter(val items: List<Item>): RecyclerView.Adapter<ItemsRecyclerViewAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_card, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemImage = itemView.findViewById<ImageView>(R.id.itemImage)
        val itemName = itemView.findViewById<TextView>(R.id.itemName)
        val itemPrice = itemView.findViewById<TextView>(R.id.itemPrice)

        fun bind(position: Int) {
            itemName.text = items[position].name
            itemPrice.text = items[position].price.toString()

            val url = Constant.CLOUDINARY_BASE_URL + (items[position].resources?.get(0)?.resourceLink
                ?: "")
            Picasso.get().load(url).error(R.mipmap.ic_launcher).fit().centerInside().into(itemImage)
        }
    }
}