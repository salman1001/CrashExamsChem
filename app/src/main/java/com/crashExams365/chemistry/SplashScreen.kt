package com.crashExams365.chemistry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.crashExams365.chemistry.common.UserInfo
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
class SplashScreen : AppCompatActivity() {


    private lateinit var providers: List<AuthUI.IdpConfig>
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var listener:FirebaseAuth.AuthStateListener
    private  lateinit var userdatabase:FirebaseDatabase
    private lateinit var userref:DatabaseReference


    override fun onStop() {
        if (firebaseAuth!=null&&listener!=null) firebaseAuth.removeAuthStateListener(listener)
        super.onStop()
    }




    private fun delaySplashScreen() {
        firebaseAuth.addAuthStateListener(listener)
//        Handler(Looper.getMainLooper()).postDelayed({
//
//        },500)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initilize()
        delaySplashScreen()

    }



    private fun initilize() {

        providers=Arrays.asList(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        firebaseAuth= FirebaseAuth.getInstance()
        userdatabase= FirebaseDatabase.getInstance()
        userref=userdatabase.getReference("UerRef")


        listener=FirebaseAuth.AuthStateListener { myfirebaseAuth->
            val user=myfirebaseAuth.currentUser

            if (user!=null){
                checkUserFromFirebase()

            }
            else{
                showLoginLay()
            }
        }
    }

    private fun checkUserFromFirebase() {
        if (getConnectionType(this)){

            userref.child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            val userIn:UserInfo= snapshot.getValue(UserInfo::class.java)!!
                            com.crashExams365.chemistry.CommonData.userInfo=userIn
                            startActivity(Intent(this@SplashScreen,MainActivity::class.java))
                            finish()

                        }
                        else{
                            startActivity( Intent(this@SplashScreen,RegisterWithName::class.java))

                            finish()
                        }



                    }

                    override fun onCancelled(error: DatabaseError){


                    }

                })
        }
        else{
            Toast.makeText(this,"Please Connect To The Internet",Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

    }

    private fun showLoginLay() {

        val authMethodPickerLayout=AuthMethodPickerLayout.Builder(R.layout.layout_sign_in)
            .setPhoneButtonId(R.id.btn_phone)
            .setGoogleButtonId(R.id.btn_google).build()
        val intent = Intent(AuthUI.getInstance().createSignInIntentBuilder().setAuthMethodPickerLayout(authMethodPickerLayout).setTheme(R.style.LoignTheme)
            .setAvailableProviders(providers).setIsSmartLockEnabled(false).build())

        getResult.launch(intent)




    }
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {

                   checkUserFromFirebase()
            }
            else{
                finish()
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

}