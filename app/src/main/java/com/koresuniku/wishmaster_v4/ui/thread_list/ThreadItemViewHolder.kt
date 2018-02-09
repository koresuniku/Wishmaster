package com.koresuniku.wishmaster_v4.ui.thread_list

import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.gallery.ImageItemData
import com.koresuniku.wishmaster_v4.core.gallery.WishmasterImageUtils
import com.koresuniku.wishmaster_v4.core.thread_list.view.ThreadItemView
import com.koresuniku.wishmaster_v4.ui.preview.PreviewImageGridAdapter
import com.koresuniku.wishmaster_v4.ui.util.UiUtils
import com.koresuniku.wishmaster_v4.ui.util.ViewUtils

/**
 * Created by koresuniku on 07.01.18.
 */

class ThreadItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), ThreadItemView {
    private val LOG_TAG = ThreadItemViewHolder::class.java.simpleName

    @Nullable @BindView(R.id.subject) lateinit var mSubject: TextView
    @Nullable @BindView(R.id.comment) lateinit var mComment: TextView
    @Nullable @BindView(R.id.resume) lateinit var mResume: TextView

    @Nullable @BindView(R.id.image_layout) lateinit var mImageLayout: ViewGroup
    @Nullable @BindView(R.id.image) lateinit var mImage: ImageView
    @Nullable @BindView(R.id.image_comment_container) lateinit var mImageCommentContainer: ViewGroup
    @Nullable @BindView(R.id.summary) lateinit var mImageSummary: TextView

    @Nullable @BindView(R.id.image_grid) lateinit var mImageGrid: GridView

    private var mIsSubjectVisible = true

    init { ButterKnife.bind(this, itemView) }



    override fun switchSubjectVisibility(visible: Boolean) {
        mSubject.visibility = if (visible) View.VISIBLE else View.GONE
        mIsSubjectVisible = visible
    }

    override fun setSubject(subject: Spanned) { mSubject.text = subject }
    override fun setComment(comment: Spanned) { mComment.text = comment }
    override fun setThreadShortInfo(info: String) { mResume.text = info }

    override fun setSingleImage(imageItemData: ImageItemData, url: String, imageUtils: WishmasterImageUtils) {
        val imageLayout = itemView.findViewById<ViewGroup>(R.id.image_layout)
        val image = imageLayout.findViewById<ImageView>(R.id.image)
//        val imageCommentContainer = itemView.findViewById<ViewGroup>(R.id.image_comment_container)
//        val imageSummary = itemView.findViewById<TextView>(R.id.summary)


        (mImageCommentContainer.layoutParams as RelativeLayout.LayoutParams).topMargin =
               if (mIsSubjectVisible) itemView.context.resources.getDimension(R.dimen.thread_item_image_comment_is_subject_top_margin).toInt()
               else itemView.context.resources.getDimension(R.dimen.thread_item_image_comment_no_subject_top_margin).toInt()

        mImageSummary.text = imageItemData.summary
        imageUtils.loadImageThumbnail(imageItemData, image, url)
    }

    override fun setMultipleImages(imageItemDataList: List<ImageItemData>, url: String, imageUtils: WishmasterImageUtils) {
        (mImageGrid.layoutParams as RelativeLayout.LayoutParams).topMargin =
                if (mIsSubjectVisible) itemView.context.resources.getDimension(R.dimen.thread_item_image_comment_is_subject_top_margin).toInt()
                else itemView.context.resources.getDimension(R.dimen.thread_item_image_comment_no_subject_top_margin).toInt()

        mImageGrid.columnWidth = imageItemDataList[0].dimensions.widthInPx
        mImageGrid.adapter = PreviewImageGridAdapter(imageItemDataList, url, imageUtils)
//
        val summaryTextView = mImageGrid.adapter.getView(0, null, mImageGrid).findViewById<TextView>(R.id.summary)
        ViewUtils.measureView(summaryTextView)
        ViewUtils.setGridViewHeight(mImageGrid, imageItemDataList,
                imageItemDataList[0].dimensions.widthInPx, summaryTextView.measuredHeight)
    }
}