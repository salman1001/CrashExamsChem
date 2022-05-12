package com.crashExams365.chemistry.Fragments.Doubts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.crashExams365.chemistry.Fragments.Launch.LaunchFragmentDirections
import com.crashExams365.chemistry.Fragments.Launch.LaunchViewModel
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.DoubtsFragmentBinding
import com.crashExams365.chemistry.databinding.LaunchFragmentBinding

class DoubtsFragment : Fragment() {

    private var _binding: DoubtsFragmentBinding? = null
    private val binding get() = _binding!!
    var navController: NavController? = null

    private lateinit var viewModel: DoubtsViewModel

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
        navController = Navigation.findNavController(view)
        binding.newDoubts.setOnClickListener{
            val action = DoubtsFragmentDirections.actionDoubtsFragmentToAskNewDoubtsFragment()
            navController!!.navigate(action)

        }
    }



}