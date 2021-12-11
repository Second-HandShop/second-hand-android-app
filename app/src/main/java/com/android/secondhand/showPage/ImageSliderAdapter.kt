package com.android.secondhand.showPage

import com.android.secondhand.R
import android.content.Context
import android.graphics.Color

import android.widget.TextView
import com.smarteist.autoimageslider.SliderViewAdapter
import android.widget.Toast

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso


class ImageSliderAdapter(val mSliderItems: ArrayList<String>, context: Context) : SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH>() {
    private val context: Context

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem: String = mSliderItems[position]
//        viewHolder.textViewDescription.setText(sliderItem.getDescription())
//        viewHolder.textViewDescription.textSize = 16f
        viewHolder.textViewDescription.setTextColor(Color.WHITE)

        val url = sliderItem
        Picasso.get().load(url).error(R.mipmap.ic_launcher).into(viewHolder.imageViewBackground)

    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        lateinit var imageViewBackground: ImageView
        lateinit var imageGifContainer: ImageView
        lateinit var textViewDescription: TextView

        init {
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container)
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider)
        }
    }

    init {
        this.context = context
    }
}
