package com.crashExams365.chemistry.Fragments.Launch

import android.content.Context

class PicassoImageLoadingServices(var context: Context) {
}
//}:ImageLoadingService {
//    override fun loadImage(url: String?, imageView: ImageView?) {
//      // Picasso.get().load(url).into(imageView)
//
//        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView!!)
//        //Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView!!)
//
//        //Glide.with(context).load(url).into(imageView!!)
//       // Glide.with(context).load(url).into(imageView!!)
//
//
//    }
//
//    override fun loadImage(resource: Int, imageView: ImageView?) {
//      //  Picasso.get().load(resource).into(imageView)
//        Glide.with(context).load(resource).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView!!)
//       // Glide.with(context).load(resource).into(imageView!!)
//       // Glide.with(context).load(resource).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView!!)
//
//    }
//
//    override fun loadImage(
//        url: String?,
//        placeHolder: Int,
//        errorDrawable: Int,
//        imageView: ImageView?
//    ) {
//
//       // Picasso.get().load(url).placeholder(placeHolder).error(errorDrawable).into(imageView)
//        Glide.with(context).load(url).placeholder(placeHolder).error(errorDrawable).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView!!)
//       // Glide.with(context).load(url).into(imageView!!)
//    }
//}