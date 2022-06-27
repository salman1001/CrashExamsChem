package com.crashExams365.chemistry.Fragments.Doubts

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.crashExams365.chemistry.Fragments.Chapters.ChapterCliked
import com.crashExams365.chemistry.Fragments.Chapters.ChapterFragmentDirections
import com.crashExams365.chemistry.Fragments.Launch.LaunchFragmentDirections
import com.crashExams365.chemistry.Fragments.Launch.LaunchViewModel
import com.crashExams365.chemistry.Fragments.Launch.SpacesItemDecoration
import com.crashExams365.chemistry.Fragments.Notification.NotificationAdapter
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.DoubtsFragmentBinding
import com.crashExams365.chemistry.databinding.LaunchFragmentBinding
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class DoubtsFragment : Fragment() {

    private var _binding: DoubtsFragmentBinding? = null
    private val binding get() = _binding!!
    var navController: NavController? = null
    var layoutAnimationController: LayoutAnimationController?=null
    lateinit var adpater: MyQueAndAns



     var viewModel: DoubtsViewModel?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=ViewModelProvider(this).get(DoubtsViewModel::class.java)
        _binding = DoubtsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // Toast.makeText(requireContext(),"Salman",Toast.LENGTH_LONG);

        navController = Navigation.findNavController(view)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = "Doubts"
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#486BD3")
                )
            )
        }
        binding.prodoubts.visibility=View.VISIBLE




        viewModel!!.getMesssageError().observe(viewLifecycleOwner,{
          //  binding.pronoti.visibility=View.GONE
            binding.prodoubts.visibility=View.GONE



            try {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()
            }catch (e: Exception){

            }


        })
        viewModel!!.getTopicsList().observe(viewLifecycleOwner,{
            binding.prodoubts.visibility=View.GONE

            // binding.pronoti.visibility=View.GONE
            if (it.size>0){
                binding.displayMessage.visibility=View.GONE;
            }
            else{
                binding.displayMessage.visibility=View.VISIBLE;

            }

            adpater= MyQueAndAns(requireContext(),it)
            binding.recQueAndAns.addItemDecoration(SpacesItemDecoration(8))
            binding.recQueAndAns.adapter=adpater
            binding.recQueAndAns.layoutAnimation=layoutAnimationController


        })







        binding.floatingActionButton.setOnClickListener{
            val action = DoubtsFragmentDirections.actionDoubtsFragmentToAskNewDoubtsFragment()
            navController!!.navigate(action)


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
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    fun DoubtsClicked(event: DoubtsClicked){
        if (event.issuccess){

         //   val action= ChapterFragmentDirections.actionChapterFragmentToTopicsInChap(event.catModel.name.toString(), "TopicsInChap")
         //   navController!!.navigate(action)
        }

    }



}