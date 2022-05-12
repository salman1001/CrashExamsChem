package com.crashExams365.chemistry.Fragments.TestYearRep

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.common.IRecListener
import org.greenrobot.eventbus.EventBus

class PreAdapter(var context: Context,var yearmodel:List<YearModel>) :
    RecyclerView.Adapter<PreAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_pre_year, parent, false))

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.que_no!!.text=yearmodel[position].name.toString()
        holder.setListener(object : IRecListener {
            override fun onItemClick(view: View, pos: Int) {
                EventBus.getDefault().post(YearClicked(true,yearmodel[pos]))

            }

        })


    }

    override fun getItemCount(): Int {
        return yearmodel.size
    }




    override fun getItemViewType(position: Int): Int {
        return if (yearmodel.size==1)
            0
        else{
            if (yearmodel.size%3==0)
                0
            else
                if (position>1&&position==yearmodel.size-1) 1 else 0
        }
    }

  inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var que_no: TextView?=null
        private var listener: IRecListener? = null
        fun setListener(listener: IRecListener) {
            this.listener = listener
        }

        init {

            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            listener!!.onItemClick(v!!, adapterPosition)

        }



        init {
            que_no=itemView.findViewById(R.id.year_no) as TextView

        }

    }

}