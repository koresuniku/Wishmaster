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

package com.koresuniku.wishmaster.ui.dashboard.board_list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.data.model.boards.BoardListsObject
import com.koresuniku.wishmaster.core.data.database.repository.BoardsRepository
import java.lang.ref.WeakReference

/**
 * Created by koresuniku on 12.11.17.
 */

class BoardListAdapter (private val mContext: WeakReference<Context>,
                        private val mBoardsListsObject: BoardListsObject,
                        private val mPresenter: IDashboardPresenter)
    : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any = mBoardsListsObject.boardLists[groupPosition].second

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var newConvertView = convertView
        if (newConvertView == null) {
            mContext.get()?.let { newConvertView = LayoutInflater.from(it).inflate(
                    R.layout.fragment_board_list_group_item, parent, false) }
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
            mContext.get()?.let { newConvertView = LayoutInflater.from(it).inflate(
                    R.layout.fragment_board_list_child_item, parent, false) }
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

        makeFavouriteButton?.setOnClickListener({
            mPresenter.switchBoardFavourability(boardModel.getBoardId())
        })

        return newConvertView!!
    }

    fun onBoardFavourabilityChanged(boardId: String, newFavouritePosition: Int) {
        mBoardsListsObject.boardLists.forEach {
            it.second.firstOrNull { it.getBoardId() == boardId }?.let {
                Log.d("BLA", "foundBoardId: ${it.getBoardId()}")
                it.setFavouritePosition(newFavouritePosition)
                this.notifyDataSetChanged()
            }
        }
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getGroupCount(): Int = mBoardsListsObject.boardLists.size
}