package com.crashExams365.chemistry.Fragments.ResultFragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.crashExams365.chemistry.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.chemistry.Fragments.Test.PassObj
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.ResultFragmentsFragmentBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class ResultFragments : Fragment() {
    private var _binding: ResultFragmentsFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null
    val args: ResultFragmentsArgs by navArgs<ResultFragmentsArgs>()
    var passObj:PassObj?=null
    var layoutAnimationController: LayoutAnimationController?=null
    lateinit var mAdView : AdView
    private var mRewardedAd: RewardedAd? = null




    lateinit var adapterForRank:AdapterForRank
    lateinit var resultFragmentsViewModel: ResultFragmentsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultFragmentsViewModel=ViewModelProvider(this).get(ResultFragmentsViewModel::class.java)

        _binding= ResultFragmentsFragmentBinding.inflate(inflater,container,false)


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                passObj!!.localFile.deleteRecursively()
                isEnabled = false
                activity?.onBackPressed()

            }
        })

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        binding.Show.setOnClickListener{
            var obj=ObjectShowAns(passObj!!.chapterSelected,passObj!!.hashmapForAns!!,passObj!!.hashmapForMarked!!,passObj!!.localFile)

            showAd(obj)



        }
        binding.mypb.visibility=View.VISIBLE
        resultFragmentsViewModel.getMesssageError().observe(viewLifecycleOwner,{


            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()

            }catch (e: Exception){

            }




        })
        resultFragmentsViewModel.getTopicsList(passObj!!).observe(viewLifecycleOwner,{
            adapterForRank= AdapterForRank(requireContext(),it.list!!)
            binding.rankingOrder.adapter=adapterForRank
            binding.rankingOrder.layoutAnimation=layoutAnimationController

            binding.NO.text=it.rank.toString()


            binding.markstotla.text=it.total.toString()
            binding.markstotla.visibility=View.VISIBLE
            binding.WrnNo.text=it.wrong.toString()
            binding.correctNo.text=it.correct.toString()
            binding.unmaNo.text=it.unmarked.toString()
            binding.cardHeader.visibility=View.VISIBLE
            binding.myll.visibility=View.VISIBLE
            binding.mypb.visibility=View.GONE


        })





    }
    private fun initViews(view: View) {
        navController= Navigation.findNavController(view)
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

        passObj=args.obj
        binding.rankingOrder.addItemDecoration(SpacesItemDecoration(8))
        binding.testNameOrTitle.text= passObj!!.testName




    }

    private fun showAd(obj: ObjectShowAns) {


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

                mRewardedAd = null
                loadAd()

            }
        }



        if (mRewardedAd != null) {
            mRewardedAd?.show(requireActivity(), OnUserEarnedRewardListener() {
                Log.d(ContentValues.TAG, "showAd: done")
                godostuffs(obj)




                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.getAmount()
                    var rewardType = rewardItem.getType()


                    Log.d(ContentValues.TAG, "User earned the reward.")
                    // Toast.makeText(this, "User earned the reward.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.d(ContentValues.TAG, "The rewarded ad wasn't ready yet.")
            godostuffs(obj)

            //  Toast.makeText(this, "The rewarded ad wasn't ready yet.", Toast.LENGTH_SHORT).show()
        }


    }

    private fun godostuffs(obj: ObjectShowAns) {
        val action= ResultFragmentsDirections.actionResultFragmentsToShowAnswers(obj)
        navController!!.navigate(action)

    }

    private fun loadAd() {
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            requireContext(),
            "ca-app-pub-3543422873793913/5659265029",
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