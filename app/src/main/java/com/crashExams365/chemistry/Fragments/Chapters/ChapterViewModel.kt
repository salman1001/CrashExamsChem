package com.crashExams365.chemistry.Fragments.Chapters

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChapterViewModel(application: Application) : AndroidViewModel(application), IChaptersLoadSuccess {



    private var chapterListMutable: MutableLiveData<List<ChapterModel>>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    private val iChaptersLoadSuccess: IChaptersLoadSuccess







    init {
        iChaptersLoadSuccess=this
    }



    private fun getConnectionType(context: Context): Boolean {
        var result = false // Returns connection type. 0: none; 1: mobile data; 2: wifi
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                        result = true
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    } else if(type == ConnectivityManager.TYPE_VPN) {
                        result = true
                    }
                }
            }
        }
        return result
    }




    fun getCatList(): MutableLiveData<List<ChapterModel>> {
        if (chapterListMutable==null){
            chapterListMutable= MutableLiveData()
            loadCategory()

        }
        return chapterListMutable!!
    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }




    private fun loadCategory() {

        if (getConnectionType(getApplication())){


            val chapterModellist = ArrayList<ChapterModel>()
            val cah_ref =FirebaseDatabase.getInstance().getReference("CatClick").child("Chapters")


            cah_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (category in snapshot.children) {
                        val catModel = category.getValue(ChapterModel::class.java)
                        chapterModellist.add(catModel!!)
                    }
                    iChaptersLoadSuccess.onLoadsuccess(chapterModellist)
                }

                override fun onCancelled(error: DatabaseError) {
                    iChaptersLoadSuccess.onChapLoadFailed(error.message)


                }

            })

        }
        else{
            iChaptersLoadSuccess.onChapLoadFailed("No Internet Habibi")

        }

    }


    override fun onLoadsuccess(list: List<ChapterModel>) {

        chapterListMutable!!.value=list

    }

    override fun onChapLoadFailed(messgae: String) {
        messageError.value=messgae

    }
}
