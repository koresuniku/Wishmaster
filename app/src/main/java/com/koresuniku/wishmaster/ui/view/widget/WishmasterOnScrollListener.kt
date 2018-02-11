package com.koresuniku.wishmaster.ui.view.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView
import com.bumptech.glide.Glide

/**
 * Created by koresuniku on 18.01.18.
 */

class WishmasterOnScrollListener(private val context: Context) : RecyclerView.OnScrollListener() {


    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
            Glide.with(context).pauseRequests()
        } else {
            Glide.with(context).resumeRequests()
        }

    }

}