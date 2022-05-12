package com.crashExams365.chemistry.Fragments.IndexFragment

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.crashExams365.chemistry.Fragments.PdfViewer.HeaderTopicsModel
import com.crashExams365.chemistry.Fragments.PdfViewer.IHeaderTopicsLoad
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IndexViewModel(application: Application) : AndroidViewModel(application), IHeaderTopicsLoad {


    private var indexListMutable: MutableLiveData<List<HeaderTopicsModel>>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    private val iHeaderTopicsLoad:IHeaderTopicsLoad







    init {
        iHeaderTopicsLoad=this
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




    fun getTopicsList(chapterselected:String): MutableLiveData<List<HeaderTopicsModel>> {
        if (indexListMutable == null) {
            indexListMutable = MutableLiveData()
            loadTopics(chapterselected)

        }
        return indexListMutable!!
    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }




    private fun loadTopics( chapterselected: String) {

        if (getConnectionType(getApplication())){


            val topicsModellist = ArrayList<HeaderTopicsModel>()
            var cah_ref= FirebaseDatabase.getInstance().getReference("HeaderTopics").child(chapterselected)

            cah_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (category in snapshot.children) {
                        val catModel = category.getValue(HeaderTopicsModel::class.java)
                        topicsModellist.add(catModel!!)
                    }

                    iHeaderTopicsLoad.onBannerLoadDoneListener(topicsModellist)
                }

                override fun onCancelled(error: DatabaseError) {
                    iHeaderTopicsLoad.onloadFiled(error.message)


                }

            })

        }
        else{
            iHeaderTopicsLoad.onloadFiled("No Internet")

        }

    }
    override fun onBannerLoadDoneListener(list: List<HeaderTopicsModel>) {
        indexListMutable!!.value=list

    }

    override fun onloadFiled(message: String) {
        messageError.value=message

    }

}