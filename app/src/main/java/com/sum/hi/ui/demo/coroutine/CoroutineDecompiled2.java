package com.sum.hi.ui.demo.coroutine;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlinx.coroutines.DelayKt;

/**
 * @Author:   smy
 * @Date:     2022/3/10 11:17
 * @Desc: 协程挂起恢复原理逆向刨析
 *     private suspend fun request2(): String {
 *          delay(2000)
 *           return "result from request()"
 *        }
 *
 *      private suspend fun request1(): String {
 *          val result2 = request2()
 *          return "result from request1 + $result2"
 *      }
 */

public class CoroutineDecompiled2{
    private static String TAG = "CoroutineDecompiled2";

    //挂起的流程
    public static final Object request1(Continuation preCallback){

        ContinuationImpl request1Callback;
        //是不是ContinuationImpl的实例，并且与Integer.MIN_VALUE做过或运算
     if (!(preCallback instanceof ContinuationImpl) || (((ContinuationImpl)preCallback).label & Integer.MIN_VALUE) == 0){
         request1Callback = new ContinuationImpl(preCallback){

             @Override
             Object invokeSuspend(@NotNull Object resumeResult) {
                 Log.e(TAG,"request1 has resumed");
                 this.result = resumeResult;
                 this.label |= Integer.MIN_VALUE;//在恢复的时候，进行或运算，代表request2已经对Continuation进行包装了，下次恢复时就不需要包装了
                 return request1(this);
             }
         };
     }else {
         request1Callback = (ContinuationImpl) preCallback;
     }
        switch (request1Callback.label){
            case 0:
                Object request2 = request2(request1Callback);
//                Object delay = DelayKt.delay(2000, request1Callback);
                if (request2 == IntrinsicsKt.getCOROUTINE_SUSPENDED()){
                    Log.e(TAG,"request1 has suspended");
                    return IntrinsicsKt.getCOROUTINE_SUSPENDED();
                }
        }
        Log.e(TAG,"request1 completed");
        return "result from request1"+request1Callback.result;//返回值
    }

     //挂起的流程
    public static final Object request2(Continuation preCallback){

        ContinuationImpl request2Callback;
        //是不是ContinuationImpl的实例，并且与Integer.MIN_VALUE做过或运算
     if (!(preCallback instanceof ContinuationImpl) || (((ContinuationImpl)preCallback).label & Integer.MIN_VALUE) == 0){
         request2Callback = new ContinuationImpl(preCallback){

             @Override
             Object invokeSuspend(@NotNull Object resumeResult) {
                 Log.e(TAG,"request2 has resumed");
                 this.result = resumeResult;
                 this.label |= Integer.MIN_VALUE;//在恢复的时候，进行或运算，代表request2已经对Continuation进行包装了，下次恢复时就不需要包装了
                 return request2(this);
             }
         };
     }else {
         request2Callback = (ContinuationImpl) preCallback;
     }

     switch (request2Callback.label){
         case 0:
             Object delay = DelayKt.delay(2000, request2Callback);
             if (delay == IntrinsicsKt.getCOROUTINE_SUSPENDED()){
                 Log.e(TAG,"request2 has suspended");
                 return IntrinsicsKt.getCOROUTINE_SUSPENDED();
             }
     }
        Log.e(TAG,"request2 completed");
     return "result from request2";//返回值
    }

    static abstract class ContinuationImpl implements Continuation{
        Object result;
        int label;
        private Continuation mPreCallback;

        public ContinuationImpl(Continuation preCallback) {
            this.mPreCallback = preCallback;
        }

        @NotNull
        @Override
        public CoroutineContext getContext() {
            return mPreCallback.getContext();
        }

        /**
         * 当delay方法执行完成之后，就会通过传递给它的Continuation的callback方法回调resumeWith()里面
         * @param resumeResult
         */
        @Override
        public void resumeWith(@NotNull Object resumeResult) {
            //调用invokeSuspend()方法让它的子类定向恢复某一个方法
            Object suspend = invokeSuspend(resumeResult);

            //虽然被恢复了，单接着又被挂起了
            if (suspend == IntrinsicsKt.getCOROUTINE_SUSPENDED()){
                return;
            }

            //否则经过本次恢复已经执行完，调用mPreCallback的resumeWith方法， 这就完成了恢复的流程
            mPreCallback.resumeWith(suspend);
        }

       abstract Object invokeSuspend(@NotNull Object resumeResult);
    }
}