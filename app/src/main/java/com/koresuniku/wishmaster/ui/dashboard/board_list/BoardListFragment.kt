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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.data.model.boards.BoardListsObject
import com.koresuniku.wishmaster.core.module.dashboard.DashboardContract
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment
import com.koresuniku.wishmaster.ui.dashboard.DashboardActivity
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by koresuniku on 10.11.17.
 */

class BoardListFragment : BaseWishmasterFragment(), DashboardContract.IDashboardBoardListView {
    private val LOG_TAG = BoardListFragment::class.java.simpleName

    @Inject lateinit var presenter: DashboardContract.IDashboardPresenter
    @Inject lateinit var injector: IWishmasterDaggerInjector

    override lateinit var rootView: ViewGroup
    @BindView(R.id.board_list) lateinit var mBoardList: ExpandableListView

    private lateinit var mBoardListAdapter: BoardListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_board_list, container, false) as ViewGroup
        ButterKnife.bind(this, rootView)
        (activity as DashboardActivity)
                .getWishmasterApplication()
                .daggerDashboardViewComponent
                .inject(this)
        presenter.bindDashboardBoardListView(this)

        return rootView
    }

    override fun onBoardListsObjectReceived(boardListsObject: BoardListsObject) {
        setupBoardListAdapter(boardListsObject)
    }

    private fun setupBoardListAdapter(boardListsObject: BoardListsObject) {
        context?.let {
            mBoardListAdapter = BoardListAdapter(injector, WeakReference(it), boardListsObject)
            mBoardList.setAdapter(mBoardListAdapter)
            mBoardList.setGroupIndicator(null)
            mBoardList.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                presenter.launchThreadList(
                        boardListsObject.boardLists[groupPosition].second[childPosition].getBoardId())
                false
            }
        }
    }

    override fun onBoardFavourabilityChanged(boardId: String, newFavouritePosition: Int) {
        mBoardListAdapter.onBoardFavourabilityChanged(boardId, newFavouritePosition)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindDashboardBoardListView()
    }
}