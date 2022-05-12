package com.crashExams365.chemistry.Fragments.Test

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.databinding.TestFragmentBinding
import java.io.File

import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.crashExams365.chemistry.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.chemistry.Fragments.TopicsInChap.TopicsInChapClicked
import com.crashExams365.chemistry.Fragments.TopicsInChap.TopicsInChapDirections
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import com.crashExams365.chemistry.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


class TestFragment : Fragment() {
    private var _binding: TestFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null
    var rec_test_header: RecyclerView?=null
    var adapter:MyViewPagerAdapter?=null
    var adpaterforHeader:MyHeaderAdapetr?=null
    val args: TestFragmentArgs by navArgs<TestFragmentArgs>()
    var localFile = File.createTempFile("test", "pdf")
    private var mRewardedAd: RewardedAd? = null



    var view1:View?=null
    var layoutAnimationController: LayoutAnimationController?=null



    lateinit var toggle: ActionBarDrawerToggle
    var testType:String?=null
    var chapterSelected:StringBuilder?=null
    var hashmapForAns:HashMap<String,String> = HashMap()
    var hashmapForMarked:HashMap<String,String> = HashMap()
    var chaptername:String?=null
    var testNo:String?=null




    val TOTAL_TIME=48*1000*60
    var Time_Left=TOTAL_TIME
    var countDownTimer:CountDownTimer?=null

    private lateinit var userTestRefs: DatabaseReference
     private lateinit var testViewModel: TestViewModel
     var boolean:Boolean=false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (!boolean){

                    if (countDownTimer!=null){
                        val builder = AlertDialog.Builder(requireContext())
                        //set title for alert dialog
                        builder.setTitle("Leave Test")
                        //set message for alert dialog
                        builder.setMessage("Do You Want To leave the Test")
                        builder.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.unatt56565e))

                        //performing positive action
                        builder.setPositiveButton("Yes"){dialogInterface, which ->
                            dialogInterface.dismiss()
                            if (adapter!=null){
                                adapter!!.deleveryting()
                                Log.d(TAG, "handleOnBackPressed: caleed deleteing locale file")
                                if (localFile!=null){
                                    localFile.deleteRecursively()

                                }
                            }

                            isEnabled = false
                            activity?.onBackPressed()

                        }

