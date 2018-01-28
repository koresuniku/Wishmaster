package com.koresuniku.wishmaster_v4.ui.view.drag_and_swipe_recycler_view

import android.support.v7.widget.RecyclerView

/**
 * Created by koresuniku on 13.11.17.
 */

interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}