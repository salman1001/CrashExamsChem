package com.crashExams365.chemistry.Fragments.PdfViewer

import com.google.firebase.storage.StreamDownloadTask

interface IPdfLoad {
    fun onPdfSuccess(byteArray: StreamDownloadTask.TaskSnapshot)
    fun onloadFiledPdf(message:String)
    fun onPdfSuccessT(byteArray: ByteArray)
    fun progressTeller(double: Double)

}