package com.crashExams365.chemistry.Fragments.TestYearRep

interface ITestYearsLoad {
    fun onLoadsuccess(list: List<YearModel>)
    fun onfailed(message:String)
}