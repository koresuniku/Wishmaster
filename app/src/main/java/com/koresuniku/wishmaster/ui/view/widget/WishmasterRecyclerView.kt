package com.koresuniku.wishmaster.ui.view.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.koresuniku.wishmaster.application.preferences.UiParams

/**
 * Created by koresuniku on 2/16/18.
 */

class WishmasterRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun canScrollVertically(direction: Int): Boolean {
        // check if scrolling up
        if (direction < 1) {
            val original = super.canScrollVertically(direction)
            return !original && getChildAt(0) != null && getChildAt(0).top < 0 || original
        }
        return super.canScrollVertically(direction)
    }

    fun checkRefreshPossibility(actualAppBarOffset: Int, uiParams: UiParams): RefreshPossibility {
        val readyToRefreshTop: Boolean = !canScrollVertically(-1) && actualAppBarOffset == 0
        val readyToRefreshBottom: Boolean = !canScrollVertically(1)
                && Math.abs(actualAppBarOffset) == uiParams.toolbarHeight

        if (readyToRefreshTop) return RefreshPossibility(RefreshPossibility.TOP)
        if (readyToRefreshBottom) return RefreshPossibility(RefreshPossibility.BOTTOM)

        return RefreshPossibility(RefreshPossibility.NONE)
    }

    fun checkRefreshPossibility(): RefreshPossibility {
        val readyToRefreshTop: Boolean = !canScrollVertically(-1)
        val readyToRefreshBottom: Boolean = !canScrollVertically(1)

        if (readyToRefreshTop && readyToRefreshBottom) {
            return RefreshPossibility(RefreshPossibility.BOTH)
        } else {
            if (readyToRefreshTop) return RefreshPossibility(RefreshPossibility.TOP)
            if (readyToRefreshBottom) return RefreshPossibility(RefreshPossibility.BOTTOM)
        }

        return RefreshPossibility(RefreshPossibility.NONE)
    }

    data class RefreshPossibility(val possibility: Int) {
        companion object {
            const val NONE = 0
            const val TOP = 1
            const val BOTTOM = 2
            const val BOTH = 3
        }
    }
}