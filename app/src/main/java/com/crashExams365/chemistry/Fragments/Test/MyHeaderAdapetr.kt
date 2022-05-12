package com.crashExams365.chemistry.Fragments.Test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.common.IRecListener
import org.greenrobot.eventbus.EventBus

class MyHeaderAdapetr(var context: Context,var size:Int) :
    RecyclerView.Adapter<MyHeaderAdapetr.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_que_header_item, parent, false))

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.que_no!!.text=(position+1).toString()
        holder.setListener(object : IRecListener {
            override fun onItemClick(view: View, pos: Int) {
                EventBus.getDefault().post(HeaderQuesTap(true,holder.adapterPosition))

            }

        })


    }

    override fun getItemCount(): Int {
        return size
    }




    override fun getItemViewType(position: Int): Int {
        return if (size==1)
            0
        else{
            if (size%3==0)
                0
            else
                if (position>1&&position==size-1) 1 else 0
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
            que_no=itemView.findViewById(R.id.ques_no) as TextView

        }

    }

}