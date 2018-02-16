package com.koresuniku.wishmaster.ui.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.AdapterView
import android.widget.GridView

/**
 * Created by koresuniku on 2/16/18.
 */

class TouchyGridView(context: Context, attrs: AttributeSet) : GridView(context, attrs) {

    private var listener: OnNoItemClickListener? = null

    interface OnNoItemClickListener {
        fun onNoItemClick()
    }

    fun setOnNoItemClickListener(listener: OnNoItemClickListener) {
        this.listener = listener
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // The pointToPosition() method returns -1 if the touch event
        // occurs outside of a child View.
        // Change the MotionEvent action as needed. Here we use ACTION_UP
        // as a simple, naive indication of a click.
        if (pointToPosition(event.x.toInt(), event.y.toInt()) == -1 && event.action == MotionEvent.ACTION_UP) {
            if (listener != null) {
                listener!!.onNoItemClick()
            }
        }
        return super.dispatchTouchEvent(event)
    }
}