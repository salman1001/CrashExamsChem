package com.crashExams365.chemistry.Fragments.ShowAnswers

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.crashExams365.chemistry.Fragments.ResultFragment.ObjectShowAns
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.ShowAnswersFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.lang.Exception


class ShowAnswers : Fragment() {


    private var _binding: ShowAnswersFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null
    val args: ShowAnswersArgs by navArgs<ShowAnswersArgs>()
    var objectShowAns: ObjectShowAns?=null
    lateinit var adapter:ShowAnsAdap
    lateinit var mAdView : AdView

    private var mInterstitialAd: InterstitialAd? = null








    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= ShowAnswersFragmentBinding.inflate(inflater,container,false)



        var adRequest1 = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),"ca-app-pub-3543422873793913/3219544763", adRequest1, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(ContentValues.TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                //  Log.d(TAG, 'Ad was dismissed.')
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                //    Log.d(TAG, 'Ad failed to show.')
            }

            override fun onAdShowedFullScreenContent() {
                //    Log.d(TAG, 'Ad showed fullscreen content.')
                mInterstitialAd = null
            }
        }





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.testTool)
            (activity as AppCompatActivity).supportActionBar?.title="Solutions"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3"))
            )
        }
        objectShowAns=args.objectShowAns
        adapter= ShowAnsAdap(requireContext(),objectShowAns!!.localFile,resources,objectShowAns!!)
        binding.pagerforans.adapter=adapter

        TabLayoutMediator(binding.tabLayout,binding.pagerforans) { tab, position ->
            tab.text = "Question "+(position+1)
        }.attach()





    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                try {
                    if (adapter!=null){
                        adapter.deleveryting()
                    }

                }catch (e:Exception){

                }
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                }

                isEnabled = false
                activity?.onBackPressed()

            }
        })

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }
//////////////////////////////////////////////     ON STOP   //////////////////////////////////////////////////////////////////////////////////////////

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onShowAns(event: ShowAnsButtonPopUp) {
        if (event.issuccess) {


            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(true)



            val inflater = layoutInflater
            val dialoglayout: View = inflater.inflate(R.layout.popup_dialog, null)
            val imageView=dialoglayout.findViewById<ImageView>(R.id.myphoto)
            imageView.setImageBitmap(event.bitmap)

            builder.setView(dialoglayout)
            builder.show()

            mAdView=dialoglayout.findViewById(R.id.adView1)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)



            mAdView.adListener = object: AdListener() {
                override fun onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    super.onAdLoaded()
                }

                override fun onAdFailedToLoad(adError : LoadAdError) {
                    // Code to be executed when an ad request fails.
                    super.onAdFailedToLoad(adError)
                    mAdView.loadAd(adRequest)
                }

                override fun onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.

                }

                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            }




        }
    }




}