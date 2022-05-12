package com.crashExams365.chemistry.Fragments.Chapters



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
import com.makeramen.roundedimageview.RoundedImageView
import org.greenrobot.eventbus.EventBus


class MyChapterAdapter(var context: Context, var chapterlist:List<ChapterModel>):
    RecyclerView.Adapter<MyChapterAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var chaptername: TextView? = null
        var chapterimage:ImageView?=null

        private var listener: IRecListener? = null
        fun setListener(listener: IRecListener) {
            this.listener = listener
        }

        init {
            chaptername = itemView.findViewById(R.id.chapter_name) as TextView
            chapterimage=itemView.findViewById(R.id.imag_chapter) as RoundedImageView
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
        when(chapterlist[position].id){
            1 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.unit))
            2 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.car))
            3 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.plane))
            4 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.newton))
            5 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.work))
            6 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.rotation))
            7 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.gravity))
            8 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.solid))
            9 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.liquid))
            10 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.thermal11111111111))
            11->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.thermometers))
            12 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.kinetic))
            13 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.oscii111))
            14 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.dopller))
            15 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.elctro))
            16 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.capacitor))
            17 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.current))
            18 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.magticeffect))
            19 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.magpro))
            20 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.induction))
            21 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.accurrct))
            22 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.emwa))
            23 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.rayop))
            24 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.waveoop))
            25 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.electronic))
            26 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.atimic))
            27 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.nuclear))
            28 ->  holder.chapterimage!!.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.semi))




        }
        holder.chaptername!!.text = chapterlist[position].name

        holder.setListener(object : IRecListener {
            override fun onItemClick(view: View, pos: Int) {
                EventBus.getDefault().post(
                    ChapterCliked(
                        true,
                        chapterlist[pos]
                    )
                )

            }

        })
    }

    override fun getItemCount(): Int {
        return chapterlist.size
    }

}