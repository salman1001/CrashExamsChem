package com.crashExams365.chemistry.Fragments.PdfViewer

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.chemistry.Fragments.TestNumbers.TestNumbersDirections
import com.crashExams365.chemistry.Fragments.TopicsInChap.TopicsInChapClicked
import com.crashExams365.chemistry.databinding.PdfViewerFragmentBinding
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.StringBuilder
import com.crashExams365.chemistry.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.lang.Exception

class PdfViewer : Fragment(), IHeaderTopicsLoad {
    private var _binding: PdfViewerFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null
    val args: PdfViewerArgs by navArgs<PdfViewerArgs>()
    var type:String?=null
    var chapter:StringBuilder?=null
    lateinit var topic_ref_header: DatabaseReference
    lateinit var iHeaderTopicsLoad:IHeaderTopicsLoad
    var rec_header_topic:RecyclerView?=null

    var view1:View?=null

    lateinit var toggle:ActionBarDrawerToggle
    var adapterForHeader:AdapterForHeader?=null
    lateinit var mAdView : AdView




    private lateinit var pdfViewerViewModel: PdfViewerViewModel
    var str:String?=null
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
    ): View? {
        pdfViewerViewModel= ViewModelProvider(this).get(PdfViewerViewModel::class.java)


        _binding= PdfViewerFragmentBinding.inflate(inflater,container,false)

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

        InterstitialAd.load(requireContext(),"ca-app-pub-3543422873793913/8946530075", adRequest1, object : InterstitialAdLoadCallback() {
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




        pdfViewerViewModel.getMesssageErrorHeader().observe(viewLifecycleOwner,{

            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()
            }catch (e: Exception){

            }



        })

        pdfViewerViewModel.getTopicsList(str!!).observe(viewLifecycleOwner,{
            adapterForHeader= AdapterForHeader(requireContext(),it)
            rec_header_topic!!.adapter=adapterForHeader


        })

        pdfViewerViewModel.getMesssageError().observe(viewLifecycleOwner,{
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                ContextCompat.getColor(requireContext(), R.color.pri)).show()
        })

        pdfViewerViewModel.getPdfData(type!!,chapter.toString(),view).observe(viewLifecycleOwner,{

            binding.pdfViewer.fromStream(it.stream).spacing(16).onLoad{
                binding.prolaunch.visibility=View.GONE
            }.scrollHandle(DefaultScrollHandle(requireContext())).load()

        })





    }



    private fun initViews(view: View) {
        navController= Navigation.findNavController(view)
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title="Theory"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3"))
            )
        }
        toggle= ActionBarDrawerToggle(requireActivity(),binding.draw,binding.toolbar,
            R.string.open,
            R.string.close)
        toggle.isDrawerIndicatorEnabled=true
        binding.draw!!.addDrawerListener(toggle)
        toggle.syncState()
        view1=binding.navView.getHeaderView(0)
        rec_header_topic=view1!!.findViewById(R.id.rec_pdf_header)
        rec_header_topic!!.addItemDecoration(SpacesItemDecoration(8))
        type=args.pdftype
        chapter= StringBuilder(args.chapterseleetd)
        str=chapter.toString()
        chapter!!.append(".pdf")

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
    fun onheaderItemClicked(event: HeaderTopicClicked){
        if (event.isSuccess){
            binding.pdfViewer.jumpTo((event.topics.page!!-1),true)
            binding.draw.closeDrawer(GravityCompat.START)
        }
    }
    override fun onBannerLoadDoneListener(list: List<HeaderTopicsModel>) {
        adapterForHeader= AdapterForHeader(requireContext(),list)
        rec_header_topic!!.adapter=adapterForHeader
    }

    override fun onloadFiled(message: String) {

    }






}