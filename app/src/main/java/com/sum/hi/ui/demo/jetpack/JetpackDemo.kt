package com.sum.hi.ui.demo.jetpack

import androidx.annotation.NonNull
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList

/**
 * @Author:   smy
 * @Date:     2022/3/30 13:55
 * @Desc:
 */
class JetpackDemo {
    fun navigation() {
//        val navController: NavController
//        navController.navigate(int resId, Bundle args, NavOptions navOptions)
//
//        navController.handleDeepLink(Intent())
//        navController.popBackStack(int destinationId, boolean inclusive)
    }

    /**
     * 实现了LifecycleOwner接口，这就说明了它是生命周期的宿主，LifecycleRegistry是一个keyvalue的map集合，
     * 用来存储我们注册进去的一个个addObserver(mPresenter)，会在每个Fragment生命周期方法里面遍历它里面
     * 存储的Observeable观察者，从而把数组的状态分发给他们。如果我们向Fragmegnt注册一个观察者，
     * 任意一个类只要实现LifecycleObserver接口就可以了，在类里面可以定义不同的方法，方法上面使用注解标记
     * 当宿主生命周期发生变化的时候，就会传到相应的方法上面去。这种方式就巧妙的解耦合对宿主生命周期的感知。
     *
     */
    public class Fragment : LifecycleOwner {
        val mLifecycleRegistry = LifecycleRegistry(this)
        override fun getLifecycle(): Lifecycle {
            return mLifecycleRegistry
        }
    }

    class MyPresenter : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate(@NonNull lifecycleOwner: LifecycleOwner) {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy(@NonNull lifecycleOwner: LifecycleOwner) {
        }
    }

    //最后调用addObserver()注册进去
//    lifecycle.addObserver(mPresenter)//注册观察者，观察宿主生命周期状态变化


    /**
     * 1.创建ViewModel类
     */
//    class MyViewModel : ViewModel() {
//        override fun onCleared() {
//            super.onCleared()
//            //宿主销毁时，回执行到这里，可自我清理释放资源
//        }
//
//        //使用LiveData分发给观察者， LiveData他会自动判断数组是否存活，如果不存活LiveData就不会向他分发数据了，
//        val mutableLiveData = MutableLiveData<User>()
//        fun getUserInfo(owner: LifecycleOwner, observer: Observer<User>) {
//            mutableLiveData.observe(owner, observer)
//            //从数据库查询用户信息
//            //只要viewmodel不被销毁，mutableLiveData中的数据就不会被清理掉
//            mutableLiveData.value = user
//        }

