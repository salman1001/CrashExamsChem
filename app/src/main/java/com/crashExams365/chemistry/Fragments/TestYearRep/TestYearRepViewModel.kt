package com.crashExams365.chemistry.Fragments.TestYearRep

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

class TestYearRepViewModel(application: Application) : AndroidViewModel(application),
    ITestYearsLoad {
    private var topicsListMutable: MutableLiveData<List<YearModel>>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    private val iTestYearsLoad: ITestYearsLoad

    init {
        iTestYearsLoad=this
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
    fun getTopicsList(type:String): MutableLiveData<List<YearModel>> {
        if (topicsListMutable == null) {
            topicsListMutable = MutableLiveData()
            loadTopics(type)

        }
        return topicsListMutable!!
    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }
    private fun loadTopics(type: String) {

        if (getConnectionType(getApplication())){
            var testsref=FirebaseDatabase.getInstance().reference
            if (type=="NONE"){
                testsref=FirebaseDatabase.getInstance().getReference("PreviousYears")

            }
            else{
                testsref=FirebaseDatabase.getInstance().getReference(type)

            }


            testsref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val testlist = ArrayList<YearModel>()
                    for (catlist in snapshot.children) {
                        val cat = catlist.getValue(YearModel::class.java)
                        testlist.add(cat!!)
                    }
                    iTestYearsLoad.onLoadsuccess(testlist)

                }

                override fun onCancelled(error: DatabaseError) {
                    iTestYearsLoad.onfailed(error.message)
                }

            })
        }
        else{
            iTestYearsLoad.onfailed("No Internet")

        }

    }

    override fun onLoadsuccess(list: List<YearModel>) {
        topicsListMutable!!.value=list
    }

    override fun onfailed(message: String) {
        messageError.value=message
    }


}