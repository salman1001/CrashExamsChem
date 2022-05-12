package com.crashExams365.chemistry.Fragments.Test

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class PassObj(
    var testType:String,
    var chapterSelected: String,
    var hashmapForAns:HashMap<String,String>,
    var hashmapForMarked:HashMap<String,String>,
    val localFile:File,
    var testName:String

):Parcelable


