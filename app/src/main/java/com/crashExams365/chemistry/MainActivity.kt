package com.crashExams365.chemistry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
//    var mes:String?=null
//    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent) {
//            mes= intent.extras?.getString("message")
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val bundle = intent.extras
//        if (bundle != null) {
//            supportFragmentManager.popBackStack()
//            val fragment=NotificationFragment()
//            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment, "dogList").commit()
//        }
//
//
//        Log.d(TAG, "onCreate: caleddd 11baby")
//        val bundle=Bundle()
//        val rt=bundle.get("FromMessage")
//        Log.d(TAG, "onCreate: $rt")
////
//        if (rt!=null){
//            supportFragmentManager.popBackStack()
//            Log.d(TAG, "onCreate: caleddd 22baby")
//
//            val fragment=NotificationFragment()
//            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment, "dogList").commit()
//
//        }
    }

//    override fun onStart() {
//        super.onStart()
//        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter("MyData"))
//    }
//
//    override fun onStop() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
//
//        super.onStop()
//
//    }
}