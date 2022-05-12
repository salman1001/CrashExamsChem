package com.crashExams365.chemistry.Fragments.Launch

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.LaunchFragmentBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.smarteist.autoimageslider.SliderAnimations


class LaunchFragment : Fragment(R.layout.launch_fragment) {

//////////////////////////////////////////////     INSTANCE VARIABLES   //////////////////////////////////////////////////////////////////////////////////////////
    private var _binding: LaunchFragmentBinding? = null
    private val binding get() = _binding!!
    var navController: NavController? = null
    lateinit var toggle: ActionBarDrawerToggle

    lateinit var adpater: MyCatAdpater
    var view1:View?=null
    lateinit var textViewemail:TextView
    lateinit var textViewname:TextView

    private lateinit var launchViewModel: LaunchViewModel
    val versionno:String="D"
    var mycheck:String?=null
    var baccking:Int=0
    var layoutAnimationController: LayoutAnimationController?=null
    var versionFromBack:String?=null

    private var mInterstitialAd: InterstitialAd? = null











//////////////////////////////////////////////     ON CREATE VIEW   //////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        launchViewModel=ViewModelProvider(this).get(LaunchViewModel::class.java)
        _binding = LaunchFragmentBinding.inflate(inflater, container, false)
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),"ca-app-pub-3543422873793913/7241678119", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
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

//////////////////////////////////////////////     ON BACK PRESSED   //////////////////////////////////////////////////////////////////////////////////////////


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                baccking++
                if (baccking>1){
                    isEnabled = false
                    activity?.onBackPressed()
                }
                else{
                    try {
                        Snackbar.make(binding.view, "Press Again to exit ", Snackbar.LENGTH_SHORT).setBackgroundTint(
                            ContextCompat.getColor(requireContext(), R.color.pri)).show()
                    }catch (e:Exception){

                    }
                             }



            }
        })
    }



//////////////////////////////////////////////     ON VIEW CREATED  //////////////////////////////////////////////////////////////////////////////////////////


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInit(view)

        launchViewModel.getMesssageError().observe(viewLifecycleOwner,{
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                ContextCompat.getColor(requireContext(), R.color.pri)).show()
        })
        launchViewModel.getCatMesssageError().observe(viewLifecycleOwner,{
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                ContextCompat.getColor(requireContext(), R.color.pri)).show()
        })
        binding.prolaunch.visibility=View.VISIBLE

        launchViewModel.getBannerList().observe(viewLifecycleOwner,{ it ->
            versionFromBack=it.status.toString()
            if (versionno==versionFromBack){
//                binding.slider.setAdapter(MySliderAdapter(it.url!!))
//                binding.slider.setLoopSlides(true)
//                binding.slider.setInterval(3500)
//                binding.slider.setAnimateIndicators(true)

                val sliderAdapter = SliderAdapter(it,requireContext())
                binding.imageSlider.setSliderAdapter(sliderAdapter)
                binding.imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
                binding.imageSlider.startAutoCycle()
              //  binding.imageSlider

                launchViewModel.getCatList().observe(viewLifecycleOwner,{
                    mycheck=it.status

                    binding.prolaunch.visibility=View.GONE
                    binding.view.visibility=View.VISIBLE
                    binding.holder.visibility=View.VISIBLE
//                    val catModel=CatModel()
//                    catModel.id=14
//                    catModel.name="Doubts"
//                    val list:ArrayList<CatModel> =it.Category!!
//                    list.add(2,catModel)

                    adpater = MyCatAdpater(requireContext(), it.Category!!)
                    binding.recCat.adapter = adpater
                    binding.recCat.layoutAnimation=layoutAnimationController
                    binding.prolaunch.visibility = View.GONE
                })


            }
            else{
                binding.prolaunch.visibility = View.GONE
                showdialog()
            }
        })
    }

