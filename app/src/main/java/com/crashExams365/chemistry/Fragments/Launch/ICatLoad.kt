package com.crashExams365.chemistry.Fragments.Launch

interface ICatLoad {
     fun onsucess(list:List<CatModel>)
     fun onCatLoadFailed(messgae:String)

}