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

package com.koresuniku.wishmaster.ui.utils

import android.content.Context
import android.os.Build
import android.support.design.widget.TabLayout
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.view.WindowManager
import android.widget.GridView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.module.gallery.ImageItemData
import com.koresuniku.wishmaster.ui.view.widget.WMGridView
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by koresuniku on 13.11.17.
 */

class ViewUtils @Inject constructor(private val deviceUtils: DeviceUtils) {

    fun disableTabLayout(tabLayout: TabLayout) {
        val tabStrip = tabLayout.getChildAt(0) as LinearLayout
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).setOnTouchListener { _, _ -> true }
        }
    }

    fun enableTabLayout(tabLayout: TabLayout) {
        val tabStrip = tabLayout.getChildAt(0) as LinearLayout
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).setOnTouchListener { _, _ -> false }
        }
    }

    fun setDynamicHeight(gridView: GridView) {
        val gridViewAdapter = gridView.adapter ?: return

        var totalHeight: Int
        val items = gridViewAdapter.count
        val rows: Int
        val columns = gridView.numColumns

        val listItem = gridViewAdapter.getView(0, null, gridView)
        listItem.measure(0, 0)
        totalHeight = listItem.measuredHeight

        val x: Float
        if (items > columns) {
            x = (items / columns).toFloat()
            rows = (x + 1).toInt()
            totalHeight *= rows
        }

        val params = gridView.layoutParams
        params.height = totalHeight
        gridView.layoutParams = params
    }

    fun measureView(view: View) {
        val wm = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        view.measure(display.width, display.height)
    }

    fun setGridViewHeight(gridView: GridView, imageItemDataList: List<ImageItemData>,
                          columnWidth: Int, summaryHeight: Int) {
        var lastRowHeight = 0
        var finalHeight = 0
        val columnCount = getGridViewColumnNumber(gridView.context, columnWidth)
        val verticalSpacing = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            gridView.verticalSpacing
        } else {
            0

        }
        Log.d("ViewUtils", "gridView.vertical spacing: $verticalSpacing")

        val calculation = Completable.create { e -> kotlin.run {
            imageItemDataList.forEachIndexed({ position, data ->
                var itemHeight = data.dimensions.heightInPx + summaryHeight
                if (position / columnCount > 1) itemHeight += verticalSpacing
                if (position == 0 || (position >= columnCount && columnCount % position == 0)) {
                    if (position != 0) lastRowHeight = finalHeight
                    finalHeight += itemHeight
                } else if (lastRowHeight + itemHeight > finalHeight) {
                    finalHeight = lastRowHeight + itemHeight
                }
            })
            e.onComplete()
        } }
        calculation
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { gridView.layoutParams.height = finalHeight })
    }

    fun getGridViewHeight(context: Context,
                          imageItemDataList: List<ImageItemData>,
                          columnWidth: Int, summaryHeight: Int): Single<WMGridView.GridViewParams> {
        return Single.create({
            val rowsHeights: MutableList<Int> = ArrayList()

            var lastRowHeight = 0
            var finalHeight = 0
            val columnCount = getGridViewColumnNumber(context, columnWidth)
            val verticalSpacing = context.resources.getDimension(R.dimen.thread_post_side_margin_default).toInt()

            imageItemDataList.forEachIndexed({ position, data ->
                val itemHeight = data.dimensions.heightInPx + summaryHeight

                //first item in a next row detected
                if (position != 0 && position % columnCount == 0) {
                    finalHeight += verticalSpacing
                    lastRowHeight = finalHeight
                    finalHeight += itemHeight
                } else if (lastRowHeight + itemHeight > finalHeight) {
                    finalHeight = lastRowHeight + itemHeight
                    val rowsHeightPosition = position / columnCount
                    if (rowsHeights.size - 1 >= rowsHeightPosition) {
                        rowsHeights.removeAt(rowsHeightPosition)
                    }
                    rowsHeights.add(rowsHeightPosition, itemHeight)
                }
            })

            it.onSuccess(WMGridView.GridViewParams(finalHeight, columnCount, rowsHeights))
        })
    }

    private fun getGridViewColumnNumber(context: Context, columnWidth: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            deviceUtils.getDisplayWidth(context) / (columnWidth +
                    context.resources.getDimension(R.dimen.thread_post_side_margin_default).toInt())
        } else {
            deviceUtils.getDisplayWidth(context) / (columnWidth)
        }
    }

}