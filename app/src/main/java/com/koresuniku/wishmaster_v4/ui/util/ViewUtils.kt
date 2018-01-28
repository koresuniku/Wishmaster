package com.koresuniku.wishmaster_v4.ui.util

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.GridView
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.gallery.ImageItemData
import android.opengl.ETC1.getWidth
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by koresuniku on 13.11.17.
 */

object ViewUtils {

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
        val columnCount = getGridViewColumnNumber(gridView, columnWidth)
        val verticalSpacing = gridView.verticalSpacing
        Log.d("ViewUtils", "gridView.numColumns: $columnCount")

        val calculation = Completable.create { e -> kotlin.run {
            imageItemDataList.forEachIndexed({ position, data ->
                var itemHeight = data.configuration.heightInPx + summaryHeight
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


        Log.d("ViewUtils", "finalHeight: $finalHeight")
        //gridView.post { gridView.layoutParams.height = finalHeight }

    }

    private fun getGridViewColumnNumber(gridView: GridView, columnWidth: Int): Int {
        return DeviceUtils.getDisplayWidth(gridView.context) / (columnWidth + gridView.horizontalSpacing)
    }

}