package com.example.hirouter.ui.dashboard

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

@Destination(pageUrl = "main/tabs/dashboard")
class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        Log.e("TAG", "DashboardFragment -- onCreateView: ")
        return root
    }


    override fun onStart() {
        super.onStart()
        Log.e("TAG", "DashboardFragment -- onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", "DashboardFragment -- onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("TAG", "DashboardFragment -- onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG", "DashboardFragment -- onStop: ")
    }
}