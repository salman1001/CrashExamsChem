package com.crashExams365.chemistry.Fragments.Launch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.common.IRecListener
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus

class MyCatAdpater(var context:Context,var catList:ArrayList<CatModel>):RecyclerView.Adapter<MyCatAdpater.MyViewHolder>() {
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var cat_name:TextView?=null
        var cat_image:ImageView?=null
        private var listener:IRecListener?=null
        fun setListener(listener: IRecListener){
            this.listener=listener
        }
        init {
            cat_name=itemView.findViewById(R.id.cat_name) as TextView
            cat_image=itemView.findViewById(R.id.cat_imag) as CircleImageView
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            listener!!.onItemClick(v!!,adapterPosition)

        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_for_cat,parent,false))


    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cat_name!!.text = catList[position].name
        when(catList[position].id){
            1->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bookstack111111))}
            2->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.p2))}
            3->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.previ11111111111111))}
            4->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.hcverhjkmaji))}
            6->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.noti))}
            7->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ier9832))}
            8->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.dcpandey1111111))}
            9->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.cutofff88888))}
            10->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.reee3333))}
            12->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.rateingg1111))}
            13->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.sahre))}
            14->{holder.cat_image!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.sahre))}



        }
        holder.setListener(object:IRecListener{
            override fun onItemClick(view: View, pos: Int) {
                EventBus.getDefault().post(LaunchCategoryClick(true, catList[pos]))
            }

        })
    }
    override fun getItemCount(): Int {
       return catList.size
    }
    override fun getItemViewType(position: Int): Int {
        return if (catList.size==1)
            0
        else{
            if (catList.size%3==0)
                0
            else
                if (position>1&&position==catList.size-1) 1 else 0
        }
    }
}