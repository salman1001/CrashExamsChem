package com.crashExams365.chemistry

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.crashExams365.chemistry.common.UserInfo
import com.crashExams365.chemistry.databinding.ActivityRegisterWithNameBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterWithName : AppCompatActivity() {






    private  lateinit var userdatabase: FirebaseDatabase
    private lateinit var userref: DatabaseReference
    private lateinit var activityRegisterWithNameBinding:ActivityRegisterWithNameBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterWithNameBinding = ActivityRegisterWithNameBinding.inflate(layoutInflater)
        val view=activityRegisterWithNameBinding.root
        setContentView(view)
        initialize()
        activityRegisterWithNameBinding.buttonRegister.setOnClickListener{
            if (activityRegisterWithNameBinding.firstname.text!!.isEmpty()||activityRegisterWithNameBinding.lastname.text!!.isEmpty()){
                Snackbar.make(view, "Enter Your First And Last Name", Snackbar.LENGTH_SHORT).setBackgroundTint(
                    ContextCompat.getColor(this, R.color.pri)).show()

            }
            else{
                if (getConnectionType(this)){
                    activityRegisterWithNameBinding.buttonRegister.visibility=View.GONE
                    activityRegisterWithNameBinding.namepb.visibility=View.VISIBLE
                    val modeljh=UserInfo()
                    modeljh.firstname=activityRegisterWithNameBinding.firstname.text.toString()
                    modeljh.lastName=activityRegisterWithNameBinding.lastname.text.toString()
                    modeljh.EmOrPh=activityRegisterWithNameBinding.phOrEmailname.text.toString()
                    userref.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(modeljh).addOnSuccessListener {

                        com.crashExams365.chemistry.CommonData.userInfo=modeljh
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()

                    }.addOnFailureListener{
                        Snackbar.make(view, "Could Not Set Data", Snackbar.LENGTH_SHORT).setBackgroundTint(
                            ContextCompat.getColor(this, R.color.pri)).show()
                        activityRegisterWithNameBinding.buttonRegister.visibility=View.VISIBLE
                        activityRegisterWithNameBinding.namepb.visibility=View.GONE

                    }

                }
                else{
                    Snackbar.make(view, "Please Connect To The Internet", Snackbar.LENGTH_SHORT).setBackgroundTint(
                        ContextCompat.getColor(this, R.color.pri)).show()

                }



            }

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

    private fun initialize() {
        userdatabase= FirebaseDatabase.getInstance()
        userref=userdatabase.getReference("UerRef")

        if (FirebaseAuth.getInstance().currentUser!!.phoneNumber!=null&&FirebaseAuth.getInstance().currentUser!!.phoneNumber!!.isNotEmpty()) {

            activityRegisterWithNameBinding.phOrEmailname.setText(FirebaseAuth.getInstance().currentUser!!.phoneNumber)

        } else {
            activityRegisterWithNameBinding.phOrEmailname.setText(FirebaseAuth.getInstance().currentUser!!.email)
        }



        }


    }
