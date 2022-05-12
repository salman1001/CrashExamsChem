package com.crashExams365.chemistry.Fragments.Notification

interface INotifLoadSuccess {
    fun onLoadsuccess(list: List<MessageClass>)
    fun onLoadFailed(message:String)

}