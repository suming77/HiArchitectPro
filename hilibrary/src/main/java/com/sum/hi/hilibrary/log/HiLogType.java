package com.sum.hi.hilibrary.log;

import android.util.Log;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 15:50
 * @类描述 ${TODO}打印Log类型
 */
public class HiLogType {
    @IntDef({V,D,I,E,A,W})//类型
    @Retention(RetentionPolicy.SOURCE)//源码级别
    public @interface type{

    }

    public final static int V = Log.VERBOSE;
    public final static int D = Log.DEBUG;
    public final static int I = Log.INFO;
    public final static int E = Log.ERROR;
    public final static int A = Log.ASSERT;
    public final static int W = Log.WARN;
}
