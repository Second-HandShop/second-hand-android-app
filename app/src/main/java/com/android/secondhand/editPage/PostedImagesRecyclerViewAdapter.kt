package com.android.secondhand.editPage

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.secondhand.R
import com.squareup.picasso.Picasso

class PostedImagesRecyclerViewAdapter(val items: List<String>): RecyclerView.Adapter<PostedImagesRecyclerViewAdapter.ImageViewHolder>()  {


    lateinit var myListener : OnImageClickFromAdapter

    interface OnImageClickFromAdapter{
        fun onPostedImageLongClick(position: Int)
    }

    fun setClickListener(listener : OnImageClickFromAdapter){
        myListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_image_edit_page, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.item_image)

        init{
            imageView.setOnLongClickListener {
                myListener?.onPostedImageLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }

        fun bind(uri: String){
            Picasso.get().load(uri).error(R.mipmap.ic_launcher).fit().centerInside().into(imageView)
        }


    }
}