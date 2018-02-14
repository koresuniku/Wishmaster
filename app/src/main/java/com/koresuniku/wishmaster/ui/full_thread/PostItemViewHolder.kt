package com.koresuniku.wishmaster.ui.full_thread

import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.modules.full_thread.presenter.IFullThreadPresenter
import com.koresuniku.wishmaster.core.modules.full_thread.view.PostItemView
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.utils.images.WishmasterImageUtils
import com.koresuniku.wishmaster.ui.preview.PreviewImageGridAdapter
import javax.inject.Inject

/**
 * Created by koresuniku on 2/14/18.
 */

class PostItemViewHolder(itemView: View, injector: IWishmasterDaggerInjector) :
        RecyclerView.ViewHolder(itemView), PostItemView {
    private val LOG_TAG = PostItemViewHolder::class.java.simpleName

    @Inject lateinit var presenter: IFullThreadPresenter

    @Nullable @BindView(R.id.item_layout) lateinit var mItemLayout: ViewGroup
    @Nullable @BindView(R.id.head) lateinit var mHeader: TextView
    @Nullable @BindView(R.id.comment) lateinit var mComment: TextView

    @Nullable @BindView(R.id.image_layout) lateinit var mImageLayout: ViewGroup
    @Nullable @BindView(R.id.image) lateinit var mImage: ImageView
    @Nullable @BindView(R.id.image_comment_container) lateinit var mImageCommentContainer: ViewGroup
    @Nullable @BindView(R.id.summary) lateinit var mImageSummary: TextView
    @Nullable @BindView(R.id.image_grid) lateinit var mImageGrid: GridView

    @Nullable @BindView(R.id.answers_layout) lateinit var mAnswersLayout: ViewGroup
    @Nullable @BindView(R.id.answers) lateinit var mAnswers: TextView

    init {
        ButterKnife.bind(this, itemView)
        injector.daggerFullThreadViewComponent.inject(this)
    }

    override fun setOnClickItemListener(threadNumber: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setHeader(head: Spannable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun switchAnswersVisibility(visible: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAnswers(subject: Spanned) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setComment(comment: Spanned) { mComment.text = comment }

    override fun setSingleImage(imageItemData: ImageItemData, url: String, imageUtils: WishmasterImageUtils) {
        val imageLayout = itemView.findViewById<ViewGroup>(R.id.image_layout)
        val image = imageLayout.findViewById<ImageView>(R.id.image)

        mImageSummary.text = imageItemData.summary
        imageUtils.loadImageThumbnail(imageItemData, image, url)
    }

    override fun setMultipleImages(imageItemDataList: List<ImageItemData>,
                                   url: String,
                                   imageUtils: WishmasterImageUtils,
                                   gridViewHeight: Int) {
        mImageGrid.columnWidth = imageItemDataList[0].dimensions.widthInPx
        mImageGrid.adapter = PreviewImageGridAdapter(imageItemDataList, url, imageUtils)
        mImageGrid.layoutParams.height = gridViewHeight
    }
}