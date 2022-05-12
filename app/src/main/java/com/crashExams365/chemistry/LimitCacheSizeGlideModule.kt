package com.crashExams365.chemistry
//
//import android.content.Context
//import com.bumptech.glide.Glide
//
//import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
//
//import com.bumptech.glide.GlideBuilder
//import com.bumptech.glide.annotation.GlideModule
//import android.os.StatFs
//
//import android.os.Build
//
//import android.annotation.TargetApi
//
//import android.os.Environment
//import java.lang.String
//
//
//class LimitCacheSizeGlideModule : GlideModule {
//    fun applyOptions(context: Context?, builder: GlideBuilder) {
//        if (MyApplication.from(context).isTest()) return  // NOTE: StatFs will crash on robolectric.
//        val totalGiB = totalBytesOfInternalStorage / 1024.0 / 1024.0 / 1024.0
//        Log.i(String.format(Locale.US, "Internal Storage Size: %.1fGiB", totalGiB))
//        if (totalGiB < SMALL_INTERNAL_STORAGE_THRESHOLD_GIB) {
//            Log.i("Limiting image cache size to " + DISK_CACHE_SIZE_FOR_SMALL_INTERNAL_STORAGE_MIB + "MiB")
//            builder.setDiskCache(
//                InternalCacheDiskCacheFactory(
//                    context,
//                    DISK_CACHE_SIZE_FOR_SMALL_INTERNAL_STORAGE_MIB.toLong()
//                )
//            )
//        }
//    }
//
//    fun registerComponents(context: Context?, glide: Glide?) {}
//
//    // http://stackoverflow.com/a/4595449/1474113
//    private val totalBytesOfInternalStorage: Long
//        private get() {
//            // http://stackoverflow.com/a/4595449/1474113
//            val stat = StatFs(Environment.getDataDirectory().path)
//            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                getTotalBytesOfInternalStorageWithStatFs(stat)
//            } else {
//                getTotalBytesOfInternalStorageWithStatFsPreJBMR2(stat)
//            }
//        }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
//    private fun getTotalBytesOfInternalStorageWithStatFs(stat: StatFs): Long {
//        return stat.totalBytes
//    }
//
//    private fun getTotalBytesOfInternalStorageWithStatFsPreJBMR2(stat: StatFs): Long {
//        return stat.blockSize.toLong() * stat.blockCount
//    }
//
//    companion object {
//        // Modern device should have 8GB (=7.45GiB) or more!
//        private const val SMALL_INTERNAL_STORAGE_THRESHOLD_GIB = 6
//        private const val DISK_CACHE_SIZE_FOR_SMALL_INTERNAL_STORAGE_MIB = 50 * 1024 * 1024
//    }
//}