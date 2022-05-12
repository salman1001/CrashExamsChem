package com.crashExams365.chemistry.Fragments.TestNumbers

import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.crashExams365.chemistry.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.chemistry.Fragments.TopicsInChap.TopicsInChapAdap
import com.crashExams365.chemistry.Fragments.TopicsInChap.TopicsInChapClicked
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.TestNumbersFragmentBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class TestNumbers : Fragment() {
    private var _binding: TestNumbersFragmentBinding? = null
    private val binding get() = _binding!!
    var navController: NavController? = null
    var layoutAnimationController: LayoutAnimationController? = null
    var title = ""

    var titlesend = ""
    lateinit var mAdView: AdView


    var adpater: TopicsInChapAdap? = null
    val args: TestNumbersArgs by navArgs<TestNumbersArgs>()
    var chapterselected: String? = null
    var testtype: String? = null
    lateinit var testNumbersViewModel: TestNumbersViewModel
    private var mRewardedAd: RewardedAd? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        testNumbersViewModel = ViewModelProvider(this).get(TestNumbersViewModel::class.java)
        _binding = TestNumbersFragmentBinding.inflate(inflater, container, false)
        MobileAds.initialize(requireContext()) {}
        loadAd()
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)



        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
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
        navController = Navigation.findNavController(view)

        goinitViews()




        testNumbersViewModel.getMesssageError().observe(viewLifecycleOwner, {


            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)
                ).show()

            }catch (e: Exception){

            }



            binding.probhai.visibility = View.GONE

        })

        testNumbersViewModel
            .getTopicsList(
                FirebaseAuth.getInstance().currentUser!!.uid,
                testtype!!,
                chapterselected!!
            )
            .observe(viewLifecycleOwner, {
                binding.probhai.visibility = View.GONE

                adpater =
                    TopicsInChapAdap(requireContext(), it, chapterselected!!, titlesend, testtype!!)
                binding.recTopisc.adapter = adpater
                binding.recTopisc.layoutAnimation = layoutAnimationController


            })


    }

    private fun goinitViews() {
        chapterselected = args.nameOfChap
        testtype = args.testtype
        titlesend = args.nameOfChap
        layoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

        if (testtype != "chapterwise") title = testtype.toString()
        else title = chapterselected.toString()


        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = title
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3")
                )
            )
        }

        binding.recTopisc.addItemDecoration(SpacesItemDecoration(8))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onCatInChapterClicked(event: TopicsInChapClicked) {
        if (event.issuccess) {

            if (event.catModel.id!! < 50) {
                if (!event.catModel.done) {
                    showAd(event,false)




                } else {

                    showAd(event,true)



                }


            } else {
                Snackbar.make(
                    binding.root,
                    "Stay Tuned, We Will Get It For You",
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)
                ).show()
            }

        }
    }

    private fun showAd(event: TopicsInChapClicked,doneSatus:Boolean) {


        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.")

                mRewardedAd = null
                loadAd()

            }
        }



        if (mRewardedAd != null) {
            mRewardedAd?.show(requireActivity(), OnUserEarnedRewardListener() {
                godostuffs(doneSatus,event)



                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.getAmount()
                    var rewardType = rewardItem.getType()


                    Log.d(TAG, "User earned the reward.")
                   // Toast.makeText(this, "User earned the reward.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            godostuffs(doneSatus,event)
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
          //  Toast.makeText(this, "The rewarded ad wasn't ready yet.", Toast.LENGTH_SHORT).show()
        }


    }

    private fun godostuffs(doneSatus: Boolean, event: TopicsInChapClicked) {

        when(doneSatus){
            true->{
                binding.recTopisc.visibility = View.GONE
                binding.probhai.visibility = View.VISIBLE

                testNumbersViewModel
                    .getpdfandData(
                        testtype!!,
                        chapterselected!!,
                        event.catModel.name!!,
                        FirebaseAuth.getInstance().currentUser!!.uid
                    )
                    .observe(viewLifecycleOwner, {

                        val action =
                            TestNumbersDirections.actionTestNumbersToResultFragments(it)
                        navController!!.navigate(action)
                        binding.recTopisc.visibility = View.VISIBLE
                        binding.probhai.visibility = View.GONE


                    })
                testNumbersViewModel.getMesssageErrorPdf().observe(viewLifecycleOwner, {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    binding.recTopisc.visibility = View.VISIBLE
                    binding.probhai.visibility = View.GONE
                })


            }
            false->{

                val actioN=TestNumbersDirections.actionTestNumbersToTestFragment(testtype!!,event.chapterselected,event.catModel.name.toString())
                navController!!.navigate(actioN)
            }
        }
    }

    private fun loadAd() {
        var adRequest = AdRequest.Builder().build()

         RewardedAd.load(
            requireContext(),
            "ca-app-pub-3543422873793913/3340223449",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.message)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mRewardedAd = rewardedAd
                }
            })
    }
}