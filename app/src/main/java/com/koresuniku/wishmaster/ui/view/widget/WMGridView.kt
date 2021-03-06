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

import android.R.attr.max
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.GridView
import android.R.attr.numColumns



/**
* Created by koresuniku on 2/16/18.
*/

class WMGridView(context: Context, attrs: AttributeSet) : GridView(context, attrs) {

    private var listener: OnNoItemClickListener? = null

    data class GridViewParams(val finalHeight: Int, val numColumns: Int, val maxHeightInARow: List<Int>)

    interface OnNoItemClickListener {
        fun onNoItemClick()
    }

    interface OnImageItemClickListener {
        fun onImageItemClick(position: Int)
    }

    fun setOnNoItemClickListener(listener: OnNoItemClickListener) {
        this.listener = listener
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (pointToPosition(event.x.toInt(), event.y.toInt()) == -1
                && event.action == MotionEvent.ACTION_UP) {
            listener?.onNoItemClick()
        }
        return super.dispatchTouchEvent(event)
    }

}