    /**
     * 2.构建Viewmodel实例对象，需要使用 ViewModelProvider，来获取viewmodel
     * 如果ViewModelStore中已经存在则返回公用实例，如果不存在，则创建并缓存
     * 不同Fragment中获取同一个ViewModel实例，从而实现数据共享
     *
     * ViewModelProvider其实啥事也没做，viewModelStore本质上是一个hashMap用来存储一个个viewmodel实例
     * 注意：Activity和Fragment中都存在一个viewModelStore，如果我们要实现Activity和Fragment数据共享，
     * 在fragment中获取ViewModel时候要获取activity!!.viewModelStore的
     *
     * 如果获取Viewmodel实例的时候没有数据，则ViewModelProvider.NewInstanceFactory()构建一个
     * viewmodel实例并且缓存到集合当中
     */
//        class FragmentA : androidx.fragment.app.Fragment() {
//            override fun onCreate(savedInstanceState: Bundle?) {
//                super.onCreate(savedInstanceState)
//                val myViewModel = ViewModelProvider(//viewmodel提供者
//                    activity!!.viewModelStore,
//                    ViewModelProvider.NewInstanceFactory()
//                ).get(MyViewModel::class.java)
//            }
//        }
//    }

//    val mutableLiveData  = MutableLiveData();
//    //注册一个跟宿主生命周期绑定的观察者，宿主销毁了，会自动解除注册
//    observe(LifecycleOwner owner, Observer<? super T> observer)
//
//    //观察不到宿主生命周期，不会自动解除注册
//    observeForever(Observer<? super T> observer)
//
//    //下面方法都是分发数据给所有的观察者
//    //只能用在主线程
//    setValue(T value)
//    //子线程，主线程都可以用
//    postValue(T value)


//    /**
//     * 1.创建一个操作数据库CRUD数据的实体层
//     */
//    @Dao//标记dao注解
//    public interface UserDao {
//        @Query("SELECT * FROM user")
//        fun getALL(): List<User>
//
//        @Update("update table user set desc=:desc and where user_id =:userId")
//        fun updateUser(desc: String, userId: String)
//
//        @Insert
//        fun insertUser(vararg user: User)
//
//        @Delete
//        fun delete(user: User)
//    }
//
//    /**
//     * 2.创建数据库
//     */
//    @Database(entities = { User::class }, version = 1)
//    public abstract class HiDatabase : RoomDatabase() {
//        public val hiDatabase: HiDatabase? = null
//
//        companion object {
//            init {
//                //数据库实例创建
//                hiDatabase = Room.databaseBuilder(
//                    getApplicationContext(),
//                    AppDataBase::clss,
//                    "database_name"
//                ).build
//            }
//        }
//
//        public abstract fun userDao(): UserDao
//    }
//
//    //3.通过数据库单例对象，获取userDao数据操作对象，访问数据库
//    hiAppDb.userDao.getAll()


//    //1.构建任务
//    class UploadFileWork : Worker() {
//        override fun doWork(): Result {
//            return Result.success()
//        }
//    }
//
//    //2.构建执行任务的request对象, 一次性任务
//    val request = OneTimeWorkRequest.Builder(UploadFileWork::class.java).build()
//
//    //3.把任务加入调度队列
//    val workContinuation = WorkManager.getInstance(context).beginWith(request)
//    workContinuation.then(workB).then(workC).enqueue()//如果workB，workC与request相关联，使用then联起来，就可以在任意地方查看执行任务的结果



//    //1.构建PagedList.Config对象，用以声明以何种方式分页
//    PagedList.Config config = new PagedList.Config.Builder()
//    .setPageSize(10)//指定每次分页加载的数量
//    .setInitialLoadSizeHint(12)//指定初始化数据加载数量
//    .build();
//
//    //创建数据源工厂类，用来创建数据提供者 DataSource触发数据初始化，刷新数据
//    DataSource.Factory factory = new DataSource.Factory() {
//        @NonNull
//        @Override
//        public DataSource create() {
//            return new ItemKeyedDataSource();
//        }
//    };
//
//    //3.构建一个能够触发加载分页初始化数据的LiveData对象，并且把上面的DataSource.Factory和PagedList.Config
//    LiveData<PagedList<T>> pageData = new LivePagedListBuilder(factory, config)
//    .build();//最后通过build方法，构建出LiveData对象，请注意，它的泛型是PagedList<T>
//
//    //4.最后我们只需要拿到前面构建出来的LiveData对象注册一个Observer观察者，这样就可以触发页面初始化数据
//    mViewMidel.getPageData().Observer(this, pageList -> submitList(pageList));
//
//
//    class MyItemKeyedDataSource extends ItemKeyedDataSource<User>{
//
//        @Override
//        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback callback) {
//            //页面初始化数据加载
//            callback.onResult(list);
//        }
//
//        @Override
//        public void loadAfter(@NonNull LoadParams params, @NonNull LoadCallback callback) {
//            //向后分页数据加载
//            callback.onResult(list);
//        }
//
//        @Override
//        public void loadBefore(@NonNull LoadParams params, @NonNull LoadCallback callback) {
//            //向前分页数据加载
//            callback.onResult(list);
//        }
//
//        @NonNull
//        @Override
//        public User getKey(@NonNull Object item) {
//            return null;
//        }
//    }
}