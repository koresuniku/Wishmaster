package com.koresuniku.wishmaster_v4.ui.dashboard.favourite_boards

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster_v4.R
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.FavouriteBoardsView
import com.koresuniku.wishmaster_v4.core.data.boards.BoardModel
import com.koresuniku.wishmaster_v4.ui.dashboard.DashboardActivity
import com.koresuniku.wishmaster_v4.ui.view.drag_and_swipe_recycler_view.OnStartDragListener
import com.koresuniku.wishmaster_v4.ui.view.drag_and_swipe_recycler_view.SimpleItemTouchItemCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by koresuniku on 10.11.17.
 */

class FavouriteBoardsFragment : Fragment(), OnStartDragListener, FavouriteBoardsView {
    private val LOG_TAG = FavouriteBoardsFragment::class.java.simpleName

    @Inject lateinit var presenter: DashboardPresenter

    private lateinit var mRootView: View
    @BindView(R.id.favourites_recycler_view) lateinit var recyclerView: RecyclerView
    @BindView(R.id.nothing_container) lateinit var nothingContainer: ViewGroup

    private lateinit var mCompositeDisposable: CompositeDisposable
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mRecyclerViewAdapter: FavouriteBoardsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_favourite_boards, container, false)
        ButterKnife.bind(this, mRootView)
        (activity as DashboardActivity)
                .getWishmasterApplication()
                .getDashBoardComponent()
                .inject(this)
        presenter.bindFavouriteBoardsView(this)

        mCompositeDisposable = CompositeDisposable()

        initRecyclerView()
        loadFavouriteBoardsList()

        return mRootView
    }

    private fun initRecyclerView() {
        mRecyclerViewAdapter = FavouriteBoardsRecyclerViewAdapter(
                this, presenter, mCompositeDisposable)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mRecyclerViewAdapter
        context?.let { recyclerView.addItemDecoration(FavouriteBoardsItemDividerDecoration(it)) }

        val callback = SimpleItemTouchItemCallback(mRecyclerViewAdapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun loadFavouriteBoardsList() {
        mCompositeDisposable.add(presenter.loadFavouriteBoardsList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { boardList ->
                    boardList.forEach { Log.d(LOG_TAG, it.toString() + "\n") }
                    nothingContainer.visibility = if (boardList.isEmpty()) View.VISIBLE else View.GONE
                    mRecyclerViewAdapter.bindFavouriteBoardList(boardList)
                }))
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper.startDrag(viewHolder)
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
        mCompositeDisposable.clear()
        presenter.unbindFavouriteBoardsView()
    }
}