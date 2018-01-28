package com.koresuniku.wishmaster_v4.ui.dashboard.history

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster_v4.R

/**
 * Created by koresuniku on 12.11.17.
 */

class HistoryFragment : Fragment() {
    private val LOG_TAG = HistoryFragment::class.java.simpleName

    private lateinit var mRootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_history, container, false)

        return mRootView
    }
}
