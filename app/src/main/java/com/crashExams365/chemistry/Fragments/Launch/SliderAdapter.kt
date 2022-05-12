package com.crashExams365.chemistry.Fragments.Launch



import android.content.Context
import com.smarteist.autoimageslider.SliderViewAdapter

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crashExams365.chemistry.R


class SliderAdapter(var images: SliderModel,var context:Context) : SliderViewAdapter<SliderAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.sliderlay, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(viewHolder: Holder, position: Int) {
        Glide.with(context).load(images.url!![position].name).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(viewHolder.imageView)
    }

    override fun getCount(): Int {
        return images.url!!.size
    }

    inner class Holder(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.image_view)
        }
    }
}