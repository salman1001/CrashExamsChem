package com.crashExams365.chemistry.Fragments.Launch

interface INewCatLoad {
    fun onsucess(myNewCategoryModel: MyNewCategoryModel)
    fun onCatLoadFailed(messgae:String)
}