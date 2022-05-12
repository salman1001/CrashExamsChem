package com.crashExams365.chemistry.Fragments.AskNewDoubts

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.crashExams365.chemistry.Fragments.Doubts.DoubtsViewModel
import com.crashExams365.chemistry.MainActivity
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.common.UserInfo
import com.crashExams365.chemistry.databinding.AskNewDoubtsFragmentBinding
import com.crashExams365.chemistry.databinding.DoubtsFragmentBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

class AskNewDoubtsFragment : Fragment() {
    lateinit var uri:Uri


    private var _binding: AskNewDoubtsFragmentBinding? = null
    private val binding get() = _binding!!
    var navController: NavController? = null
    private lateinit var viewModel: AskNewDoubtsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=ViewModelProvider(this).get(AskNewDoubtsViewModel::class.java)
        _binding = AskNewDoubtsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonsubmit.setOnClickListener{
            binding.buttonsubmit.visibility=View.GONE
            binding.prosub.visibility=View.VISIBLE


            val storageRef = FirebaseStorage.getInstance().getReference()

// Create a reference to "mountains.jpg"
       //     val mountainsRef = storageRef.child("mountains.jpg")

// Create a reference to 'images/mountains.jpg'
        //    val mountainImagesRef = storageRef.child("images/mountains.jpg")




       //     var file = Uri.fromFile(File("path/to/images/rivers.jpg"))
            val riversRef = storageRef.child("Question").child(FirebaseAuth.getInstance().currentUser!!.uid).child("${uri.lastPathSegment}")


            var uploadTask = riversRef.putFile(uri)

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // uploadTask.
               // val ref = storageRef.child("images/mountains.jpg")
                riversRef.downloadUrl.addOnCompleteListener{
                    Log.d(TAG, "onViewCreated: ${it.result}")
                    var userref: DatabaseReference
                    var userdatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
                    userref=userdatabase.getReference("Question").child(FirebaseAuth.getInstance().currentUser!!.uid)
                     var questionids=userdatabase.getReference("QuestionIds")


                    val modeljh= AskQuestionModel()
                    modeljh.questionText=binding.optionText.text.toString()
                    modeljh.questionurl=it.result.toString()
                    modeljh.userId=FirebaseAuth.getInstance().currentUser!!.uid
                    val timestamp=Calendar.getInstance().timeInMillis.toString()
                    modeljh.timestamp= timestamp
                    modeljh.ansUrl=""
                    modeljh.boolean=false
                    val adminQuestionIds=AdminQuestionIds()
                    adminQuestionIds.timestamp=timestamp
                    adminQuestionIds.uid=FirebaseAuth.getInstance().currentUser!!.uid
                    userref.child(timestamp).setValue(modeljh).addOnSuccessListener {
                        questionids.child(timestamp).setValue(adminQuestionIds).addOnSuccessListener {

                            Toast.makeText(requireContext(),"Done Mate",Toast.LENGTH_LONG).show()
                            binding.prosub.visibility=View.GONE

                            requireActivity().onBackPressed()


                        }




                    }.addOnFailureListener{

                    }



                }


                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }

//                .addOnProgressListener {
//                    (bytesTransferred, totalByteCount) ->
//                val progress = (100.0 * bytesTransferred) / totalByteCount
//                Log.d(TAG, "Upload is $progress% done")
//            }
//            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
//                val progress = (100.0 * bytesTransferred) / totalByteCount
//                Log.d(TAG, "Upload is $progress% done")
//            }.addOnPausedListener {
//                Log.d(TAG, "Upload is paused")
//            }








        }
        binding.getimage.setOnClickListener{

            ImagePicker.with(this)
                   .crop()      //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
//            val intent=Intent()
//            intent.setType("image/*")
//            intent.setAction(Intent.ACTION_GET_CONTENT)
//            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE)


        }
    }
//    companion object{
//        val PICK_IMAGE=6060
//    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            //Image Uri will not be null for RESULT_OK
//            val uri: Uri = data?.data!!
//
//            // Use Uri object instead of File to avoid storage permissions
//           // binding.getimage.setImageURI(uri)
//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
//        }
//    }
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

             //   mProfileUri = fileUri
                binding.getimage.setImageURI(fileUri)
                uri=fileUri
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
               // Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
               // Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }




}