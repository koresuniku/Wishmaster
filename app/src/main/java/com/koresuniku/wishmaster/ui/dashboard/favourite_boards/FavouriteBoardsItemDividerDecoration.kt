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

package com.koresuniku.wishmaster.ui.dashboard.favourite_boards

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import com.koresuniku.wishmaster.R

/**
 * Created by koresuniku on 13.11.17.
 */

class FavouriteBoardsItemDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable = context.resources.getDrawable(R.drawable.line_divider)

    private var isDragging = false

    fun onStartDrag() { isDragging = true }
    fun onFinishDrag() { isDragging = false }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft
        val right = (parent.width - parent.paddingRight)

        val childCount = parent.childCount
        for (i in 0 until if (isDragging) childCount - 1 else childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            return
        }

        outRect.top = mDivider.intrinsicHeight
    }
}