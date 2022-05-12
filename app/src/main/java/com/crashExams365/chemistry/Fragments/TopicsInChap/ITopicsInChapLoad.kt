package com.crashExams365.chemistry.Fragments.TopicsInChap

interface ITopicsInChapLoad {
    fun onLoadsuccess(list: List<TopicsInChapModel>)
    fun onfailed(message:String)
}