/*
 * Copyright (c) 2018 koresuniku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.koresuniku.wishmaster.ui.view.widget

import android.app.Activity
import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import com.bumptech.glide.Glide
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import java.lang.ref.WeakReference

/**
 * Created by koresuniku on 2/16/18.
 */

class WishmasterRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var mAppBarLayout: AppBarLayout? = null
    private var mSwipyRefreshLayout: SwipyRefreshLayout? = null
    private var mActivityReference: WeakReference<Activity>? = null
    private var mActualAppBarOffset: Int = -1

    fun attachAppBarLayout(appBarLayout: AppBarLayout) {
        mAppBarLayout = appBarLayout
        mAppBarLayout?.addOnOffsetChangedListener { _, verticalOffset ->
            mActualAppBarOffset = verticalOffset
        }
    }

    fun attachSwipyRefreshLayout(swipyRefreshLayout: SwipyRefreshLayout) {
        mSwipyRefreshLayout = swipyRefreshLayout
    }

    fun attachActivity(activity: Activity) {
        mActivityReference = WeakReference(activity)
    }

    override fun canScrollVertically(direction: Int): Boolean {
        if (direction < 1) {
            val original = super.canScrollVertically(direction)
            return !original && getChildAt(0) != null && getChildAt(0).top < 0 || original
        }
        return super.canScrollVertically(direction)
    }

    fun checkRefresh() {
        when (checkRefreshPossibility().possibility) {
            WishmasterRecyclerView.RefreshPossibility.TOP -> {
                mSwipyRefreshLayout?.direction = SwipyRefreshLayoutDirection.TOP
                mSwipyRefreshLayout?.isEnabled = true
            }
            WishmasterRecyclerView.RefreshPossibility.BOTTOM -> {
                mSwipyRefreshLayout?.direction = SwipyRefreshLayoutDirection.BOTTOM
                mSwipyRefreshLayout?.isEnabled = true
            }
            WishmasterRecyclerView.RefreshPossibility.BOTH -> {
                mSwipyRefreshLayout?.direction = SwipyRefreshLayoutDirection.BOTH
                mSwipyRefreshLayout?.isEnabled = true
            }
            else -> mSwipyRefreshLayout?.isEnabled = false
        }
    }

    private fun checkRefreshPossibility(): RefreshPossibility {
        return if (mAppBarLayout == null)
            checkRefreshPossibilityDefault() else
            checkRefreshPossibilityConsiderAppBar()
    }

    private fun checkRefreshPossibilityConsiderAppBar(): RefreshPossibility {
        val readyToRefreshTop: Boolean = !canScrollVertically(-1) && mActualAppBarOffset == 0
        val readyToRefreshBottom: Boolean = !canScrollVertically(1)
                && Math.abs(mActualAppBarOffset) == mAppBarLayout?.height

        if (readyToRefreshTop && readyToRefreshBottom) {
            return RefreshPossibility(RefreshPossibility.BOTH)
        } else {
            if (readyToRefreshTop) return RefreshPossibility(RefreshPossibility.TOP)
            if (readyToRefreshBottom) return RefreshPossibility(RefreshPossibility.BOTTOM)
        }

        return RefreshPossibility(RefreshPossibility.NONE)
    }

    private fun checkRefreshPossibilityDefault(): RefreshPossibility {
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

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        checkRefresh()
    }
}