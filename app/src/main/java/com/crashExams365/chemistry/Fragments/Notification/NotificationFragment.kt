package com.crashExams365.chemistry.Fragments.Notification

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.crashExams365.chemistry.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.NotificationFragmentBinding
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class NotificationFragment : Fragment() {
    private var _binding: NotificationFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null
    var layoutAnimationController: LayoutAnimationController?=null
    lateinit var mAdView : AdView



    lateinit var adpater: NotificationAdapter
    private lateinit var notificationViewModel: NotificationViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationViewModel=ViewModelProvider(this).get(NotificationViewModel::class.java)
        _binding= NotificationFragmentBinding.inflate(inflater,container,false)

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
        initailize(view)

        notificationViewModel.getMesssageError().observe(viewLifecycleOwner,{
            binding.pronoti.visibility=View.GONE


            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()
            }catch (e: Exception){

            }


                })
        notificationViewModel.getTopicsList().observe(viewLifecycleOwner,{
            binding.pronoti.visibility=View.GONE

            adpater= NotificationAdapter(requireContext(),it)
        binding.recNotification.addItemDecoration(SpacesItemDecoration(8))
        binding.recNotification.adapter=adpater
            binding.recNotification.layoutAnimation=layoutAnimationController

        })

    }

    private fun initailize(view: View) {
        navController = Navigation.findNavController(view)
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = "Notifications"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3")
                )
            )
        }

    }


}