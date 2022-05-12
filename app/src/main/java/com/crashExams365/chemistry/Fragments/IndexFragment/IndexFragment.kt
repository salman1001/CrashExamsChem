package com.crashExams365.chemistry.Fragments.IndexFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.crashExams365.chemistry.Fragments.PdfViewer.AdapterForHeader
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.IndexFragmentBinding
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class IndexFragment : Fragment(){
    private var _binding: IndexFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null
    val args: IndexFragmentArgs by navArgs<IndexFragmentArgs>()
    var chaptername:String?=null
    var adapterForHeader: AdapterForHeader?=null
    private lateinit var indexViewModel: IndexViewModel
    lateinit var mAdView : AdView






    var layoutAnimationController: LayoutAnimationController?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        indexViewModel= ViewModelProvider(this).get(IndexViewModel::class.java)

        _binding= IndexFragmentBinding.inflate(inflater,container,false)


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




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goinitViews(view)


        indexViewModel.getMesssageError().observe(viewLifecycleOwner,{

            try {

                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()
            }catch (e: Exception){

            }

            binding.proIndex.visibility=View.GONE

        })

        indexViewModel.getTopicsList(chaptername!!).observe(viewLifecycleOwner,{
            binding.proIndex.visibility=View.GONE
            adapterForHeader= AdapterForHeader(requireContext(),it)
            binding.recIndex!!.adapter=adapterForHeader
            binding.recIndex.layoutAnimation=layoutAnimationController


        })


    }

    private fun goinitViews(view: View) {
        navController= Navigation.findNavController(view)
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title="Index"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3"))
            )
        }
        chaptername=args.chapterselectd
    }



    






}