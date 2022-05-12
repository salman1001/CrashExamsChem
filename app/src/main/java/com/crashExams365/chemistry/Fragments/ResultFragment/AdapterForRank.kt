package com.crashExams365.chemistry.Fragments.ResultFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.Fragments.Test.DataObjectStore
import com.crashExams365.chemistry.R
import java.lang.StringBuilder

class AdapterForRank(var context: Context, var chapterlist:List<DataObjectStore>):
    RecyclerView.Adapter<AdapterForRank.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rank: TextView? = null
        var name: TextView? = null
        var marks: TextView? = null







        init {
            rank = itemView.findViewById(R.id.rank_no) as TextView
            name = itemView.findViewById(R.id.rank_holder_name) as TextView
            marks = itemView.findViewById(R.id.marks_otained_holer) as TextView


        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.rank_of_users_in_list, parent, false)
        )


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var fullName=StringBuilder("")
        fullName.append(chapterlist[position].userName).append(" ").append(chapterlist[position].userlastName)
        holder.rank!!.text = (position+1).toString()

        holder.name!!.text = fullName
        holder.marks!!.text = chapterlist[position].totalMarksObtained.toString()

    }

    override fun getItemCount(): Int {
        return chapterlist.size
    }
}