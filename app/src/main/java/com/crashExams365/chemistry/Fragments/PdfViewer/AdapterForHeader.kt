package com.crashExams365.chemistry.Fragments.PdfViewer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.common.IRecListener
import org.greenrobot.eventbus.EventBus

class AdapterForHeader(var context: Context, var topicsList:List<HeaderTopicsModel>): RecyclerView.Adapter<AdapterForHeader.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var cat_name: TextView?=null

        private var listener: IRecListener?=null
        fun setListener(listener: IRecListener){
            this.listener=listener
        }
        init {
            cat_name=itemView.findViewById(R.id.topic_namess) as TextView
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            listener!!.onItemClick(v!!,adapterPosition)

        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_for_header_item,parent,false))


    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cat_name!!.text = topicsList[position].name
        holder.setListener(object: IRecListener {
            override fun onItemClick(view: View, pos: Int) {
                EventBus.getDefault().post(HeaderTopicClicked(true, topicsList[pos]))
            }

        })
    }
    override fun getItemCount(): Int {
        return topicsList.size
    }

}