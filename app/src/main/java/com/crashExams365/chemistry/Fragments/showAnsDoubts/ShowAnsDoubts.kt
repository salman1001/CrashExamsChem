package com.crashExams365.chemistry.Fragments.showAnsDoubts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crashExams365.chemistry.R

class ShowAnsDoubts : Fragment() {

    companion object {
        fun newInstance() = ShowAnsDoubts()
    }

    private lateinit var viewModel: ShowAnsDoubtsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.show_ans_doubts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShowAnsDoubtsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}