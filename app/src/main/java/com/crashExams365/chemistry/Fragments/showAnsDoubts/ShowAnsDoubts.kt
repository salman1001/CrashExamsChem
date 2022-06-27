package com.crashExams365.chemistry.Fragments.showAnsDoubts

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.crashExams365.chemistry.Fragments.Doubts.QuestionAndAns
import com.crashExams365.chemistry.Fragments.ResultFragment.ObjectShowAns
import com.crashExams365.chemistry.Fragments.ShowAnswers.ShowAnswersArgs
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.databinding.ShowAnsDoubtsFragmentBinding
import com.crashExams365.chemistry.databinding.ShowAnswersFragmentBinding

class ShowAnsDoubts : Fragment() {


    private var _binding: ShowAnsDoubtsFragmentBinding?=null
    private val binding get() =_binding!!
    var navController: NavController?=null
    val args: ShowAnsDoubtsArgs by navArgs<ShowAnsDoubtsArgs>()
    var objectShowAns: QuestionAndAns?=null



    private lateinit var viewModel: ShowAnsDoubtsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= ShowAnsDoubtsFragmentBinding.inflate(inflater,container,false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        objectShowAns=args.doubtsAnsModel;
        Glide.with(requireContext()).load(objectShowAns!!.questionurl).into(binding.myqueimge1)


        binding.showclicked1.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(true)



            val inflater = layoutInflater
            val dialoglayout: View = inflater.inflate(R.layout.show_ans_doubts_pop_up_item, null)
            val imageView=dialoglayout.findViewById<ImageView>(R.id.myqueimge4);
            Glide.with(requireContext()).load(objectShowAns!!.ansUrl).into(imageView)

            builder.setView(dialoglayout)
            builder.show()
        }
    }



}