package com.sum.hi.ui.demo.jetpack

import android.app.Application
import androidx.lifecycle.*

/**
 * @Author:   smy
 * @Date:     2022/4/2 23:50
 * @Desc:
 */
class ViewModelDemo {
    class HiViewModel : ViewModel() {
        val liveData = MutableLiveData<List<String>>()
        fun loadInitData(): LiveData<List<String>> {
            //form remote 接口请求
            //为了适配因配置变更而导致的页面重建，重复利用之前的数据，加快新页面渲染，不在请求接口
            //如果value不为空，说明这个ViewModel肯定是复用的，因为新建的ViewModel的liveData是不会有数据的
            if (liveData.value == null) {
//                val remoteData = fetchDataFromRemote()
//                liveData.postValue(remoteData)
            }
            return liveData
        }


    }

    //Fragment或者Activity当中
    //通过ViewModelProvider来获取ViewModel对象
//    val viewModel = ViewModelProvider(this).get(HiViewModel::class.java)
//    viewModel.loadInitData().observe(this, Observer {
//        //接收到数据
//    })

    class HiSaveViewModel(val savedState: SavedStateHandle) : ViewModel() {
        private val KEY_SAVE_STATE = "key_save_state"
        private val liveData = MutableLiveData<List<String>>()
        fun loadInitData(): LiveData<List<String>> {
            //1.from memory
            if (liveData.value == null) {
                val memoryData = savedState.get<List<String>>(KEY_SAVE_STATE)
                liveData.postValue(memoryData)
                return liveData

                //2.from remote
//                val remoteData = fetchDataFromRemote()
//                savedState.set(KEY_SAVE_STATE, remoteData)
//                liveData.postValue(remoteData)
            }
            return liveData
        }
    }

    //第二种写法
    class HiAndroidViewModel(val appInstance: Application, val savedStateHandle: SavedStateHandle) :
        AndroidViewModel(appInstance) {

    }

    //让Application实现ViewModelStoreOwner接口
    class MyApp : Application(), ViewModelStoreOwner {
        private val appViewModelStore: ViewModelStore by lazy {
            ViewModelStore()
        }

        override fun getViewModelStore(): ViewModelStore {
            return appViewModelStore
        }
    }

//    val viewModel = ViewModelProvider(application).get(HiViewModel::class.java)


}