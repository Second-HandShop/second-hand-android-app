package com.android.secondhand

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImagesRecyclerViewAdapter(val items: List<Bitmap>): RecyclerView.Adapter<ImagesRecyclerViewAdapter.ImageViewHolder>()  {


    lateinit var myListener : OnImageClickFromAdapter

    interface OnImageClickFromAdapter{
        fun onImageClick(position: Int)
        fun onImageLongClick(position: Int)
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
            imageView.setOnClickListener{
                myListener?.onImageClick(adapterPosition)
            }

            imageView.setOnLongClickListener {
                myListener?.onImageLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }

        fun bind(bitmap: Bitmap){
            imageView.setImageBitmap(bitmap)
        }


    }
}