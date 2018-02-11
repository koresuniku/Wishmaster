package com.koresuniku.wishmaster.ui.dashboard.favourite_boards

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.modules.dashboard.presenter.IDashboardPresenter
import com.koresuniku.wishmaster.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster.core.utils.text.WishmasterTextUtils
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view.ItemTouchHelperAdapter
import com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view.OnStartDragListener
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

/**
 * Created by koresuniku on 13.11.17.
 */

class FavouriteBoardsRecyclerViewAdapter():
        RecyclerView.Adapter<FavouriteBoardsRecyclerViewViewHolder>(), ItemTouchHelperAdapter {
    private val LOG_TAG = FavouriteBoardsRecyclerViewAdapter::class.java.simpleName

    @Inject lateinit var presenter: IDashboardPresenter
    @Inject lateinit var textUtils: WishmasterTextUtils

    private lateinit var mActivity: WeakReference<BaseWishmasterActivity<IDashboardPresenter>>
    private lateinit var mOnStartDragListener: OnStartDragListener
    private var mFavouriteBoards: List<BoardModel> = emptyList()

    constructor(activity: BaseWishmasterActivity<IDashboardPresenter>,
                onStartDragListener: OnStartDragListener) : this() {
        activity.getWishmasterApplication().daggerDashboardViewComponent.inject(this)
        this.mActivity = WeakReference(activity)
        this.mOnStartDragListener = onStartDragListener
    }

    fun bindFavouriteBoardList(favouriteBoardList: List<BoardModel>) {
        mFavouriteBoards = favouriteBoardList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mFavouriteBoards.size

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: FavouriteBoardsRecyclerViewViewHolder, position: Int) {
        val boardModel = mFavouriteBoards[position]
        holder.mBoardName.text = textUtils.obtainBoardIdDashName(boardModel)
        holder.mDragAndDrop.setOnLongClickListener { mOnStartDragListener.onStartDrag(holder); false }
        holder.itemView.setOnClickListener { presenter.shouldLaunchThreadListActivity(boardModel.getBoardId()) }
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

        presenter.reorderFavouriteBoardList(mFavouriteBoards)

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemRemoved(position: Int) {}
    override fun onSelectedChanged(actionState: Int) {}
}