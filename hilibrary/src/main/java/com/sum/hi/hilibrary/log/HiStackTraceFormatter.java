package com.sum.hi.hilibrary.log;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 18:46
 * @类描述 ${TODO}堆栈打印转换器
 */
public class HiStackTraceFormatter implements HiLogFormatter<StackTraceElement[]> {

    @Override
    public String format(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder(128);
        if (stackTrace == null || stackTrace.length == 0) {
            return null;
        } else if (stackTrace.length == 1) {
            return "\t-" + stackTrace[0].toString();// \t会有8个空格缩进
        } else {
            for (int i = 0, len = stackTrace.length; i < len; i++) {
                if (i == 0) {
                    sb.append("stackTrance: \n");
                }

                if (i != len - 1) {
                    sb.append("\t├");
                    sb.append(stackTrace[i].toString());
                    sb.append("\n");
                } else {
                    sb.append("\t┴");
                    sb.append(stackTrace[i].toString());

                }
            }
            return sb.toString();
        }
    }
}
