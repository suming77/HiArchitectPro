package com.sum.hi.hilibrary.log;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/14 18:20
 * @类描述 ${TODO}堆栈信息工具类
 */
public class HiStackTraceUtil {

    public static StackTraceElement[] getCroppedRealStackTrace(StackTraceElement[] elements, String ignorePackage, int maxDepth) {
        return cropStackTrace(getRealStackTrace(elements, ignorePackage), maxDepth);
    }


    /**
     * 获取除了包名以外的堆栈信息
     *
     * @param elements      堆栈信息
     * @param ignorePackage 忽略的包名
     * @return
     */
    public static StackTraceElement[] getRealStackTrace(StackTraceElement[] elements, String ignorePackage) {
        int ignoreDepth = 0;
        int allDepth = elements.length;
        String className;
        for (int i = allDepth - 1; i >= 0; i--) {
            className = elements[i].getClassName();
            if (ignorePackage != null && className.startsWith(ignorePackage)) {
                ignoreDepth += 1;
                break;
            }
        }
        int realDepth = allDepth - ignoreDepth;
        StackTraceElement[] realStacks = new StackTraceElement[realDepth];
        System.arraycopy(elements, ignoreDepth, realStacks, 0, realDepth);
        return realStacks;
    }

    /**
     * 裁剪堆栈信息
     *
     * @param callStack 堆栈信息
     * @param maxDepth  最大深度
     * @return
     */
    public static StackTraceElement[] cropStackTrace(StackTraceElement[] callStack, int maxDepth) {
        int realDepth = callStack.length;//堆栈信息长度
        if (maxDepth > 0) {//取最小值
            realDepth = Math.min(realDepth, maxDepth);
        }
        StackTraceElement[] traceElements = new StackTraceElement[realDepth];
        System.arraycopy(callStack, 0, traceElements, 0, realDepth);//copy出来
        return traceElements;
    }

}
