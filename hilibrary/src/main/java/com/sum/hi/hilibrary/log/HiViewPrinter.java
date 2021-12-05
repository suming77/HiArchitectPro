package com.sum.hi.hilibrary.log;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sum.hi.hilibrary.R;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.LongIterator;

/**
 * @创建者 mingyan.su
 * @创建时间 2021/11/14 20:16
 * @类描述 ${TODO}日志可视化
 */
public class HiViewPrinter implements HiLogPrinter {
    private RecyclerView mRecyclerView;
    private LogAdapter logAdapter;
    private HiViewPrinterProvider printerProvider;

    public HiViewPrinter(Activity activity) {
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        mRecyclerView = new RecyclerView(activity);
        logAdapter = new LogAdapter(LayoutInflater.from(mRecyclerView.getContext()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(logAdapter);

        printerProvider = new HiViewPrinterProvider(rootView, mRecyclerView);
    }

    /**
     * 获取ViewProvider,通过ViewProvider可以控制log的显示和隐藏
     *
     * @return printerProvider
     */
    public HiViewPrinterProvider getPrinterProvider() {
        return printerProvider;
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {
        //将log添加到recyclerView
        logAdapter.addItem(new HiLogMo(System.currentTimeMillis(), level, tag, printString));
        //滚动到相对应的位置
        mRecyclerView.smoothScrollToPosition(logAdapter.getItemCount()-1);
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {
        private LayoutInflater inflater;
        private List<HiLogMo> logs = new ArrayList();

        void addItem(HiLogMo item) {
            logs.add(item);
            notifyItemInserted(logs.size() - 1);
        }

        public LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.item_hilog, parent, false);
            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
            HiLogMo hiLogMo = logs.get(position);
            int logColor = getLogColor(hiLogMo.level);
            holder.tvTag.setTextColor(logColor);
            holder.tvMessage.setTextColor(logColor);

            holder.tvTag.setText(hiLogMo.getFlattened());
            holder.tvMessage.setText(hiLogMo.log);
        }

        /**
         * 根据log等级获取不同的颜色
         *
         * @param logLevel
         * @return 颜色值
         */
        private int getLogColor(int logLevel) {
            int color;
            switch (logLevel) {
                case HiLogType.V:
                    color = 0xffbbbbbb;
                    break;
                case HiLogType.D:
                    color = 0xffffffff;
                    break;
                case HiLogType.I:
                    color = 0xff6a8759;
                    break;
                case HiLogType.W:
                    color = 0xffbb529;
                    break;
                case HiLogType.E:
                    color = 0xffbb6b68;
                    break;
                default:
                    color = 0xffffff00;
                    break;
            }
            return color;
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag;
        TextView tvMessage;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }
    }

}
