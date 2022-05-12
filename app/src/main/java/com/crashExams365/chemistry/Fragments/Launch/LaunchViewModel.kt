package com.crashExams365.chemistry.Fragments.Launch

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LaunchViewModel(application: Application) : AndroidViewModel(application), IBannerLoadList,
    INewCatLoad {
    private var bannerListMutable:MutableLiveData<SliderModel>?=null
    private var messageError:MutableLiveData<String> = MutableLiveData()
    private val iBannerLoadList:IBannerLoadList

    private var catListMutable:MutableLiveData<MyNewCategoryModel>?=null
    private var catmessageError:MutableLiveData<String> = MutableLiveData()
  //  private val iCatLoad:ICatLoad
    private val iNewCatLoad:INewCatLoad





    init {
        iBannerLoadList=this
      //  iCatLoad=this
        iNewCatLoad=this
    }

    override fun onBannerLoadDoneListener(banners: SliderModel) {
        bannerListMutable!!.value=banners

    }

    private fun getConnectionType(context:Context): Boolean {
        var result = false // Returns connection type. 0: none; 1: mobile data; 2: wifi
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
                        result = true
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    } else if(type == ConnectivityManager.TYPE_VPN) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    override fun onBannerLoadFailed(messgae: String) {
        messageError.value=messgae

    }
    fun getBannerList():MutableLiveData<SliderModel>{
        if (bannerListMutable==null){
            bannerListMutable= MutableLiveData()
            loadBanner()

        }
        return bannerListMutable!!
    }

    fun getCatList():MutableLiveData<MyNewCategoryModel>{
        if (catListMutable==null){
            catListMutable= MutableLiveData()
            loadCategory()

        }
        return catListMutable!!
    }


    fun getMesssageError():MutableLiveData<String>{
        return messageError

    }
    fun getCatMesssageError():MutableLiveData<String>{
        return catmessageError

    }


    private fun loadBanner() {
        if (getConnectionType(getApplication())){


            val bannerlist = ArrayList<SliderModel>()
            val banner_ref = FirebaseDatabase.getInstance().getReference("Banners")




            banner_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (banners in snapshot.children) {
//                        val sliderModel = banners.getValue(SliderModel::class.java)
//                        bannerlist.add(sliderModel!!)
//                    }
                    val sliderModel:SliderModel= snapshot.getValue(SliderModel::class.java)!!
                    iBannerLoadList.onBannerLoadDoneListener(sliderModel)
                }

                override fun onCancelled(error: DatabaseError) {
                    iBannerLoadList.onBannerLoadFailed(error.message!!)
                }

            })


        }
        else{
            iBannerLoadList.onBannerLoadFailed("No Internet Habibi")
        }
    }

    private fun loadCategory() {

        if (getConnectionType(getApplication())){


            val categorylist = ArrayList<CatModel>()
            val cat_ref = FirebaseDatabase.getInstance().getReference("Cat")


            cat_ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (category in snapshot.children) {
//                        val catModel = category.getValue(CatModel::class.java)
//                        categorylist.add(catModel!!)
//                    }
                    val myNewCategoryModel:MyNewCategoryModel=snapshot.getValue(MyNewCategoryModel::class.java)!!
                    val catModel=CatModel()
                    catModel.id=14
                    catModel.name="Doubts"
                    //val list:ArrayList<CatModel> =it.Category!!
                  //  list.add(2,catModel)
                    myNewCategoryModel.Category!!.add(2,catModel)
                    iNewCatLoad.onsucess(myNewCategoryModel)
                }

                override fun onCancelled(error: DatabaseError) {
                    iNewCatLoad.onCatLoadFailed(error.message)
                }

            })

        }
        else{
            iNewCatLoad.onCatLoadFailed("No Internet Habibi")

        }

    }

    override fun onsucess(myNewCategoryModel: MyNewCategoryModel) {
        catListMutable!!.value=myNewCategoryModel

    }

    override fun onCatLoadFailed(messgae: String) {
        catmessageError.value=messgae
    }

//    override fun onsucess(list: List<CatModel>) {
//        catListMutable!!.value=list
//
//
//    }
//
//    override fun onCatLoadFailed(messgae: String) {
//        catmessageError.value=messgae
//    }
}