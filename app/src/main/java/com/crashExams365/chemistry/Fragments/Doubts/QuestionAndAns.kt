package com.crashExams365.chemistry.Fragments.Doubts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionAndAns (
    var ansUrl:String?=null,
    var boolean:Boolean?=null,
    var questionText:String?=null,
    var questionurl:String?=null,
    var timestamp:String?=null,
    var userId:String?=null
):Parcelable



