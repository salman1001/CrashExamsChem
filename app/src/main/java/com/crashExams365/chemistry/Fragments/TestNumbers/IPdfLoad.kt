package com.crashExams365.chemistry.Fragments.TestNumbers

import com.crashExams365.chemistry.Fragments.Test.PassObj


interface IPdfLoad {
    fun onPdfLoadDoneListener(obj:PassObj)
    fun onloadFiled(message:String)
}