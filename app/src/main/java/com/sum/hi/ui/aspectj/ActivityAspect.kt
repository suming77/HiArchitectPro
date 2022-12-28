package com.sum.hi.ui.aspectj

import android.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

/**
 * @创建者 mingyan.su
 * @创建时间 2022/12/15 22:08
 * @类描述 ${TODO}
 * 加载setContentView的一个耗时，拦截Activity中的
 *
 * public void checkLogin(ProceedingJoinPoint joinPoint)，这里的一定要是public。如果是private就会报Cause: zip file is empty
 */
@Aspect
open class ActivityAspect {

    /**
     * before, after = JoinPoint
     */
    @Around("execution(* android.app.Activity.setContentView(..))")
    open fun setContentView(joinPoint: ProceedingJoinPoint) {
        adviceCode(joinPoint)
    }

    private fun adviceCode(joinPoint: ProceedingJoinPoint) {
        val signature = joinPoint.signature
        val className = signature.declaringType.simpleName
        val methodName = signature.name

        val time = System.currentTimeMillis()
        joinPoint.proceed()
        Log.e("ActivityAspect", "$className:$methodName cost=${System.currentTimeMillis() - time}")
    }

    //标记了@MethodTrace注解的都会被插入该方法
    @Around("execution(@com.sum.hi.ui.aspectj.MethodTrace * *(..))")
   open fun methodTrace(joinPoint: ProceedingJoinPoint){
        adviceCode(joinPoint)
    }
}