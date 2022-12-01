package com.sum.hi_debugtool

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sum.hi.common.component.HiBaseActivity
import com.sum.hi.hilibrary.crash.CrashManger
import kotlinx.android.synthetic.main.activity_crash_log.*
import kotlinx.android.synthetic.main.crash_log_item.view.*
import java.io.File

/**
 * @创建者 mingyan.su
 * @创建时间 2022/11/30 23:27
 * @类描述 ${TODO}
 */
class CrashLogActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_log)

        val crashFiles = CrashManger.crashFiles()
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = CrashLogAdapter(crashFiles)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_divider2)!!)
        recycler_view.addItemDecoration(decoration)
    }

    inner class CrashLogAdapter(val crashFiles: Array<File>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return object : RecyclerView.ViewHolder(
                layoutInflater.inflate(
                    R.layout.crash_log_item,
                    parent,
                    false
                )
            ) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val file = crashFiles[position]
            holder.itemView.tv_title.text = file.name
            holder.itemView.tv_share.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra("subject", "")
                intent.putExtra("body", "")

                //大于7.0
                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(
                        this@CrashLogActivity,
                        "${packageName}.fileProvider",
                        file
                    )
                } else {
                    Uri.fromFile(file)
                }
                intent.putExtra(Intent.EXTRA_STREAM, uri)//添加文件流
                if (file.name.endsWith(".txt")) {
                    intent.type = "text/plain"//纯文本
                } else {
                    intent.type = "application/actet-stream"//二进制文件流
                }
                startActivity(Intent.createChooser(intent, "分享Crash日志文件"))
            }
        }

        override fun getItemCount(): Int {
            return crashFiles.size
        }

    }
}