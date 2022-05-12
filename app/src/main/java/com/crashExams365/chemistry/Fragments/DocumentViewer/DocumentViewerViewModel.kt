package com.crashExams365.chemistry.Fragments.DocumentViewer

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.crashExams365.chemistry.Fragments.PdfViewer.IPdfLoad
import com.crashExams365.chemistry.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StreamDownloadTask
import java.lang.Exception


class DocumentViewerViewModel(application: Application) : AndroidViewModel(application), IPdfLoad {


    private var pdfData: MutableLiveData<StreamDownloadTask.TaskSnapshot>?=null
    private var messageError: MutableLiveData<String> = MutableLiveData()
    var iPdfLoad: IPdfLoad = this
    var progres:MutableLiveData<Double>?= MutableLiveData()


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
    fun getProgess():MutableLiveData<Double>{
        return progres!!

    }


    fun getPdfData(type:String,chapterselected:String,view: View): MutableLiveData<StreamDownloadTask.TaskSnapshot> {
        if (pdfData == null) {
            pdfData = MutableLiveData()
            getIt(type,chapterselected,view)

        }
        return pdfData!!
    }


    fun getMesssageError(): MutableLiveData<String> {
        return messageError

    }




    private fun getIt(type: String, chapterselected1: String,view:View) {

        if (getConnectionType(getApplication())){

            try {

                Snackbar.make(view, "We Are Getting Your Pdf File", Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(getApplication(), R.color.pri)).show()
            }catch (e: Exception){

            }



            var storageReference= FirebaseStorage.getInstance().getReference(type).child(chapterselected1)
            storageReference.getStream().addOnSuccessListener {
                                iPdfLoad.onPdfSuccess(it)

            }.addOnFailureListener{
                iPdfLoad.onloadFiledPdf(it.message!!)
            }

        }
        else{
            iPdfLoad.onloadFiledPdf("No Internet")

        }
    }


    override fun onPdfSuccess(byteArray: StreamDownloadTask.TaskSnapshot) {
        pdfData!!.value=byteArray
    }

    override fun onloadFiledPdf(message: String) {
        messageError.value=message
    }

    override fun onPdfSuccessT(byteArray: ByteArray) {


    }

    override fun progressTeller(double: Double) {
        progres!!.value=double


    }

}