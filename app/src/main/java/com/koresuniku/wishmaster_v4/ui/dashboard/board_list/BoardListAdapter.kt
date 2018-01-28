package com.koresuniku.wishmaster_v4.ui.dashboard.board_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardPresenter
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListsObject
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by koresuniku on 12.11.17.
 */

class BoardListAdapter (private val mContext: Context,
                        private val mBoardsListsObject: BoardListsObject,
                        private val mPresenter: DashboardPresenter,
                        private val mCompositeDisposable: CompositeDisposable)
    : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any = mBoardsListsObject.boardLists[groupPosition].second

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var newConvertView = convertView
        if (newConvertView == null) {
            newConvertView = LayoutInflater.from(mContext).inflate(
                    R.layout.fragment_board_list_group_item, parent, false)
        }
        val groupName: TextView? = newConvertView?.findViewById(R.id.group_board_name)
        groupName?.text = mBoardsListsObject.boardLists[groupPosition].first

        val indicator: ImageView? = newConvertView?.findViewById(R.id.group_item_indicator)
        if (isExpanded) {
            indicator?.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp)
        } else {
            indicator?.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp)
        }
        return newConvertView!!
    }

    override fun getChildrenCount(groupPosition: Int): Int = mBoardsListsObject.boardLists[groupPosition].second.size

    override fun getChild(groupPosition: Int, childPosition: Int): Any = mBoardsListsObject.boardLists[groupPosition].second[childPosition]

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var newConvertView = convertView
        if (newConvertView == null) {
            newConvertView = LayoutInflater.from(mContext).inflate(
                    R.layout.fragment_board_list_child_item, parent, false)
        }
        val childName: TextView? = newConvertView?.findViewById(R.id.child_board_name)
        val makeFavouriteButton: ImageView? = newConvertView?.findViewById(R.id.make_favourite_button)

        val boardModel = mBoardsListsObject.boardLists[groupPosition].second[childPosition]

        val name = "/${boardModel.getBoardId()}/ - ${boardModel.getBoardName()}"
        childName?.text = name

        makeFavouriteButton?.setImageResource(
                if (boardModel.getFavouritePosition() == BoardsRepository.FAVOURITE_POSITION_DEFAULT)
                    R.drawable.ic_favorite_border_gray_24dp
                else
                    R.drawable.ic_favorite_gray_24dp)
        makeFavouriteButton?.setOnClickListener { mCompositeDisposable.add(
                mPresenter.makeBoardFavourite(boardModel.getBoardId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ newPosition ->
                            boardModel.setFavouritePosition(newPosition)
                            makeFavouriteButton.setImageResource(
                                    if (newPosition == BoardsRepository.FAVOURITE_POSITION_DEFAULT)
                                        R.drawable.ic_favorite_border_gray_24dp
                                    else
                                        R.drawable.ic_favorite_gray_24dp)
                            mPresenter.reloadBoards()
                        }, { e -> e.printStackTrace()}))}
        return newConvertView!!
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getGroupCount(): Int = mBoardsListsObject.boardLists.size
}