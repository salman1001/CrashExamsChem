package com.crashExams365.chemistry.Fragments.TopicsInChap

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.crashExams365.chemistry.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.TopicsInChapFragmentBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class TopicsInChap : Fragment() {
    private var _binding: TopicsInChapFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null


    var adpater:TopicsInChapAdap?=null
    val args:TopicsInChapArgs by navArgs<TopicsInChapArgs>()
    var chapterselected:String?=null
    var type:String?=null
    var layoutAnimationController: LayoutAnimationController?=null
    var title:String=""
    var titlesend:String=""
    lateinit var mAdView : AdView


    private var mRewardedAd: RewardedAd? = null






    private lateinit var topicsInChapViewModel: TopicsInChapViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        topicsInChapViewModel= ViewModelProvider(this).get(TopicsInChapViewModel::class.java)

        _binding= TopicsInChapFragmentBinding.inflate(inflater,container,false)

        MobileAds.initialize(requireContext()) {}
        loadAd()
        mAdView=binding.adView
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



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        goinitViews()




        topicsInChapViewModel.getMesssageError().observe(viewLifecycleOwner,{

            binding.proTopi.visibility=View.GONE
            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()

            }catch (e: Exception){

            }





        })

        topicsInChapViewModel.getTopicsList(type!!,chapterselected!!).observe(viewLifecycleOwner,{
            binding.proTopi.visibility=View.GONE
            adpater= TopicsInChapAdap(requireContext(),it,titlesend,title,type!!)
            binding.recTopisc.adapter=adpater
            binding.recTopisc.layoutAnimation=layoutAnimationController


        })




    }


    private fun goinitViews() {
        chapterselected=args.chapterslected
        type=args.tyoe
        titlesend=args.chapterslected
        if (chapterselected=="NONE") title=type.toString()
        else title=chapterselected.toString()
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)


        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title=title
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3"))
            )
        }
        binding.recTopisc.addItemDecoration(SpacesItemDecoration(8))
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }





    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    fun onCatInChapterClicked(event: TopicsInChapClicked){
        if (event.issuccess){
            when(type){
                "Hc Verma"->{
                    showAd(event,"Hc Verma",false)

                }
                "Dc Pandey"->{

                    showAd(event,"Dc Pandey",false)


                }
                "IE IRODOV"->{

                    showAd(event, "IE IRODOV",false)

                }
                "PreYearWise"->{

                    showAd(event,"PreYearWise",false)


                }

            }

            when(event.catModel.name){
                "Theory"->{
                    showAd(event, "Theory", true)

                }
                "Index"->{
                    val action= TopicsInChapDirections.actionTopicsInChapToIndexFragment(event.chapterselected)
                    navController!!.navigate(action)

                }
                "Test"->{
                    val action=TopicsInChapDirections.actionTopicsInChapToTestNumbers(event.chapterselected,"chapterwise")
                    navController!!.navigate(action)

                }
                "Part Test"->{
                    val action=TopicsInChapDirections.actionTopicsInChapToTestNumbers("Part","Part Test")
                    navController!!.navigate(action)

                }
                "Full Test" ->{
                    val action=TopicsInChapDirections.actionTopicsInChapToTestNumbers("Full","Full Test")
                    navController!!.navigate(action)

                }
                "Previous Years"->{
                  showAd(event, "Previous Years", true)
                }
                "Exercise"->{
                   showAd(event, "Exercise", true)
                }
                "Notes"->{
                    showAd(event, "Notes", true)

                }
            }
        }
    }

    private fun gotoDocumentViewer(event: TopicsInChapClicked, type: String) {
        if (type=="PreYearWise"){
            val action=TopicsInChapDirections.actionTopicsInChapToDocumentViewer(event.chapterselected,event.catModel.name.toString())
            navController!!.navigate(action)
        }
        else{
            val action=TopicsInChapDirections.actionTopicsInChapToDocumentViewer(type,event.catModel.name.toString())
            navController!!.navigate(action)
        }



    }

    private fun showAd(event: TopicsInChapClicked, s: String, b: Boolean) {


        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(ContentValues.TAG, "Ad was shown.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                // Called when ad fails to show.
                Log.d(ContentValues.TAG, "Ad failed to show.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(ContentValues.TAG, "Ad was dismissed.")

                loadAd()

            }
        }



        if (mRewardedAd != null) {
            mRewardedAd?.show(requireActivity(), OnUserEarnedRewardListener() {
                if (b){
                    godoStuffs(s,event)
                }
                else{
                    gotoDocumentViewer(event,s)
                }


                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.getAmount()
                    var rewardType = rewardItem.getType()


                    Log.d(ContentValues.TAG, "User earned the reward.")
                    // Toast.makeText(this, "User earned the reward.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            if (b){
                godoStuffs(s,event)
            }
            else{
                gotoDocumentViewer(event,s)
            }
        }


    }

    private fun godoStuffs(s: String, event: TopicsInChapClicked) {
        when(s){

            "Notes"->{
                val action= TopicsInChapDirections.actionTopicsInChapToDocumentViewer(s,event.chapterselected)
                navController!!.navigate(action)

            }
            "Theory"->{
                val action= TopicsInChapDirections.actionTopicsInChapToPdfViewer(s,event.chapterselected.toString())
                navController!!.navigate(action)
            }
            "Previous Years"->{
                val action= TopicsInChapDirections.actionTopicsInChapToDocumentViewer("PreChap",event.chapterselected)
                navController!!.navigate(action)
            }
            "Exercise"->{
                val action= TopicsInChapDirections.actionTopicsInChapToDocumentViewer("ExerciseChap",event.chapterselected)
                navController!!.navigate(action)
            }


        }


    }

    private fun loadAd() {
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            requireContext(),
            "ca-app-pub-3543422873793913/3049381734",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(ContentValues.TAG, adError?.message)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(ContentValues.TAG, "Ad was loaded.")
                    mRewardedAd = rewardedAd
                }
            })
    }
}