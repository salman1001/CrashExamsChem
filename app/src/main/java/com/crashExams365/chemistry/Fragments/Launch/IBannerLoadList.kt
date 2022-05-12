package com.crashExams365.chemistry.Fragments.Launch

interface IBannerLoadList {
    fun onBannerLoadDoneListener(banners:SliderModel)
    fun onBannerLoadFailed(messgae:String)
}