//////////////////////////////////////////////     DO INITIALIZING   //////////////////////////////////////////////////////////////////////////////////////////


    private fun doInit(view: View) {
        try {

            navController = Navigation.findNavController(view)

            layoutAnimationController= AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

            if (activity is AppCompatActivity) {
                (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
                (activity as AppCompatActivity).supportActionBar?.title = "Physics"
                (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.parseColor("#486BD3")
                    )
                )
            }
            toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLay, binding.toolbar, R.string.open, R.string.close)
            toggle.isDrawerIndicatorEnabled = true
            binding.drawerLay!!.addDrawerListener(toggle)
            toggle.syncState()
            binding.navView!!.setNavigationItemSelectedListener {
                if (versionFromBack==versionno){
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(requireActivity())
                    }

                    when (it.itemId) {



                        R.id.nav_chapters ->{
                            val action = LaunchFragmentDirections.actionLaunchFragmentToChapterFragment()
                            navController!!.navigate(action)
                        }
                        R.id.nav_hcverma ->{
                            val action = LaunchFragmentDirections.actionLaunchFragmentToTopicsInChap("NONE","Hc Verma")
                            navController!!.navigate(action)
                        }
                        R.id.nav_dc->{
                            val action = LaunchFragmentDirections.actionLaunchFragmentToTopicsInChap("NONE","Dc Pandey")
                            navController!!.navigate(action)
                        }
                        R.id.nav_iro ->{
                            val action = LaunchFragmentDirections.actionLaunchFragmentToTopicsInChap("NONE","IE IRODOV")
                            navController!!.navigate(action)
                        }


                        R.id.nav_prev ->{
                            val action = LaunchFragmentDirections.actionLaunchFragmentToTestYearRep("NONE")
                            navController!!.navigate(action)
                        }
                        R.id.nav_req ->{
                            val action = LaunchFragmentDirections.actionLaunchFragmentToRequestFragment()
                            navController!!.navigate(action)

                        }
                        R.id.nav_jeenoti->{
                            val action = LaunchFragmentDirections.actionLaunchFragmentToNotificationFragment()
                            navController!!.navigate(action)
                        }
                        R.id.nav_cutoff->{
                            val action = LaunchFragmentDirections.actionLaunchFragmentToTestYearRep("ResultsRank")
                            navController!!.navigate(action)
                        }
                        R.id.nav_tests->{
                            val action = LaunchFragmentDirections.actionLaunchFragmentToTopicsInChap("Tests","TestTypes")
                            navController!!.navigate(action)
                        }


                        R.id.nav_rate->{
                            goreview()
                        }
                        R.id.nav_share->{
                            goShare()
                        }
                    }

                }
                else{
                    showdialog()
                }
                binding.drawerLay!!.closeDrawer(GravityCompat.START)
                true

            }
            //   Slider.init(PicassoImageLoadingServices(requireContext()))

            view1=binding.navView.getHeaderView(0)
            textViewname=view1!!.findViewById(R.id.user_name)
            textViewemail=view1!!.findViewById(R.id.user_email_ph)
            textViewname.setText(com.crashExams365.chemistry.CommonData.userInfo!!.firstname)
            textViewemail.setText(com.crashExams365.chemistry.CommonData.userInfo!!.EmOrPh)


            val layMan = GridLayoutManager(requireContext(), 3)
            layMan.orientation = RecyclerView.VERTICAL
            layMan.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adpater.getItemViewType(position)) {
                        0 -> 1
                        1 -> 2
                        else -> -1
                    }
                }

            }
            binding.recCat.layoutManager = layMan
            binding.recCat.addItemDecoration(SpacesItemDecoration(8))


        }catch (e:java.lang.Exception){

        }

    }



    private fun showdialog() {
        val builder = AlertDialog.Builder(requireContext())
        //set title for alert dialog
        builder.setTitle("Update")
        //set message for alert dialog
        builder.setMessage("Please Update Your App")
        builder.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.unatt56565e))

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            //take to playStore
            dialogInterface.dismiss()
            Snackbar.make(binding.view, "Taking You To Playstore", Snackbar.LENGTH_SHORT).setBackgroundTint(
                ContextCompat.getColor(requireContext(), R.color.pri)).show()
          //  val playstoreuri2: Uri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)


            val playstoreuri:Uri=Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
            try {


            val playstoreIntent1: Intent = Intent(Intent.ACTION_VIEW, playstoreuri)
            startActivity(playstoreIntent1)
                activity!!.finish()

        }catch (exp:Exception){

            //val playstoreuri2: Uri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)
            var playstoreuri12:Uri=Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
            val playstoreIntent2: Intent = Intent(Intent.ACTION_VIEW, playstoreuri12)
            startActivity(playstoreIntent2)
                activity!!.finish()

            }


    }.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
            baccking=2
            activity!!.onBackPressed()

        }

        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()


    }
