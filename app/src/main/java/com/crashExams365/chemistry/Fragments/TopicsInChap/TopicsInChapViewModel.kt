package com.crashExams365.chemistry.Fragments.TopicsInChap

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

class TopicsInChapViewModel(application: Application) : AndroidViewModel(application),
    ITopicsInChapLoad {

    private var topicsListMutable: MutableLiveData<List<TopicsInChapModel>>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    private val iTopicsInChapLoad: ITopicsInChapLoad

    init {
        iTopicsInChapLoad=this
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
    fun getTopicsList(type:String,chapterselected:String): MutableLiveData<List<TopicsInChapModel>> {
        if (topicsListMutable == null) {
            topicsListMutable = MutableLiveData()
            loadTopics(type,chapterselected)

        }
        return topicsListMutable!!
    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }
    private fun loadTopics(type: String, chapterselected: String) {

        if (getConnectionType(getApplication())){


            val topicsModellist = ArrayList<TopicsInChapModel>()
            var cah_ref=FirebaseDatabase.getInstance().reference


            cah_ref = if (type == "PreYearWise")
                FirebaseDatabase.getInstance().getReference(type).child(chapterselected)
             else
                FirebaseDatabase.getInstance().getReference(type)


            cah_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (category in snapshot.children) {
                        val catModel = category.getValue(TopicsInChapModel::class.java)
                        topicsModellist.add(catModel!!)
                    }

                    iTopicsInChapLoad.onLoadsuccess(topicsModellist)
                }

                override fun onCancelled(error: DatabaseError) {
                    iTopicsInChapLoad.onfailed(error.message)


                }

            })

        }
        else{
            iTopicsInChapLoad.onfailed("No Internet")

        }

    }


    override fun onLoadsuccess(list: List<TopicsInChapModel>) {
        topicsListMutable!!.value=list
    }

    override fun onfailed(message: String) {
        messageError.value=message

    }


}