package com.koresuniku.wishmaster_v4.core.utils.text.markup

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2



/**
 * Created by koresuniku on 2/10/18.
 */
class SingleImageCommentMarginSpan(private val lines: Int, private val margin: Int): LeadingMarginSpan2 {

    override fun getLeadingMargin(first: Boolean) = if (first) margin else 0

    override fun getLeadingMarginLineCount() = lines

    override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int, top: Int, baseline: Int, bottom: Int,
                          text: CharSequence, start: Int, end: Int, first: Boolean, layout: Layout) {}
}