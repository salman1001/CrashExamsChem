package com.crashExams365.chemistry.Fragments.Request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.RequestFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import java.lang.Exception
import java.util.*

class RequestFragment : Fragment() {
    private var _binding: RequestFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null

    private lateinit var userref: DatabaseReference
    private lateinit var requestViewModel: RequestViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requestViewModel=ViewModelProvider(this).get(RequestViewModel::class.java)
        _binding= RequestFragmentBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonsubmit.setOnClickListener{
            binding.buttonsubmit.visibility=View.GONE
            binding.prosub.visibility=View.VISIBLE
            val messages:String=binding.phOrEmailname.text.toString()
            val requestItem:RequestItem= RequestItem()
            requestItem.mess=messages
            val op:String =Calendar.getInstance().timeInMillis.toString()
            requestViewModel.getMesssageError().observe(viewLifecycleOwner,{
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.pri)).show()
                binding.prosub.visibility=View.GONE
                binding.buttonsubmit.visibility=View.VISIBLE
            })
            requestViewModel.getTopicsList(requestItem,op).observe(viewLifecycleOwner,{
                if (it==true){
                    binding.prosub.visibility=View.GONE
                    binding.buttonsubmit.visibility=View.VISIBLE


                    try {

                        Snackbar.make(view, "Done Mate, We Will Get it For You", Snackbar.LENGTH_LONG).setBackgroundTint(
                            ContextCompat.getColor(requireContext(), R.color.pri)).show()

                    }catch (e: Exception){

                    }

                    requireActivity().onBackPressed()
                }
            })
        }

    }




}