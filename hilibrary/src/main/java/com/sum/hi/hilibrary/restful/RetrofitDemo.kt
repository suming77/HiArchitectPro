package com.sum.hi.hilibrary.restful

import android.service.autofill.UserData
import com.sum.hi.hilibrary.User
import com.sum.hi.hilibrary.annotation.HiCall
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import rx.Observable

/**
 * @Author:   smy
 * @Date:     2022/3/26 20:35
 * @Desc:
 */
class RetrofitDemo {

    /**
     * Retrofit最初的样子
     * 如果最初仅仅指定了一个baseURL，直接方法必须如下面24-28行，方法的返回值必须是Call<ResponseBody>类型的，
     * 那为什么我们写的时候ResponseBody可以写成任意的java bean对象，Call类型我们可以写成RXjava的Observable?
     * 那是因为你在使用的时候自定了Response转换器，Call对象的转换器，才可以写成各种形式，但是最初的样子就是下面这种
     */
    val retrofit1 = Retrofit.Builder()
        .baseUrl("https://api.devio.org/as/")
        .build()

    interface Api {
        //默认情况下返回类型为Call<T>, response类型为ResponseBody
        @GET("/user/login")
        fun login(): Call<ResponseBody>

        //添加GsonConverterFactory，response类型可以写成User
        @GET("/user/login")
        fun login2(): Call<User>

        //添加RxJavaCallAdapterFactory，方法返回类型可以写成Observable
        @GET("/user/login")
        fun login3(): Observable<UserData>

        //Retrofit+coroutine
        @GET("/user/login")
        suspend fun login4(): Response<User>

        //Retrofit+coroutine
        @GET("/user/login")
        suspend fun login5(): User
    }


    /**
     * Retrofit扩展玩法
     * 必须使用build创建，建造者模式，能够暴露给使用者的方法清晰暴露在Builder类中，不该暴露的你就用不了，
     * 能够快速完成复制，比如如果项目中需要两个Retrofit对象，但是第二个对象仅仅需要改版其他的某个参数而已，
     * 而我们不希望再次把所有的参数传递一遍，而是希望继承第一对象的部分数据，这样就可以通过build继承第一个对象的数据
     * 比如AlterDialog.
     *
     * baseUrl:认为大多数APP都是单一语言，所以搞了个BaseURL,在定义的时候传递相对路径就可以了，
     * 但是在国内环境中一个app有多个域名是非常常见的，retrofit默认不支持在方法上面标记URL,只能通过传参来改变接口的全域名
     *
     * 返回值类型是Call,里面的泛型类型是ResponseBody,如果说想把Call对象写成Obaservable,并且把response类型写成User
     * 那么办呢？
     *
     * 首先是callFactory(OkHttpClient())，我们定义的login方法都有一个返回值Call<Response>对象，
     * 它仅仅是一个接口，它具体的实现类可以是OkHttpCall,也可以是TCPCall,也可以是HiCall对象，这些都交给
     * callFactory来创建的，所以callFactory顾名思义就是来创建Call对象的。
     *
     * addConverterFactory：转换工厂，实际上是ResponseBody数据的转换工厂，如果我们需要把ResponseBody转换成User,
     * 但是Retrofit是不知道怎么转换的，以那种序列化的方式转换，所以addConverterFactory就是将ResponseBody
     * 装换成对应的类型。如果不指定addConverterFactory(GsonConverterFactory.create())那么默认类型就是
     * ResponseBody，而你如果指定User就会抛出异常。addConverterFactory也可以添加多个，可以共存。
     *
     * addCallAdapterFactory：Call对象转换工厂。通常情况下返回值是一个Call对象，那么可以写成Observable或者HiCalll类型呢？
     * 因为login()这个方法解析完成后由callFactory()来创建对象，创建的是啥，就是Call对象。
     * 所以你写成Observable或者HiCalll就会报错。需要addCallAdapterFactory转换工厂来转换Call对象，
     * RxJavaCallAdapterFactory就可以把返回类型写成Observable，HiCallAdapterFactory返回类型写成HiCall.
     * 里面实际上就是一层包裹，addCallAdapterFactory可以添加多个，可以共存。根据返回的类型从已经添加的CallAdapterFactory
     * 里面去查找，支持这种类型转换的CallAdapterFactory。
     *
     * 在Retrofit2.8的时候还适配了协程的写法，允许在方法的前面加上suspend关键字。返回值就没必要写成Call了，
     * 可以直接写成User,再也不用调用enqueue和excute拿返回值。返回值也可以写成一个Result加泛型类型。
     *
     * 最后通过build（）方法构造一个retrofit。
     */

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.devio.org/as/")
        .callFactory(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//        .addCallAdapterFactory(HiCallAdapterFactory.create())
        .build()

    /**
     * 如果指定了RxJavaCallAdapterFactory为CallAdapterFactory，那么它就会产生一个RxJavaCallAdapter，
     * 继承子Retrofit的CallAdapter，并且复写adapt方法，方法解析完成由CallFactory解析出Call对象之后并不是立马返回的，
     * 也不是立马结束的，而是调用RxJavaCallAdapterFactory产生的RxJavaCallAdapter对象的adapt方法，
     * 那么就可以对传递进来的Call对象进行转换了，可以看到就是把Call对象包裹成CallExecuteObservable，返回类型就是observable
     *
     * 为什么要做这层转换呢，根据Retrofit默认的实现呢，我们定义一个方法，我们只能使用它的enqueue或者excute方法
     * 但是经过这层adapter转换之后，我们就可以把方法的返回值改成Observable，这样我们就可以愉快使用RxJava的能力了，
     */
//    class RxJavaCallAdapter<R> : CallAdapter<R, Observable> {
//        override fun adapt(call: Call<R>): Observable {
//            //经过适配转换，就可以利用RXjava链式调用能力
//            val observable = CallExecuteObservable<>(call)
//            return observable
//        }
//    }

    /**
     * HiCallAdapterFactory
     * 假如我么在初始化CallAdapterFactory的时候指定的是HiCallAdapterFactory，在运行的时候就会产生一个HiCallAdapter对象
     * 方法解析完成之后，由CallFactory创建出一个Call对象，此时也会调用HiCallAdapter的adapt方法，进行包装进行转换
     * 这样可以使用它的enqueue或者excute之外，也可以实现网络请求结束自动切换到主线程的能力
     */
//    class HiCallAdapter<R> : CallAdapter<R, HiCall> {
//        override fun adapt(call: Call<R>): HiCall {
//            //经过适配转换，就能实现网络请求结束自动切换到主线程
//            val hiCall = HiCall<>(call)
//            return hiCall
//        }
//    }

    /**
     * HiCall
     */
//    class HiCall : Call<T> {
//        private var delegate: Call<T>
//
//        fun HiCall(delegate: Call<T>) {
//            this.delegate = delegate
//        }
//
//        fun enqueue(callBack: Callback<T>) {
//            delegate.enqueue { reponse ->
//                mainHandler.post {
//                    callBack.onSuccess(reponse)
//                }
//            }
//        }
//    }


}