package com.crashExams365.chemistry.Fragments.Request

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.crashExams365.chemistry.Fragments.Test.IUploadDone
import com.google.firebase.database.FirebaseDatabase

class RequestViewModel(application: Application) : AndroidViewModel(application), IUploadDone {
    private var IsPostDone: MutableLiveData<Boolean>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    var iUploadDone: IUploadDone = this




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
    fun getTopicsList(requestItem: RequestItem,time:String): MutableLiveData<Boolean> {
        if (IsPostDone == null) {
            IsPostDone = MutableLiveData()
            loadTopics(requestItem,time)

        }
        return IsPostDone!!
    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }
    private fun loadTopics(requestItem: RequestItem, time: String) {

        if (getConnectionType(getApplication())){
            val userref=FirebaseDatabase.getInstance().getReference("Requested")

            userref.child(time).setValue(requestItem).addOnSuccessListener {
                iUploadDone.isDoneSuccess(true)


            }.addOnFailureListener{
                iUploadDone.notDone(it.message!!)

            }





        }
        else{
            iUploadDone.notDone("No Internet")

        }

    }



    override fun isDoneSuccess(boolean: Boolean) {
        IsPostDone!!.value=boolean
    }

    override fun notDone(messagep: String) {
        messageError.value=messagep
    }
}