package com.crashExams365.chemistry.Fragments.Test

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.crashExams365.chemistry.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.Exception
import java.util.*

class TestViewModel(application: Application) : AndroidViewModel(application),ILoadTestPdfOptioins,IUploadDone {

    private var pdfData: MutableLiveData<ReturnDataTypeTest>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    var iLoadTestPdfOptioins: ILoadTestPdfOptioins = this
    var iUploadDone:IUploadDone = this


    private var isDone:MutableLiveData<Boolean>?=null
    private var erroeMessageWhilePost:MutableLiveData<String> = MutableLiveData()


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


    fun getPdfData(
        view: View,
        type: String,
        chapterselected: String,
        testNo: String,
        chaptername: String
    ): MutableLiveData<ReturnDataTypeTest> {
        if (pdfData == null) {
            pdfData = MutableLiveData()
            getIt(view,type,chapterselected,testNo,chaptername)

        }
        return pdfData!!
    }
    fun errorWhileUploading():MutableLiveData<String> {
        return erroeMessageWhilePost!!

    }

    fun postDataAndReturn(dataObjectStore: DataObjectStore,testNo: String,id:String):MutableLiveData<Boolean>{
        if (isDone==null){
            isDone= MutableLiveData()
            setData(dataObjectStore,testNo,id)
        }
        return isDone!!
    }

    private fun setData(dataObjectStore: DataObjectStore, testNo: String, id: String) {
        if (getConnectionType(getApplication())){


            val db= FirebaseFirestore.getInstance()
            val userTestRefs=FirebaseDatabase.getInstance().getReference("UserTestDone").child(FirebaseAuth.getInstance().currentUser!!.uid).child(dataObjectStore.testType.toString())
                .child(dataObjectStore.chapterSelected.toString())
            db.collection("testResult").document(dataObjectStore.testType.toString())
                .collection(dataObjectStore.chapterSelected.toString()).document( testNo)
                .collection("users")
                .document(id).set(dataObjectStore).addOnSuccessListener {


                    db.collection("testResult").document(dataObjectStore.testType.toString())
                        .collection(dataObjectStore.chapterSelected.toString()).document( testNo.toString())
                        .collection("marksRank").document("marksarr").update("arr", FieldValue.arrayUnion(dataObjectStore.totalMarksObtained))
                        .addOnSuccessListener {
                            var testItemPost=TestItemPost()
                            testItemPost.name=testNo

                            userTestRefs.child(Calendar.getInstance().timeInMillis.toString()).setValue(testItemPost).addOnSuccessListener {
                                iUploadDone.isDoneSuccess(true)



                            }.addOnFailureListener{
                                iUploadDone.notDone(it.message.toString())
                            }



                        }.addOnFailureListener{
                            iUploadDone.notDone(it.message.toString())
                        }



                }.addOnFailureListener{
                    iUploadDone.notDone(it.message.toString())

                }
        }
        else{
            iUploadDone.notDone("No Internet")

        }








    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }




    private fun getIt(
        view: View,
        type: String,
        chapterselected1: String,
        testNo: String,
        chaptername: String
    ) {

        if (getConnectionType(getApplication())){


            try {
                Snackbar.make(view, "Please Wait We Are Preparing Your Test", Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(getApplication(), R.color.pri)).show()

            }catch (e: Exception){

            }



           // Toast(getApplication()).showCustomToast ("Hello! This is a custom Toast!", this)

          //  Toast.makeText(getApplication(),"Please Wait We Are Preparing Your Test",Toast.LENGTH_LONG).show()

            val localFile = File.createTempFile("testTesting", "pdf")
            val storageReference= FirebaseStorage.getInstance().getReference(type).child(chapterselected1)
            val  cat_ref= FirebaseDatabase.getInstance().getReference("TestOptions").child(type).child(chaptername).child(testNo)

            storageReference.getFile(localFile).addOnSuccessListener {
                cat_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val opt: OptionClass? =snapshot.getValue(OptionClass::class.java)
                        iLoadTestPdfOptioins.inLoadSuccess(localFile,opt!!)

                    }

                    override fun onCancelled(error: DatabaseError) {
                        iLoadTestPdfOptioins.onFailed(error.message)

                    }

                })


            }.addOnFailureListener{
                iLoadTestPdfOptioins.onFailed(it.message!!)

            }


        }
        else{
            iLoadTestPdfOptioins.onFailed("No Internet")

        }
    }
    override fun inLoadSuccess(file: File, optionClass: OptionClass) {
        val returnDataTypeTest=ReturnDataTypeTest()
        returnDataTypeTest.optionClass=optionClass
        returnDataTypeTest.pdfFile=file
        pdfData!!.value=returnDataTypeTest
    }

    override fun onFailed(message: String) {
        messageError.value=message
    }

    override fun isDoneSuccess(boolean: Boolean) {
        isDone!!.value=true
    }

    override fun notDone(messagep: String) {
        erroeMessageWhilePost.value=messagep
    }

}