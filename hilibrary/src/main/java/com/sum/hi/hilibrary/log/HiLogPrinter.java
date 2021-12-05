package com.sum.hi.hilibrary.log;

import androidx.annotation.NonNull;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 17:36
 * @类描述 ${TODO}
 */
public interface HiLogPrinter {
    void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString);

}
