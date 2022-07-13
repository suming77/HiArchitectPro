package com.sum.hi.ui.viewmodel

import android.text.TextUtils
import androidx.lifecycle.*
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.ui.BuildConfig
import com.sum.hi.ui.http.ApiFactory
import com.sum.hi.ui.http.api.GoodsDetailApi
import com.sum.hi.ui.model.DetailModel
import java.lang.Exception

/**
 * @author smy
 * @date   2022/7/12 17:39
 * @desc
 */
class GoodsDetailViewModel(val goodsId: String?) : ViewModel() {
    companion object {
        //ViewModelProvider.NewInstanceFactory实际就是拿到class对象modelClass.newInstance()从而构造出一个实例对象
        private class DetailViewModelFactory(val goodsId: String?) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val constructor = modelClass.getConstructor(String::class.java)
                //如果没有goodsId参数就报异常了
                try {
                    constructor?.let {
                        return it.newInstance(goodsId)
                    }
                } catch (e: Exception) {
                    //ignore
                }
                return super.create(modelClass)
            }
        }

        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): GoodsDetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(
                GoodsDetailViewModel::class.java
            )
        }
    }

    fun queryGoodsDetail(): LiveData<DetailModel?> {
        val pageData = MutableLiveData<DetailModel>()
        if (!TextUtils.isEmpty(goodsId)) {

            ApiFactory.create(GoodsDetailApi::class.java).queryGoodsDetail(goodsId!!)
                .enqueue(object : HiCallback<DetailModel> {
                    override fun onSuccess(response: HiResponse<DetailModel>) {
                        if (response.successful && response.data!=null){
                            pageData.postValue(response.data)
                        }else{
                            pageData.postValue(null)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        pageData.postValue(null)
                        if (BuildConfig.DEBUG){
                            throwable.printStackTrace()
                        }
                    }
                })
        }
        return pageData
    }

}