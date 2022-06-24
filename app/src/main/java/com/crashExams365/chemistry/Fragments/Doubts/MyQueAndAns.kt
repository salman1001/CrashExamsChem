package com.crashExams365.chemistry.Fragments.Doubts

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crashExams365.chemistry.Fragments.Notification.MessageClass
import com.crashExams365.chemistry.R
import com.google.android.material.card.MaterialCardView

class MyQueAndAns(var context: Context, var messageList:List<QuestionAndAns>):
    RecyclerView.Adapter<MyQueAndAns.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.que_ans_item, parent, false)
        )


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var date: ImageView? = null
        var imageView:ImageView?=null




        init {

            date=itemView.findViewById(R.id.myqueimge) as ImageView
            imageView=itemView.findViewById(R.id.checkAns) as ImageView



        }




    }



    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(messageList[position].questionurl).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true).into(holder.date!!)

      //  holder.date!!.text=messageList[position].date
        if (messageList[position].ansUrl!!.length!=0){
            holder.imageView!!.visibility=View.VISIBLE

        }
        else{
            holder.imageView!!.visibility=View.GONE


        }


    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}