package com.crashExams365.chemistry.Fragments.Chapters

interface IChaptersLoadSuccess {
    fun onLoadsuccess(list: List<ChapterModel>)
    fun onChapLoadFailed(messgae:String)

}