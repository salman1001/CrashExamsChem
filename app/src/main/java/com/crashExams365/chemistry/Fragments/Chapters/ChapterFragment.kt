package com.crashExams365.chemistry.Fragments.Chapters
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
import com.crashExams365.chemistry.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.ChapterFragmentBinding
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class ChapterFragment : Fragment() {
    private var _binding: ChapterFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null
    lateinit var mAdView : AdView




    var adpater: MyChapterAdapter?=null
    private lateinit var chapViewModel: ChapterViewModel
    var layoutAnimationController:LayoutAnimationController?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chapViewModel= ViewModelProvider(this).get(ChapterViewModel::class.java)

        _binding= ChapterFragmentBinding.inflate(inflater,container,false)
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


        chapViewModel.getMesssageError().observe(viewLifecycleOwner,{
            binding.prochap.visibility=View.GONE
            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()
            }catch (e:Exception){

            }

        })
       // binding.prolaunch.visibility=View.VISIBLE

        chapViewModel.getCatList().observe(viewLifecycleOwner,{
            binding.prochap.visibility=View.GONE
            adpater= MyChapterAdapter(
                requireContext(),
                it
            )
            binding.recChapters.adapter=adpater
            binding.recChapters.layoutAnimation=layoutAnimationController


        })
    }



    private fun goinitViews(view: View) {
        navController= Navigation.findNavController(view)
        layoutAnimationController=AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title="Chapters"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3"))
            )
        }
        binding.recChapters.addItemDecoration(SpacesItemDecoration(8))
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
    fun onChapterClicked(event:ChapterCliked){
        if (event.issuccess){

            val action=

                ChapterFragmentDirections.actionChapterFragmentToTopicsInChap(
                    event.catModel.name.toString(),
                    "TopicsInChap"
                )
            navController!!.navigate(action)
        }

    }
}