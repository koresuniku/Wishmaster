package com.koresuniku.wishmaster_v4.ui.dashboard.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.ui.base.BaseWishmasterFragment

/**
 * Created by koresuniku on 12.11.17.
 */

class HistoryFragment : BaseWishmasterFragment() {
    private val LOG_TAG = HistoryFragment::class.java.simpleName

    override lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_history, container, false)

        return rootView
    }
}