                        //performing negative action
                        builder.setNegativeButton("No"){dialogInterface, which ->
                            dialogInterface.dismiss()
                        }
                        // Create the AlertDialog
                        val alertDialog: AlertDialog = builder.create()
                        // Set other dialog properties
                        alertDialog.setCancelable(false)
                        alertDialog.show()

                    }
                    else{
                        isEnabled = false
                        activity?.onBackPressed()
                    }

                }





            }
        })
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        testViewModel=ViewModelProvider(this).get(TestViewModel::class.java)
        _binding= TestFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
      //  getData()
        binding.loadigTest.visibility=View.VISIBLE
        testViewModel.getMesssageError().observe(viewLifecycleOwner,{
            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()

            }catch (e: java.lang.Exception){

            }



            binding.loadigTest.visibility=View.GONE

        })
        testViewModel.getPdfData(view,testType!!,chapterSelected.toString(),testNo!!,chaptername!!).observe(viewLifecycleOwner,{


            gosetOption(it.optionClass!!)
            localFile=it.pdfFile!!

            adpaterforHeader= MyHeaderAdapetr(requireContext(), it.optionClass!!.totque!!)
            rec_test_header!!.adapter=adpaterforHeader
            rec_test_header!!.layoutAnimation=layoutAnimationController

            val layMan= GridLayoutManager(requireContext(),3)
            layMan.orientation=RecyclerView.VERTICAL
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

            rec_test_header!!.layoutManager=layMan
            rec_test_header!!.addItemDecoration(SpacesItemDecoration(8))
            binding.loadigTest.visibility=View.GONE



            adapter= MyViewPagerAdapter(requireContext(),it.pdfFile!!,resources, it.optionClass!!.totque!!
            )
        binding.pager.adapter=adapter


            TabLayoutMediator(binding.tabLayout,binding.pager) { tab, position ->
            tab.text = "Question "+(position+1)
        }.attach()

        setcountdown()

        countDownTimer!!.start()
            binding.textTime.visibility=View.VISIBLE
            binding.finish.visibility=View.VISIBLE
            MobileAds.initialize(requireContext()) {}
            loadAd()




        })


    }

    private fun setcountdown() {
        countDownTimer=object:CountDownTimer(TOTAL_TIME.toLong(),1000){
            @SuppressLint("DefaultLocale")
            override fun onTick(interval: Long) {
                binding.textTime.text=(java.lang.String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(interval),TimeUnit.MILLISECONDS.toSeconds(interval)
                            -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(interval))))


                Time_Left-=1000


            }

            override fun onFinish() {
                this.cancel()
                finishgame()

            }

        }

    }

    private fun finishgame() {
        countDownTimer!!.cancel()
        binding.pager.visibility=View.GONE
        binding.textTime.visibility=View.GONE
        binding.finish.visibility=View.GONE
        binding.tabLayout.visibility=View.GONE
        doPreCal()



    }

    private fun gosetOption(optionClass: OptionClass) {
        for (i in 0 until optionClass.totque!!) {
            hashmapForAns[i.toString()] = optionClass.options!![i].opt!!
            hashmapForMarked[i.toString()] = "NULL"
        }
    }

    private fun initViews(view: View) {
        testType=args.testType
        testNo=args.testno

        chapterSelected= StringBuilder(args.chaptername)
        chaptername=args.chaptername
        chapterSelected!!.append(testNo)
        chapterSelected!!.append(".pdf")

        navController= Navigation.findNavController(view)
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.testTool)
            (activity as AppCompatActivity).supportActionBar?.title="Test"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3")))
        }
        toggle= ActionBarDrawerToggle(requireActivity(),binding.testDraw,binding.testTool,
            R.string.open,
            R.string.close)
        toggle.isDrawerIndicatorEnabled=true
        binding.testDraw.addDrawerListener(toggle)
        toggle.syncState()

        userTestRefs=FirebaseDatabase.getInstance().getReference("UserTestDone").child(FirebaseAuth.getInstance().currentUser!!.uid).child(testType.toString())
            .child(chaptername.toString())

        view1=binding.testNavView.getHeaderView(0)
        rec_test_header=view1!!.findViewById(R.id.rec_test_header)
        binding.finish.setOnClickListener{



            val builder = AlertDialog.Builder(requireContext())
            //set title for alert dialog
            builder.setTitle("Submit Test")
            //set message for alert dialog
            builder.setMessage("Do You Want To Finish The Test")
            builder.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.correct))

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->

                showAd()
                dialogInterface.dismiss()


            }

            //performing negative action
            builder.setNegativeButton("No"){dialogInterface, which ->
                dialogInterface.dismiss()
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()






        }
    }
    fun doPreCal(){
        try {


            binding.loadigTest.visibility=View.VISIBLE

            val marks:Long=calculateMarks()
            val dataObjectStore=DataObjectStore(testType,chaptername!!,hashmapForAns,hashmapForMarked
                ,true,null,marks,
                com.crashExams365.chemistry.CommonData.userInfo!!.firstname,
                com.crashExams365.chemistry.CommonData.userInfo!!.lastName)
            testViewModel.errorWhileUploading().observe(viewLifecycleOwner,{
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
                boolean=false

            })
            testViewModel.postDataAndReturn(dataObjectStore,testNo!!,FirebaseAuth.getInstance().currentUser!!.uid).observe(viewLifecycleOwner,{
                if (it==true){
                    if (adapter!=null){
                        adapter!!.deleveryting()
                    }
                    goToResultFragment()
                }
            })



        }catch (e:Exception){

        }

    }

    fun goToResultFragment(){

        val obj=PassObj(testType.toString(), chaptername!!,hashmapForAns,hashmapForMarked,localFile,testNo.toString())
        val action= TestFragmentDirections.actionTestFragmentToResultFragments(obj)
        navController!!.navigate(action)

    }

    private fun calculateMarks():Long{
        var marks:Long=0
        val len:Int=hashmapForMarked.size
        for (i in 0 until len){

            if (hashmapForMarked[i.toString()].equals(hashmapForAns[i.toString()])){
                marks+=4
            }
            else if (!hashmapForMarked[i.toString()].equals("NULL")){
                marks-=1
            }
        }
        return marks
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onLaunchCategoryClicked(event: OptionClickedPager) {

        if (event.issuccess) {


            rec_test_header!!.findViewHolderForAdapterPosition(event.pos)!!.itemView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lightblue))

            when(event.Option){
                "A"->            hashmapForMarked[event.pos.toString()]="A"
                "B"->            hashmapForMarked[event.pos.toString()]="B"
                "C"->            hashmapForMarked[event.pos.toString()]="C"
                "D"->            hashmapForMarked[event.pos.toString()]="D"
            }


        }
        else{
            rec_test_header!!.findViewHolderForAdapterPosition(event.pos)!!.itemView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.testque))
            hashmapForMarked[event.pos.toString()]="NULL"
        }

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onLaunchCategoryCl(event:HeaderQuesTap) {

        if (event.issuccess) {
            binding.testDraw.closeDrawer(GravityCompat.START)
            binding.pager.setCurrentItem(event.pos,false)
        }

    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onLaunchCatego(event:MoveClikedEvent) {

        if (event.issuccess) {
            when (event.view.tag) {
                "NEX"->binding.pager.setCurrentItem((event.pos+1),true)
                "PRE"->binding.pager.setCurrentItem((event.pos-1),true)
                "REW"->{
                    binding.pager.setCurrentItem((event.pos+1),true)
                    rec_test_header!!.findViewHolderForAdapterPosition(event.pos)!!.itemView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lightorange))

                }

            }

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


    private fun showAd() {


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
                godostuffs()
                Log.d(TAG, "showAd: done")



                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.getAmount()
                    var rewardType = rewardItem.getType()


                    Log.d(TAG, "User earned the reward.")
                    // Toast.makeText(this, "User earned the reward.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.d(ContentValues.TAG, "The rewarded ad wasn't ready yet.")
            godostuffs()
            //  Toast.makeText(this, "The rewarded ad wasn't ready yet.", Toast.LENGTH_SHORT).show()
        }


    }

    private fun godostuffs() {
        boolean=true


        binding.pager.visibility=View.GONE
        binding.textTime.visibility=View.GONE
        binding.finish.visibility=View.GONE
        binding.tabLayout.visibility=View.GONE
        doPreCal()
        countDownTimer!!.cancel()

    }

    private fun loadAd() {
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            requireContext(),
            "ca-app-pub-3543422873793913/7355490071",
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