//////////////////////////////////////////////     ON DESTROY   //////////////////////////////////////////////////////////////////////////////////////////


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//////////////////////////////////////////////     ON START   //////////////////////////////////////////////////////////////////////////////////////////



    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: called$baccking")
        baccking=0
        EventBus.getDefault().register(this)
    }
//////////////////////////////////////////////     ON STOP   //////////////////////////////////////////////////////////////////////////////////////////

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

//////////////////////////////////////////////     ON CLICKED CALLED   //////////////////////////////////////////////////////////////////////////////////////////

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onLaunchCategoryClicked(event: LaunchCategoryClick) {
        if (event.issuccess) {


            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            }



            when (event.catModel.name) {
                "Chapters" -> {
                    val action = LaunchFragmentDirections.actionLaunchFragmentToChapterFragment()
                    navController!!.navigate(action)
                }

                "Doubts" -> {
                    val action = LaunchFragmentDirections.actionLaunchFragmentToDoubtsFragment()
                    navController!!.navigate(action)
                }
                "Request" -> {
                    val action = LaunchFragmentDirections.actionLaunchFragmentToRequestFragment()
                    navController!!.navigate(action)

                }
                "Notification" -> {
                    val action = LaunchFragmentDirections.actionLaunchFragmentToNotificationFragment()
                    navController!!.navigate(action)
                }
                "Tests" -> {
                    val action = LaunchFragmentDirections.actionLaunchFragmentToTopicsInChap("Tests","TestTypes")
                    navController!!.navigate(action)
                }
                "Previous" -> {
                    val action = LaunchFragmentDirections.actionLaunchFragmentToTestYearRep("NONE")
                    navController!!.navigate(action)
                }
                "Hc Verma" -> {
                    val action = LaunchFragmentDirections.actionLaunchFragmentToTopicsInChap("NONE","Hc Verma")
                    navController!!.navigate(action)
                }
                "IE IRODOV" -> {
                    val action = LaunchFragmentDirections.actionLaunchFragmentToTopicsInChap("NONE","IE IRODOV")
                    navController!!.navigate(action)
                }
                "Dc Pandey" -> {
                    val action = LaunchFragmentDirections.actionLaunchFragmentToTopicsInChap("NONE","Dc Pandey")
                    navController!!.navigate(action)
                }
                "CutOff"->{
                    val action = LaunchFragmentDirections.actionLaunchFragmentToTestYearRep("ResultsRank")
                    navController!!.navigate(action)

                }
                "Rate"->{
                    goreview()
                }
                "Share"->{
                    goShare()
                }
            }

            }
    }

    private fun goShare() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage = """${shareMessage}https://play.google.com/store/apps/details?id=${requireContext().packageName}""".trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: java.lang.Exception) {
        }
    }
    private fun goreview(){


        val playstoreuri:Uri=Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
        try {


            val playstoreIntent1: Intent = Intent(Intent.ACTION_VIEW, playstoreuri)
            startActivity(playstoreIntent1)

        }catch (exp:Exception){

            //val playstoreuri2: Uri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)
            var playstoreuri12:Uri=Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
            val playstoreIntent2: Intent = Intent(Intent.ACTION_VIEW, playstoreuri12)
            startActivity(playstoreIntent2)

        }



//        val reviewManager: ReviewManager = ReviewManagerFactory.create(requireContext())
//
//        val requestReviewTask: Task<ReviewInfo> = reviewManager.requestReviewFlow()
//        requestReviewTask.addOnCompleteListener { request ->
//            if (request.isSuccessful) {
//                // Request succeeded and a ReviewInfo instance was received
//                val reviewInfo: ReviewInfo = request.result
//
//                val launchReviewTask: Task<Void> = reviewManager.launchReviewFlow(requireActivity(), reviewInfo)
//                launchReviewTask.addOnCompleteListener { _ ->
//                    // The review has finished, continue your app flow.
//                }
//            } else {
//                // Request failed
//            }
//
//
//
//        }
    }
}
