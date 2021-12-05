package com.sum.hi.hilibrary.log;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 18:44
 * @类描述 ${TODO}线程转换器
 */
public class HiThreadFormatter implements HiLogFormatter<Thread> {
    @Override
    public String format(Thread data) {
        return "Thread : " + data.getName();
    }


}
