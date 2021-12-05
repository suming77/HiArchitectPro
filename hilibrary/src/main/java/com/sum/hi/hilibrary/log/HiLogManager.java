package com.sum.hi.hilibrary.log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/07 16:10
 * @类描述 ${TODO}HiLog管理类
 */
public class HiLogManager {
    private HiLogConfig config;
    private static HiLogManager instance;

    //保存打印器
    private List<HiLogPrinter> printers = new ArrayList<>();


    private HiLogManager(HiLogConfig config, HiLogPrinter[] printers) {
        this.config = config;
        this.printers.addAll(Arrays.asList(printers));
    }

    public static HiLogManager getInstance() {
        return instance;
    }

    public static void init(@NonNull HiLogConfig config, HiLogPrinter... logPrinters) {
        instance = new HiLogManager(config, logPrinters);
    }

    public HiLogConfig getConfig() {
        return config;
    }

    public List<HiLogPrinter> getPrinters() {
        return printers;
    }

    public void addPrinter(HiLogPrinter printer) {
        printers.add(printer);
    }

    public void removePrinter(HiLogPrinter printer) {
        if (printers != null) {
            printers.remove(printer);
        }
    }
}
