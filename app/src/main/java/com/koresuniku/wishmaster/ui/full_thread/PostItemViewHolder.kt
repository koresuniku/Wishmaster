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

package com.koresuniku.wishmaster.ui.full_thread

import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.IWishmasterDependencyInjector
import com.koresuniku.wishmaster.core.module.full_thread.FullThreadContract
import com.koresuniku.wishmaster.core.module.gallery.ImageItemData
import com.koresuniku.wishmaster.application.global.WMImageUtils
import com.koresuniku.wishmaster.ui.gallery.PreviewImageGridAdapter
import com.koresuniku.wishmaster.ui.view.widget.WMGridView
import javax.inject.Inject

/**
 * Created by koresuniku on 2/14/18.
 */

class PostItemViewHolder(itemView: View, injector: IWishmasterDependencyInjector) :
        RecyclerView.ViewHolder(itemView), FullThreadContract.IPostItemView,
        WMGridView.OnNoItemClickListener,
        WMGridView.OnImageItemClickListener {
    private val LOG_TAG = PostItemViewHolder::class.java.simpleName

    @Inject lateinit var presenter: FullThreadContract.IFullThreadPresenter

    @Nullable @BindView(R.id.item_layout) lateinit var mItemLayout: ViewGroup
    @Nullable @BindView(R.id.head) lateinit var mHeader: TextView
    @Nullable @BindView(R.id.comment) lateinit var mComment: TextView

    @Nullable @BindView(R.id.post_item_container) lateinit var mPostItemContainer: ViewGroup
    @Nullable @BindView(R.id.image_layout) lateinit var mImageLayout: ViewGroup
    @Nullable @BindView(R.id.image) lateinit var mImage: ImageView
    @Nullable @BindView(R.id.image_comment_container) lateinit var mImageCommentContainer: ViewGroup
    @Nullable @BindView(R.id.summary) lateinit var mImageSummary: TextView
    @Nullable @BindView(R.id.image_grid) lateinit var mImageGrid: WMGridView

    @Nullable @BindView(R.id.answers_layout) lateinit var mAnswersLayout: ViewGroup
    @Nullable @BindView(R.id.answers) lateinit var mAnswers: TextView

    init {
        ButterKnife.bind(this, itemView)
        injector.daggerFullThreadViewComponent.inject(this)
    }

    override fun setOnClickItemListener(threadNumber: String) {

    }

    override fun setHeader(head: Spannable) {
        mHeader.text = head
    }

    override fun switchAnswersVisibility(visible: Boolean) {
        if (visible) {
            mAnswersLayout.visibility = View.VISIBLE
            mPostItemContainer.setPadding(
                    mPostItemContainer.paddingLeft,
                    mPostItemContainer.paddingTop,
                    mPostItemContainer.paddingRight,
                    0)
        } else {
            mAnswersLayout.visibility = View.GONE
            mPostItemContainer.setPadding(
                    mPostItemContainer.paddingLeft,
                    mPostItemContainer.paddingTop,
                    mPostItemContainer.paddingRight,
                    itemView.context.resources.getDimension(R.dimen.post_item_no_answers_bottom_padding).toInt())
        }
    }

    override fun setAnswers(subject: Spanned) {

    }

    override fun setComment(comment: Spanned) {
        mComment.post { mComment.text = comment }
    }

    override fun setSingleImage(imageItemData: ImageItemData, url: String, imageUtils: WMImageUtils) {
        val imageLayout = itemView.findViewById<ViewGroup>(R.id.image_layout)
        val image = imageLayout.findViewById<ImageView>(R.id.image)

        mImageSummary.text = imageItemData.summary
        imageUtils.loadImageThumbnail(imageItemData, image, url)
    }

    override fun setMultipleImages(imageItemDataList: List<ImageItemData>,
                                   url: String,
                                   imageUtils: WMImageUtils,
                                   gridViewParams: WMGridView.GridViewParams,
                                   summaryHeight: Int) {
        mImageGrid.setOnNoItemClickListener(this)
        mImageGrid.columnWidth = imageItemDataList[0].dimensions.widthInPx
        mImageGrid.adapter = PreviewImageGridAdapter(
                imageItemDataList, url, imageUtils, summaryHeight, gridViewParams,
                this, this)
        mImageGrid.layoutParams.height = gridViewParams.finalHeight
    }

    override fun onNoItemClick() {

    }

    override fun onImageItemClick(position: Int) {

    }
}