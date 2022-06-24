package com.crashExams365.chemistry.Fragments.Notification

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

class NotificationViewModel(application: Application) : AndroidViewModel(application),INotifLoadSuccess {
    private var mesageMutable: MutableLiveData<List<MessageClass>>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    private val iNoticeLoad: INotifLoadSuccess


    init {
        iNoticeLoad=this
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
    fun getTopicsList(): MutableLiveData<List<MessageClass>> {
        if (mesageMutable == null) {
            mesageMutable = MutableLiveData()
            loadTopics()

        }
        return mesageMutable!!
    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }
    private fun loadTopics() {

        if (getConnectionType(getApplication())){


            val topicsModellist = ArrayList<MessageClass>()
            var cah_ref= FirebaseDatabase.getInstance().getReference("Notification")

            cah_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (category in snapshot.children) {
                        val catModel = category.getValue(MessageClass::class.java)
                        topicsModellist.add(catModel!!)
                    }

                    iNoticeLoad.onLoadsuccess(topicsModellist)
                }

                override fun onCancelled(error: DatabaseError) {
                    iNoticeLoad.onLoadFailed(error.message)


                }

            })

        }
        else{
            iNoticeLoad.onLoadFailed("No Internet")

        }

    }

    override fun onLoadsuccess(list: List<MessageClass>) {
        mesageMutable!!.value=list


    }

    override fun onLoadFailed(message: String) {
        messageError.value=message

    }


}