package com.example.hirouter.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hirouter.R
import com.example.nav_annotation.Destination

@Destination(pageUrl = "main/tabs/notification")
class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        Log.e("TAG", "NotificationsFragment -- onCreateView: ")
        return root
    }

    override fun onStart() {
        super.onStart()
        Log.e("TAG", "NotificationsFragment -- onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", "NotificationsFragment -- onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("TAG", "NotificationsFragment -- onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG", "NotificationsFragment -- onStop: ")
    }
}