package com.crashExams365.chemistry.Fragments.Test

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@IgnoreExtraProperties
class DataObjectStore (
    var testType:String?=null,
    var chapterSelected: String?=null,
    var hashmapForAns:HashMap<String,String>?=null,
    var hashmapForMarked:HashMap<String,String> ?=null,
    var isAttempted:Boolean?=null,
    @ServerTimestamp
    var date:Date?=null,
    var totalMarksObtained:Long?=null,
    var userName:String?=null,
    var userlastName:String?=null


)