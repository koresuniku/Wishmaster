package com.koresuniku.wishmaster.ui.dashboard.favourite_boards

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R

/**
 * Created by koresuniku on 13.11.17.
 */

class FavouriteBoardsRecyclerViewViewHolder(mItemView: View) : RecyclerView.ViewHolder(mItemView) {
    @BindView(R.id.board_name) lateinit var mBoardName: TextView
    @BindView(R.id.drag_and_drop) lateinit var mDragAndDrop: ImageView

    init {
        ButterKnife.bind(this, mItemView)
    }

}