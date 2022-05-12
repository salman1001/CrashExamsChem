package com.crashExams365.chemistry.Fragments.ResultFragment

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.crashExams365.chemistry.Fragments.Test.DataObjectStore
import com.crashExams365.chemistry.Fragments.Test.PassObj
import com.google.firebase.firestore.FirebaseFirestore

class ResultFragmentsViewModel(application: Application) : AndroidViewModel(application),
    IRanksLoadSuccess {
    private var returningData: MutableLiveData<NewTypeWithRank>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()


    var correct =0
    var inCorrect =0
    var unMarked =0
    var marks =0L
    var IRanksLoadSuccess: IRanksLoadSuccess?=null
    init {
        IRanksLoadSuccess=this
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
    fun getTopicsList(passObj: PassObj): MutableLiveData<NewTypeWithRank> {
        if (returningData == null) {
            returningData= MutableLiveData()
            loadTopics(passObj)

        }
        return returningData!!
    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }
    private fun loadTopics(type: PassObj) {

        if (getConnectionType(getApplication())){
            getDataAndSetRanks(calculateMarks(type),type)


        }
        else{
            IRanksLoadSuccess!!.onFailedLoad("No Internet")

        }

    }





    private fun getDataAndSetRanks(calculateMarks: Long,passObj: PassObj) {
        //I am gonna make two calls
        val db= FirebaseFirestore.getInstance()
        db.collection("testResult").document(passObj.testType)
            .collection(passObj.chapterSelected).document( passObj.testName)
            .collection("users").orderBy("totalMarksObtained",
                com.google.firebase.firestore.Query.Direction.DESCENDING).limit(20).get()

            .addOnSuccessListener {

                    documents ->
                val recc_rank=ArrayList<DataObjectStore>()
                var dataObjectStore= DataObjectStore()
                for (document in documents) {
                    dataObjectStore=document.toObject(DataObjectStore::class.java)
                    recc_rank.add(dataObjectStore)
                }
           //     IRanksLoadSuccess!!.onLoadsuccess(rec_rank)444444444444444444444444444444444444444444



                db.collection("testResult").document(passObj!!.testType)
                    .collection(passObj!!.chapterSelected).document( passObj!!.testName)
                    .collection("marksRank").document("marksarr").get()

                    .addOnSuccessListener {   doci ->
                        var rec_rank=ArrayList<Long>()
                        rec_rank= doci["arr"] as ArrayList<Long>
                        rec_rank.sort()
                        rec_rank.reverse()
                        var index:Int=1
                        for (i in 0 until rec_rank.size){
                            Log.d(ContentValues.TAG, "getDataAndSetRanks: "+ rec_rank[i])
                            if (calculateMarks==rec_rank[i]){
                                index+=i
                                break
                            }

                        }
                        IRanksLoadSuccess!!.onLoadsuccess(recc_rank,index)

                    }

            }.addOnFailureListener{
            }
    }

    private fun calculateMarks(passObj: PassObj):Long {

        val len:Int=passObj!!.hashmapForAns.size
        for (i in 0 until len){
            if (passObj!!.hashmapForMarked[i.toString()].equals("NULL")){
                unMarked++
            }
            else if (passObj!!.hashmapForMarked[i.toString()].equals(passObj!!.hashmapForAns[i.toString()])){
                correct++
                marks+=4
            }
            else{
                inCorrect++
                marks-=1
            }
        }

        return marks
    }

    override fun onLoadsuccess(list: List<DataObjectStore>, rank: Int) {
        val newTypeWithRank=NewTypeWithRank()
        newTypeWithRank.list=list
        newTypeWithRank.rank=rank -1
        newTypeWithRank.correct=correct
        newTypeWithRank.unmarked=unMarked
        newTypeWithRank.total=marks
        newTypeWithRank.wrong=inCorrect
        returningData!!.value=newTypeWithRank




    }

    override fun onFailedLoad(message: String) {
        messageError.value=message
    }


}