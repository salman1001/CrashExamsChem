package com.crashExams365.chemistry.Fragments.TestYearRep

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.TestYearRepFragmentBinding
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class TestYearRep : Fragment() {
    private var _binding: TestYearRepFragmentBinding? = null
    private val binding get() = _binding!!
    var navController: NavController? = null

    var adpaterforHeader: PreAdapter?=null
    val args: TestYearRepArgs by navArgs<TestYearRepArgs>()
    private lateinit var testYearRepViewModel: TestYearRepViewModel
    var layoutAnimationController: LayoutAnimationController?=null


    lateinit var mAdView : AdView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        testYearRepViewModel=ViewModelProvider(this).get(TestYearRepViewModel::class.java)

        _binding = TestYearRepFragmentBinding.inflate(inflater, container, false)



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
        initviews(view)




        testYearRepViewModel.getMesssageError().observe(viewLifecycleOwner,{

            binding.protestrep.visibility=View.GONE

            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()

            }catch (e: Exception){

            }


        })

        testYearRepViewModel.getTopicsList(args.ifany).observe(viewLifecycleOwner,{
            binding.protestrep.visibility=View.GONE

            adpaterforHeader= PreAdapter(requireContext(), it)
            binding!!.recPreYe.adapter=adpaterforHeader


            val layMan= GridLayoutManager(requireContext(),3)
            layMan.orientation= RecyclerView.VERTICAL
            layMan.spanSizeLookup=object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    return if (adpaterforHeader!=null)
                    {
                        when(adpaterforHeader!!.getItemViewType(position)){
                            0->1
                            1->2
                            else ->-1
                        }
                    }
                    else{
                        -1
                    }
                }

            }
            binding.recPreYe.layoutManager=layMan
            binding!!.recPreYe.addItemDecoration(SpacesItemDecoration(15))
            binding.recPreYe.layoutAnimation=layoutAnimationController



        })

    }



    private fun initviews(view: View) {
        navController= Navigation.findNavController(view)
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

        var title:String=""
        when(args.ifany){
            "ResultsRank"->title="Cut Off"
            "NONE"->title="Previous Years Papers"
        }

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.testTool)
            (activity as AppCompatActivity).supportActionBar?.title=title
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3"))
            )
        }
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
    fun onLaunchCategoryClicked(event: YearClicked) {
        if (event.issuccess) {
            if (args.ifany=="NONE"){
                val action=TestYearRepDirections.actionTestYearRepToTopicsInChap(event.yearModel.name.toString(),"PreYearWise")
                navController!!.navigate(action)
            }
            else{
                val action=TestYearRepDirections.actionTestYearRepToDocumentViewer("ResultsRank",event.yearModel.name.toString())
                navController!!.navigate(action)            }
        }
    }
}