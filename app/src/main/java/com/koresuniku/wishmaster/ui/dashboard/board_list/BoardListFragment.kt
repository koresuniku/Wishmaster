package com.koresuniku.wishmaster.ui.dashboard.board_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.core.modules.dashboard.view.BoardListView
import com.koresuniku.wishmaster.core.modules.dashboard.presenter.IDashboardPresenter
import com.koresuniku.wishmaster.core.data.model.boards.BoardListsObject
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment
import com.koresuniku.wishmaster.ui.dashboard.DashboardActivity
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by koresuniku on 10.11.17.
 */

class BoardListFragment : BaseWishmasterFragment(), BoardListView<IDashboardPresenter> {
    private val LOG_TAG = BoardListFragment::class.java.simpleName

    @Inject override lateinit var presenter: IDashboardPresenter

    override lateinit var rootView: View
    @BindView(R.id.board_list) lateinit var mBoardList: ExpandableListView

    private lateinit var mBoardListAdapter: BoardListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_board_list, container, false)
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
            mBoardListAdapter = BoardListAdapter(WeakReference(it), boardListsObject, presenter)
            mBoardList.setAdapter(mBoardListAdapter)
            mBoardList.setGroupIndicator(null)
            mBoardList.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                presenter.shouldLaunchThreadListActivity(
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