package com.koresuniku.wishmaster.ui.dashboard.favourite_threads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment

/**
 * Created by koresuniku on 10.11.17.
 */

class FavouriteThreadsFragment : BaseWishmasterFragment() {
    private val LOG_TAG = FavouriteThreadsFragment::class.java.simpleName

    override lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_favourite_threads, container, false)

        return rootView
    }
}