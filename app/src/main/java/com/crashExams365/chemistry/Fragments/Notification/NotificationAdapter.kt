package com.crashExams365.chemistry.Fragments.Notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.R

class NotificationAdapter(var context: Context, var messageList:List<MessageClass>):
    RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.notifiction_item, parent, false)
        )


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var message_title: TextView? = null
        var message_body: TextView? = null
        var date: TextView? = null




        init {
            message_title = itemView.findViewById(R.id.title_notifi) as TextView
            message_body=itemView.findViewById(R.id.title_body) as TextView
            date=itemView.findViewById(R.id.title_date) as TextView



        }




    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.message_title!!.text = messageList[position].title
        holder.message_body!!.text = messageList[position].body
        holder.date!!.text=messageList[position].date


    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}