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

package com.koresuniku.wishmaster.ui.dashboard.favourite_boards

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster.core.module.dashboard.DashboardContract
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment
import com.koresuniku.wishmaster.ui.dashboard.DashboardActivity
import com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view.OnItemDroppedCallback
import com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view.OnStartDragListener
import com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view.SimpleItemTouchItemCallback
import javax.inject.Inject

/**
 * Created by koresuniku on 10.11.17.
 */

class FavouriteBoardsFragment : BaseWishmasterFragment(), DashboardContract.IDashboardFavouriteBoardsView,
        OnStartDragListener, OnItemDroppedCallback {
    private val LOG_TAG = FavouriteBoardsFragment::class.java.simpleName

    @Inject lateinit var presenter: DashboardContract.IDashboardPresenter

    override lateinit var rootView: ViewGroup
    @BindView(R.id.favourites_recycler_view) lateinit var recyclerView: RecyclerView
    @BindView(R.id.nothing_container) lateinit var nothingContainer: ViewGroup

    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mRecyclerViewAdapter: FavouriteBoardsRecyclerViewAdapter

    private lateinit var mItemDecoration: FavouriteBoardsItemDividerDecoration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_favourite_boards, container, false) as ViewGroup
        ButterKnife.bind(this, rootView)
        (activity as DashboardActivity)
                .getWishmasterApplication()
                .daggerDashboardViewComponent
                .inject(this)
        presenter.bindFavouriteBoardsView(this)

        setupRecyclerView()
        presenter.loadFavouriteBoardList()

        return rootView
    }

    private fun setupRecyclerView() {
        mRecyclerViewAdapter = FavouriteBoardsRecyclerViewAdapter(
                activity as BaseWishmasterActivity,
                this, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mRecyclerViewAdapter
        context?.let {
            mItemDecoration = FavouriteBoardsItemDividerDecoration(it)
            recyclerView.addItemDecoration(mItemDecoration)
        }

        val callback = SimpleItemTouchItemCallback(mRecyclerViewAdapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper.startDrag(viewHolder)
        mItemDecoration.onStartDrag()

    }

    override fun onItemDropped() {
        mItemDecoration.onFinishDrag()
    }

    override fun onFavouriteBoardListChanged(boardList: List<BoardModel>) {
        boardList.forEach { Log.d(LOG_TAG, it.toString() + "\n") }
        activity?.runOnUiThread({
            nothingContainer.visibility = if (boardList.isEmpty()) View.VISIBLE else View.GONE
            mRecyclerViewAdapter.bindFavouriteBoardList(boardList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindFavouriteBoardsView()
    }
}