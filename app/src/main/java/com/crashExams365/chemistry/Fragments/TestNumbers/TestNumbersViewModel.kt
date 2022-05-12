package com.crashExams365.chemistry.Fragments.TestNumbers

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.crashExams365.chemistry.Fragments.Test.DataObjectStore
import com.crashExams365.chemistry.Fragments.Test.PassObj
import com.crashExams365.chemistry.Fragments.TopicsInChap.ITopicsInChapLoad
import com.crashExams365.chemistry.Fragments.TopicsInChap.TopicsInChapModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class TestNumbersViewModel(application: Application) : AndroidViewModel(application),
    ITopicsInChapLoad, IPdfLoad {
    private var topicsListMutable: MutableLiveData<List<TopicsInChapModel>>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    private val iTopicsInChapLoad: ITopicsInChapLoad


    private var getDataAndPdfAndMove: MutableLiveData<PassObj>?=null
    private var messageErrorPdf: MutableLiveData<String> = MutableLiveData()
    private val iPdfLoad: IPdfLoad







    init {
        iTopicsInChapLoad=this
        iPdfLoad=this
    }
    fun getpdfandData(testtype: String,chapterselected: String,catName:String,id:String):MutableLiveData<PassObj>{
        if (getDataAndPdfAndMove==null){
            getDataAndPdfAndMove= MutableLiveData()
            loadPdfData(testtype,chapterselected,catName,id)
        }
        return getDataAndPdfAndMove!!
    }

    private fun loadPdfData(
        testtype: String,
        chapterselected: String,
        catName: String,
        id: String
    ) {
        val localFile = File.createTempFile("oooooooooooooooo", "pdf")

        val db= FirebaseFirestore.getInstance()
        db.collection("testResult").document(testtype)
            .collection(chapterselected).document( catName)
            .collection("users").document(id).get()

            .addOnSuccessListener { documents ->

                var fullPath= StringBuilder()
                fullPath.append(chapterselected).append(catName).append(".pdf")
                 val storageReference= FirebaseStorage.getInstance().getReference(testtype.toString())
                     .child(fullPath.toString()).getFile(localFile).addOnSuccessListener {

                    var dataObjectStore= documents.toObject(DataObjectStore::class.java)!!
                    val obj= PassObj(testtype.toString(),chapterselected.toString(),dataObjectStore.hashmapForAns!!,dataObjectStore.hashmapForMarked!!,localFile,catName)
                         iPdfLoad.onPdfLoadDoneListener(obj)


                }.addOnFailureListener{
                    iPdfLoad.onloadFiled(it.message!!)

                }
            }.addOnFailureListener{
                iPdfLoad.onloadFiled(it.message!!)


            }
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
    fun getTopicsList(id:String,testtype:String,chapterselected: String): MutableLiveData<List<TopicsInChapModel>> {
        if (topicsListMutable == null) {
            topicsListMutable = MutableLiveData()
            loadTopics(id,testtype,chapterselected)

        }
        return topicsListMutable!!
    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }

    fun getMesssageErrorPdf(): MutableLiveData<String> {
        return messageErrorPdf

    }
    private fun loadTopics(id:String,testType:String,chapterselected: String) {

        if (getConnectionType(getApplication())){

            val userTestRefs=FirebaseDatabase.getInstance().getReference("UserTestDone").child(id).child(testType).child(chapterselected)
            val testsref= FirebaseDatabase.getInstance().getReference("ChapterNoOftest").child(testType).child(chapterselected)

            val catlistst=ArrayList<TopicsInChapModel>()
            val catlistDone=ArrayList<TopicsInChapModel>()

            testsref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (catlist in snapshot.children){
                        val cat=catlist.getValue(TopicsInChapModel::class.java)
                        catlistst.add(cat!!)
                    }
                    userTestRefs.addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                for (catlistr in snapshot.children){
                                    val cat=catlistr.getValue(TopicsInChapModel::class.java)
                                    catlistDone.add(cat!!)
                                }
                                goandEditTestModel(catlistst,catlistDone)
                            }
                            else{
                                //  ITopicsInChapLoad!!.onLoadsuccess(catlistst)
                                goandEditTestModel(catlistst,catlistDone)

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            iTopicsInChapLoad.onfailed(error.message)
                        }

                    })
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

    private fun goandEditTestModel(
        catlistst: java.util.ArrayList<TopicsInChapModel>,
        catlistDone: java.util.ArrayList<TopicsInChapModel>
    ) {

        for (i in 0 until catlistDone.size){
            for (j in 0 until catlistst.size){
                if (catlistDone.size>0&&catlistst.size>0&&catlistDone[i].name==catlistst[j].name){
                    catlistst[j].done=true
                    break
                }
            }
        }

        iTopicsInChapLoad.onLoadsuccess(catlistst)
    }

    override fun onLoadsuccess(list: List<TopicsInChapModel>) {
        topicsListMutable!!.value=list
    }

    override fun onfailed(message: String) {
        messageError.value=message
    }

    override fun onPdfLoadDoneListener(obj: PassObj) {
        getDataAndPdfAndMove!!.value=obj


    }

    override fun onloadFiled(message: String) {
        messageErrorPdf.value=message
    }
}