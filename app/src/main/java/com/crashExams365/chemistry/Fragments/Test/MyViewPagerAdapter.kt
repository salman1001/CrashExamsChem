package com.crashExams365.chemistry.Fragments.Test

import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.crashExams365.chemistry.R
import com.crashExams365.chemistry.common.IRecListener
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener
import org.greenrobot.eventbus.EventBus
import java.io.File


class MyViewPagerAdapter( var context: Context,  var pdfFile: File,var resources:Resources,var pages:Int ) :
    RecyclerView.Adapter<MyViewPagerAdapter.MyViewHolder>() {

    fun deleveryting(){
        if (renderer!= null){
            try {
                renderer.close()
            }catch (e:Exception){

            }


        }
    }

    val renderer = PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
    var bitmapro: Bitmap?=null
    var optcli:String?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_viewpager_item, parent, false))
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        try {
            val page = renderer.openPage(position+pages)
            val width = resources.displayMetrics.densityDpi /150 * page.width
            val height = resources.displayMetrics.densityDpi/150  * page.height
            bitmapro = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            page.render(bitmapro!!, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            holder.que_image!!.setImageBitmap(bitmapro!!)
            Log.d(TAG, "onBindViewHolder: "+bitmapro!!.byteCount)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }


        holder.setListener(object : IRecListener {
            override fun onItemClick(view: View, pos: Int) {
                EventBus.getDefault().post(MoveClikedEvent(view,true,pos))

            }

        })

        holder.togglebuttonGroup!!.addOnButtonCheckedListener(OnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {

                if (checkedId ==R.id.opt_A) {

                    holder.butA!!.setBackgroundColor(ContextCompat.getColor(context,  R.color.lightblue))
                    optcli="A"
                }
                if (checkedId ==R.id.opt_B) {
                    holder.butB!!.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue))
                    optcli="B"

                }
                if (checkedId ==R.id.opt_C) {
                    holder.butC!!.setBackgroundColor(ContextCompat.getColor(context, R.color.lightblue))
                    optcli="C"

                }
                if (checkedId ==R.id.opt_D) {
                    holder.butD!!.setBackgroundColor(ContextCompat.getColor(context,  R.color.lightblue))
                    optcli="D"

                }
                  EventBus.getDefault().post(OptionClickedPager(true,optcli!!,position))


            }
            else{
                EventBus.getDefault().post(OptionClickedPager(false,optcli!!,position))
                holder.butA!!.setBackgroundColor(Color.WHITE)
                holder.butB!!.setBackgroundColor(Color.WHITE)
                holder.butC!!.setBackgroundColor(Color.WHITE)
                holder.butD!!.setBackgroundColor(Color.WHITE)
            }

        })
    }


    override fun getItemCount(): Int {
        return pages
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var que_image:ImageView?=null
        var togglebuttonGroup:MaterialButtonToggleGroup?=null
        var butA:MaterialButton?=null
        var butB:MaterialButton?=null
        var butC:MaterialButton?=null
        var butD:MaterialButton?=null
        var pre:Button?=null
        var rew:Button?=null
        var nex:Button?=null
        fun setListener(listener: IRecListener) {
            this.listener = listener
        }


        private var listener: IRecListener?=null

        init {
            togglebuttonGroup=itemView.findViewById(R.id.toggleButton)
            que_image=itemView.findViewById(R.id.photo_question) as PhotoView
            butA=itemView.findViewById(R.id.opt_A)
            butB=itemView.findViewById(R.id.opt_B)
            butC=itemView.findViewById(R.id.opt_C)
            butD=itemView.findViewById(R.id.opt_D)
            pre=itemView.findViewById(R.id.prev)
            rew=itemView.findViewById(R.id.review)
            nex=itemView.findViewById(R.id.next)


            pre!!.setOnClickListener(this)
            rew!!.setOnClickListener(this)
            nex!!.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            listener!!.onItemClick(v!!, adapterPosition)

        }


    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



}