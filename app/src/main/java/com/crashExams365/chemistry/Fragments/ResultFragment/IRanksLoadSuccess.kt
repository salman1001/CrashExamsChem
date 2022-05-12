package com.crashExams365.chemistry.Fragments.ResultFragment

import com.crashExams365.chemistry.Fragments.Test.DataObjectStore

interface IRanksLoadSuccess {
    fun onLoadsuccess(list: List<DataObjectStore>,rank:Int)
    fun onFailedLoad(message:String)
}