package com.sum.hi.hilibrary.log;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 18:39
 * @类描述 ${TODO}
 */
public interface HiLogFormatter<T> {
    String format(T data);
}
