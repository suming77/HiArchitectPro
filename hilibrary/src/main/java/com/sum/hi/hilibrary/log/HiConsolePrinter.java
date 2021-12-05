package com.sum.hi.hilibrary.log;

import android.util.Log;

import androidx.annotation.NonNull;

import static com.sum.hi.hilibrary.log.HiLogConfig.MAX_LEN;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 21:07
 * @类描述 ${TODO}控制台打印器
 */
public class HiConsolePrinter implements HiLogPrinter {

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {
        int len = printString.length();
        int countOfSub = len / MAX_LEN;//行数
        if (countOfSub > 0) {//打印整行
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
                Log.println(level, tag, printString.substring(index, index + MAX_LEN));
                index += MAX_LEN;
            }

            if (index != len) {//打印最后一行
                Log.println(level, tag, printString.substring(index, len));
            }
        }else{
            Log.println(level, tag, printString);
        }



    }
}
