package com.koresuniku.wishmaster_v4.ui.dashboard.board_list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.dashboard.BoardListView
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.IDashboardPresenter
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListData
import com.koresuniku.wishmaster_v4.core.data.boards.BoardListsObject
import com.koresuniku.wishmaster_v4.core.data.boards.BoardsMapper
import com.koresuniku.wishmaster_v4.ui.dashboard.DashboardActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by koresuniku on 10.11.17.
 */

class BoardListFragment : Fragment(), BoardListView {
    private val LOG_TAG = BoardListFragment::class.java.simpleName

    @Inject lateinit var presenter: IDashboardPresenter

    private lateinit var mRootView: View
    @BindView(R.id.board_list) lateinit var mBoardList: ExpandableListView

    private lateinit var mBoardListAdapter: BoardListAdapter
   // private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_board_list, container, false)
        ButterKnife.bind(this, mRootView)
        (activity as DashboardActivity)
                .getWishmasterApplication()
                .getDashboardViewComponent()
                .inject(this)
        presenter.bindDashboardBoardListView(this)

       // mCompositeDisposable = CompositeDisposable()
        //loadBoards()

        return mRootView
    }

//    fun onBoardListReceived(boardListData: BoardListData) {
//        val boardLists = BoardsMapper.mapToBoardsDataByCategory(boardListData)
//        activity?.runOnUiThread { setupBoardListAdapter(boardLists) }
//    }

    override fun onBoardListsObjectReceived(boardListsObject: BoardListsObject) {
        setupBoardListAdapter(boardListsObject)
    }

//    private fun loadBoards() {
//        mCompositeDisposable.add(presenter.getLoadBoardsObservable()
//                .subscribeOn(Schedulers.newThread())
//                .map(BoardsMapper::mapToBoardsDataByCategory)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::setupBoardListAdapter, { e -> e.printStackTrace() }))
//    }

    private fun setupBoardListAdapter(boardListsObject: BoardListsObject) {
        context?.let {
            mBoardListAdapter = BoardListAdapter(WeakReference(it), boardListsObject, presenter)
            mBoardList.setAdapter(mBoardListAdapter)
            mBoardList.setGroupIndicator(null)
            mBoardList.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                presenter.shouldLaunchThreadListActivity(boardListsObject.boardLists[groupPosition].second[childPosition].getBoardId())
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
        //mCompositeDisposable.clear()
    }
}