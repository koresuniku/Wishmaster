package com.koresuniku.wishmaster.ui.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

/**
 * Created by koresuniku on 18.01.18.
 */

class ExpandableHeightGridView : GridView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

//    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
//        super.onMeasure(widthMeasureSpec, expandSpec)
////
////        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST))
////        layoutParams.height = measuredHeight
//////        //Log.d("EHGV", "onMeasure")
//////       // super.onMeasure(widthMeasureSpec, finalHeight)
//    }
//
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        //Log.d("EHGV", "onDraw")
//    }




}