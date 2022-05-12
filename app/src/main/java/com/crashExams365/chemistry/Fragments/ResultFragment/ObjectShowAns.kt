package com.crashExams365.chemistry.Fragments.ResultFragment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class ObjectShowAns(

    var chapterSelected: String,
    var hashmapForAns:HashMap<String,String>,
    var hashmapForMarked:HashMap<String,String>,
    var localFile: File

): Parcelable
