package com.crashExams365.chemistry.Fragments.Doubts

import com.crashExams365.chemistry.Fragments.Notification.MessageClass

interface IQueAnsLoad {
    fun onLoadsuccess(list: List<QuestionAndAns>)
    fun onLoadFailed(message:String)

}