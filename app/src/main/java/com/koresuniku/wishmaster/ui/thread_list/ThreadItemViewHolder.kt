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

package com.koresuniku.wishmaster.ui.thread_list

import android.graphics.Rect
import android.graphics.RectF
import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.modules.thread_list.IThreadListPresenter
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.core.modules.thread_list.ThreadItemView
import com.koresuniku.wishmaster.ui.gallery.preview.PreviewImageGridAdapter
import com.koresuniku.wishmaster.ui.view.widget.WMGridView
import javax.inject.Inject

/**
 * Created by koresuniku on 07.01.18.
 */

class ThreadItemViewHolder(itemView: View, injector: IWishmasterDaggerInjector) :
        RecyclerView.ViewHolder(itemView),
        ThreadItemView, WMGridView.OnNoItemClickListener,
        WMGridView.OnImageItemClickListener{
    private val LOG_TAG = ThreadItemViewHolder::class.java.simpleName

    @Inject lateinit var presenter: IThreadListPresenter

    @Nullable @BindView(R.id.top) lateinit var mTop: View
    @Nullable @BindView(R.id.item_layout) lateinit var mItemLayout: ViewGroup
    @Nullable @BindView(R.id.thread_item_container) lateinit var mThreadItemContainer: ViewGroup
    @Nullable @BindView(R.id.subject) lateinit var mSubject: TextView
    @Nullable @BindView(R.id.comment) lateinit var mComment: TextView
    @Nullable @BindView(R.id.resume) lateinit var mResume: TextView

    @Nullable @BindView(R.id.image_layout) lateinit var mImageLayout: ViewGroup
    @Nullable @BindView(R.id.image) lateinit var mImage: ImageView
    @Nullable @BindView(R.id.image_comment_container) lateinit var mImageCommentContainer: ViewGroup
    @Nullable @BindView(R.id.summary) lateinit var mImageSummary: TextView
    @Nullable @BindView(R.id.image_grid) lateinit var mImageGrid: WMGridView

    private var mIsSubjectVisible = true
    override lateinit var threadNumber: String
    private var mThreadPosition: Int = 0

    init {
        ButterKnife.bind(this, itemView)
        injector.daggerThreadListViewComponent.inject(this)
    }

    override fun adaptLayout(position: Int) {
        mThreadPosition = position
        mTop.visibility = if (position == 0) View.GONE else View.VISIBLE
    }

    override fun setOnClickItemListener() {
        mThreadItemContainer.setOnClickListener {
            Log.d(LOG_TAG, "onLayoutClicked")
            presenter.onThreadItemClicked(threadNumber)
        }
    }

    override fun switchSubjectVisibility(visible: Boolean) {
        mSubject.visibility = if (visible) View.VISIBLE else View.GONE
        mIsSubjectVisible = visible
    }

    override fun setSubject(subject: Spanned) { mSubject.text = subject }
    override fun setMaxLines(value: Int) { mComment.maxLines = value }
    override fun setComment(comment: Spanned) { mComment.post { mComment.text = comment } }
    override fun setThreadShortInfo(info: String) { mResume.text = info }

    override fun setSingleImage(imageItemData: ImageItemData, url: String, imageUtils: WishmasterImageUtils) {
        val imageLayout = itemView.findViewById<ViewGroup>(R.id.clickable_item_layout)
        val image = imageLayout.findViewById<ImageView>(R.id.image)

        imageLayout.setOnClickListener {
            val rect = Rect()
            image.getGlobalVisibleRect(rect)
            val coordinates = WishmasterImageUtils.ImageCoordinates(
                    rect.left, rect.right, rect.top, rect.bottom)
            presenter.setPreviewImageCoordinates(coordinates)
            onImageItemClick(0)
        }
        imageLayout.setOnTouchListener { _, _ -> false }

        (mImageCommentContainer.layoutParams as RelativeLayout.LayoutParams).topMargin =
               if (mIsSubjectVisible) itemView.context.resources.getDimension(R.dimen.thread_item_image_comment_is_subject_top_margin).toInt()
               else itemView.context.resources.getDimension(R.dimen.thread_item_image_comment_no_subject_top_margin).toInt()
        mImageSummary.text = imageItemData.summary
        imageUtils.loadImageThumbnail(imageItemData, image, url)
    }

    override fun setMultipleImages(imageItemDataList: List<ImageItemData>,
                                   url: String,
                                   imageUtils: WishmasterImageUtils,
                                   gridViewParams: WMGridView.GridViewParams,
                                   summaryHeight: Int) {
        mImageGrid.setOnNoItemClickListener(this)
        (mImageGrid.layoutParams as RelativeLayout.LayoutParams).topMargin =
                if (mIsSubjectVisible) itemView.context.resources.getDimension(R.dimen.thread_item_image_comment_is_subject_top_margin).toInt()
                else itemView.context.resources.getDimension(R.dimen.thread_item_image_comment_no_subject_top_margin).toInt()
        mImageGrid.layoutParams.height = gridViewParams.finalHeight
        mImageGrid.columnWidth = imageItemDataList[0].dimensions.widthInPx
        mImageGrid.adapter = PreviewImageGridAdapter(
                imageItemDataList, url, imageUtils, summaryHeight,
                gridViewParams, this, this)
        mImageGrid.requestLayout()
    }

    override fun onNoItemClick() { presenter.onThreadItemClicked(threadNumber) }

    override fun onImageItemClick(position: Int) {
        presenter.onOpenGalleryClick(mThreadPosition, position)
    }
}