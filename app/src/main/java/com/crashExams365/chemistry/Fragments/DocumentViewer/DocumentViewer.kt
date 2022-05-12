package com.crashExams365.chemistry.Fragments.DocumentViewer

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.DocumentViewerFragmentBinding
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class DocumentViewer : Fragment() {

    private var _binding: DocumentViewerFragmentBinding? = null
    private val binding get() = _binding!!
    var navController: NavController? = null
    var type:String?=null
    private var chapter: StringBuilder?=null
    private val args:DocumentViewerArgs by navArgs()
    private lateinit var documentViewerViewModel: DocumentViewerViewModel
    lateinit var mAdView : AdView
    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                }
                isEnabled = false
                activity?.onBackPressed()






            }
        })
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        documentViewerViewModel= ViewModelProvider(this)[DocumentViewerViewModel::class.java]
        _binding = DocumentViewerFragmentBinding.inflate(inflater, container, false)


        MobileAds.initialize(requireContext()) {}
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

        var adRequest1 = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),"ca-app-pub-3543422873793913/6511938420", adRequest1, object : InterstitialAdLoadCallback() {
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
        initViews(view)
        binding.prolaunch.visibility=View.VISIBLE







        documentViewerViewModel.getMesssageError().observe(viewLifecycleOwner,{


            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()
            }catch (e: Exception){

            }



            binding.prolaunch.visibility=View.GONE



        })


        documentViewerViewModel.getPdfData(type!!,chapter.toString(),view).observe(viewLifecycleOwner,{
            binding.pdfViewer.fromStream(it.stream).onLoad {

                binding.prolaunch.visibility = View.GONE
            }.spacing(16).scrollHandle(DefaultScrollHandle(requireContext())).load()




        })
    }



    private fun initViews(view: View) {
        navController = Navigation.findNavController(view)
        type=args.type
        chapter= StringBuilder(args.name)
        chapter!!.append(".pdf")
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = args.name
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3")
                )
            )
        }
       // storageReference= FirebaseStorage.getInstance().getReference(type.toString()).child(chapter.toString())
    }


}