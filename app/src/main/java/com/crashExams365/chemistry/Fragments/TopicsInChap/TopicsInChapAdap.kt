package com.crashExams365.chemistry.Fragments.TopicsInChap

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.common.IRecListener
import com.makeramen.roundedimageview.RoundedImageView
import org.greenrobot.eventbus.EventBus

class TopicsInChapAdap(var context: Context, var chapterlist:List<TopicsInChapModel>,var chapterSlected:String,var title:String,var type:String):
    RecyclerView.Adapter<TopicsInChapAdap.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var topicname: TextView? = null
        var chapterimage: ImageView?=null
        var checkmee:ImageView?=null


        private var listener: IRecListener? = null
        fun setListener(listener: IRecListener) {
            this.listener = listener
        }

        init {
            topicname = itemView.findViewById(R.id.chapter_name) as TextView
            chapterimage=itemView.findViewById(R.id.imag_chapter) as RoundedImageView
            checkmee=itemView.findViewById(R.id.checkmeee) as ImageView

            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            listener!!.onItemClick(v!!, adapterPosition)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_for_chapters, parent, false)
        )


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (chapterlist[position].done){

            holder.checkmee!!.visibility=View.VISIBLE
            holder.checkmee!!.setBackgroundColor(Color.GREEN)

        }
        else{
            holder.checkmee!!.visibility=View.GONE
        }
        holder.topicname!!.text = chapterlist[position].name
//        when(chapterlist[position].id){
//            1-> holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.indexx))
//            2-> holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.theory))
//            3-> holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.solved))
//            4-> holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.previousssspa))
//            5-> holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.prac))
//        }

        //holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.theory))


//            if (title == "NULL"){
//                when(title){
//                    "Hc Verma"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.hcverhjkmaji))
//                    "Dc Pandey"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.dcpandey1111111))
//                    "IE IRODOV"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ier9832))
//                    "Tests"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.testsserer444))
//
//                }
//
//            }
//        else{
            when(type){
                "TopicsInChap"->{
                    when(chapterlist[position].id){
                        1->{
                            holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.indexx))
                        }
                        2->{
                            holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.theory))
                        }
                        3->{
                            holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bookstack111111))
                        }
                        4->{
                            holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.excercise))
                        }
                        5->{
                            holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.previ11111111111111))                        }
                        6->{
                            holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.testsserer444))
                        }


                    }

                }
                "Hc Verma"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.hcverhjkmaji))
                    "Dc Pandey"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.dcpandey1111111))
                    "IE IRODOV"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ier9832))
                    "Tests"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.testsserer444))
                "TestTypes"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.testsserer444))
                "Part Test"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.testsserer444))
                "Full Test"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.testsserer444))
                "chapterwise"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.testsserer444))
                "PreYearWise"->holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.pre34))





            }
            //}






        holder.setListener(object : IRecListener {
            override fun onItemClick(view: View, pos: Int) {
                EventBus.getDefault().post(TopicsInChapClicked(true, chapterlist[pos],chapterSlected))


            }

        })
    }

    override fun getItemCount(): Int {
        return chapterlist.size
    }
}