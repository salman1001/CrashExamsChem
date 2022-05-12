package com.crashExams365.chemistry.Fragments.ShowAnswers

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.Fragments.ResultFragment.ObjectShowAns
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.common.IRecListener
import com.github.chrisbanes.photoview.PhotoView
import org.greenrobot.eventbus.EventBus
import java.io.File

class ShowAnsAdap(var context: Context, var pdfFile: File, var resources: Resources,var objectshow:ObjectShowAns) :
    RecyclerView.Adapter<ShowAnsAdap.MyViewHolder>() {
    fun deleveryting(){
        renderer.close()
    }



    val renderer =
        PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
    var bitmapQ: Bitmap?=null
    var bitmapA: Bitmap?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_viewpager_for_fullans, parent, false))
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.que_image!!.setImageBitmap(arrayList[position])
        var op:String?=null
        var opmark:String?=null

        op=objectshow.hashmapForAns[position.toString()]
        opmark=objectshow.hashmapForMarked[position.toString()]

        when (op) {
            "A" -> holder.text_a!!.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            "B" -> holder.text_b!!.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            "C" -> holder.text_c!!.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            "D" -> holder.text_d!!.setBackgroundColor(ContextCompat.getColor(context, R.color.green))

        }
        if (!op.contentEquals(opmark)){
            when (opmark) {
                "A" -> holder.text_a!!.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.red
                ))
                "B" -> holder.text_b!!.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.red
                ))
                "C" -> holder.text_c!!.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.red
                ))
                "D" -> holder.text_d!!.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.red
                ))

            }

        }
        holder.setListener(object : IRecListener {
            override fun onItemClick(view: View, pos: Int) {
                val page1 = renderer.openPage(pos)
                val width1 = resources.displayMetrics.densityDpi / 150 * page1.width
               val heigh1t = resources.displayMetrics.densityDpi / 150 * page1.height

                bitmapA = Bitmap.createBitmap(width1, heigh1t, Bitmap.Config.ARGB_8888)

               page1.render(bitmapA!!, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page1.close()
                Log.d(ContentValues.TAG, "onBindViewHolder: "+bitmapA!!.byteCount)

                EventBus.getDefault().post(ShowAnsButtonPopUp(true,pos, bitmapA!!))

            }

        })


        try {



            val page = renderer.openPage(position+objectshow.hashmapForAns.size)
            val width = resources.displayMetrics.densityDpi / 150 * page.width
            val height = resources.displayMetrics.densityDpi / 150 * page.height

            bitmapQ = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            page.render(bitmapQ!!, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            holder.que_image!!.setImageBitmap(bitmapQ)
            page.close()
            Log.d(ContentValues.TAG, "onBindViewHolder: "+bitmapQ!!.byteCount)

//            val page1 = renderer.openPage(position)
//            val width1 = resources.displayMetrics.densityDpi / 300 * page1.width
//            val heigh1t = resources.displayMetrics.densityDpi / 300 * page1.height
//
//            bitmapA = Bitmap.createBitmap(width1, heigh1t, Bitmap.Config.ARGB_8888)
//
//            page1.render(bitmapA!!, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//
//            holder.que_ans!!.setImageBitmap(bitmapA)
//            page1.close()


            //  }

            // close the renderer

        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return objectshow.hashmapForAns.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var que_image: ImageView?=null
      //  var que_ans: ImageView?=null
        var button:Button?=null
        var text_a:TextView?=null
        var text_b:TextView?=null
        var text_c:TextView?=null
        var text_d:TextView?=null
        fun setListener(listener: IRecListener) {
            this.listener = listener
        }


        private var listener: IRecListener?=null


        init {
            button=itemView.findViewById(R.id.showclicked)
            que_image=itemView.findViewById(R.id.photo_question) as PhotoView
         //   que_ans=itemView.findViewById(R.id.photo_ans) as PhotoView
            text_a=itemView.findViewById(R.id.fullA)
            text_b=itemView.findViewById(R.id.fullB)
            text_c=itemView.findViewById(R.id.fullC)
            text_d=itemView.findViewById(R.id.fullD)
            button!!.setOnClickListener(this)



        }

        override fun onClick(v: View?) {
            listener!!.onItemClick(v!!, adapterPosition)

        }
    }




}