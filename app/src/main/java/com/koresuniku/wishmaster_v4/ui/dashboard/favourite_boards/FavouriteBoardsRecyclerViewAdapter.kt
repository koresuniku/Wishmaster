package com.koresuniku.wishmaster_v4.ui.dashboard.favourite_boards

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardPresenter
import com.koresuniku.wishmaster_v4.core.data.boards.BoardModel
import com.koresuniku.wishmaster_v4.core.util.text.WishmasterTextUtils
import com.koresuniku.wishmaster_v4.ui.view.drag_and_swipe_recycler_view.ItemTouchHelperAdapter
import com.koresuniku.wishmaster_v4.ui.view.drag_and_swipe_recycler_view.OnStartDragListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by koresuniku on 13.11.17.
 */

class FavouriteBoardsRecyclerViewAdapter(
        private val mOnStartDragListener: OnStartDragListener,
        private val mPresenter: DashboardPresenter,
        private val mCompositeDisposable: CompositeDisposable) :
        RecyclerView.Adapter<FavouriteBoardsRecyclerViewViewHolder>(), ItemTouchHelperAdapter {
    private val LOG_TAG = FavouriteBoardsRecyclerViewAdapter::class.java.simpleName

    private var mFavouriteBoards: List<BoardModel>

    init {
        mFavouriteBoards = ArrayList()
    }

    fun bindFavouriteBoardList(favouriteBoardList: List<BoardModel>) {
        mFavouriteBoards = favouriteBoardList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mFavouriteBoards.size

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: FavouriteBoardsRecyclerViewViewHolder, position: Int) {
        val boardModel = mFavouriteBoards[position]
        holder.mBoardName.text = WishmasterTextUtils.obtainBoardIdDashName(boardModel)
        holder.mDragAndDrop.setOnLongClickListener { mOnStartDragListener.onStartDrag(holder); false }
        holder.itemView.setOnClickListener { mPresenter.shouldLaunchThreadListActivity(boardModel.getBoardId()) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteBoardsRecyclerViewViewHolder {
        return FavouriteBoardsRecyclerViewViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_favourite_boards_board_item, parent, false))
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mFavouriteBoards, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mFavouriteBoards, i, i - 1)
            }
        }

        mFavouriteBoards.forEachIndexed { index, boardModel -> boardModel.setFavouritePosition(index) }

        mCompositeDisposable.add(mPresenter.reorderFavouriteBoardList(mFavouriteBoards)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemRemoved(position: Int) {
    }

    override fun onSelectedChanged(actionState: Int) {
    }
}