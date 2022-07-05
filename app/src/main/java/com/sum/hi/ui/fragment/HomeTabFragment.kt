package com.sum.hi.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * @author tea
 * @date   2022/7/5 17:56
 * @desc
 */
class HomeTabFragment : HiAbsFragment() {

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment = HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }
}