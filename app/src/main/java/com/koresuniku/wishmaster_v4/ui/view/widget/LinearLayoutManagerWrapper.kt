package com.koresuniku.wishmaster_v4.ui.view.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.util.Log

/**
 * Created by koresuniku on 07.01.18.
 */

class LinearLayoutManagerWrapper(context: Context, orientation: Int, reverseLayout: Boolean) :
        LinearLayoutManager(context, orientation, reverseLayout) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.d("LLMW", e.message)
            e.printStackTrace()
        }
    }



    override fun supportsPredictiveItemAnimations(): Boolean = false
}