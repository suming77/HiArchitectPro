package com.sum.hi.hilibrary.log;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 16:07
 * @类描述 ${TODO}Log配置
 */
public abstract class HiLogConfig {
    static int MAX_LEN = 512;//没行最大字符数

    static HiStackTraceFormatter HI_STACK_TRACE_FORMATTER = new HiStackTraceFormatter();
    static HiThreadFormatter HI_THREAD_FORMATTER = new HiThreadFormatter();

    public JsonParser injectJsonParser() {
        return null;
    }

    public String getGlobalTag() {
        return "HiLog";
    }

    public boolean enable() {
        return true;
    }

    /**
     * 是否包含线程信息
     * @return
     */
    public boolean includeThread() {
        return false;
    }

    /**
     * 栈深度
     */
    public int stackTraceDepth() {
        return 5;
    }

    public HiLogPrinter prints(){
        return null;
    }

    public interface JsonParser {
        String toJson(Object src);
    }
}
