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

package com.koresuniku.wishmaster.core.utils.text.markup

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