package com.koresuniku.wishmaster_v4.ui.dashboard

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.koresuniku.wishmaster_v4.ui.dashboard.board_list.BoardListFragment
import com.koresuniku.wishmaster_v4.ui.dashboard.favourite_boards.FavouriteBoardsFragment
import com.koresuniku.wishmaster_v4.ui.dashboard.favourite_threads.FavouriteThreadsFragment
import com.koresuniku.wishmaster_v4.ui.dashboard.history.HistoryFragment

/**
* Created by koresuniku on 10.11.17.
*/

class DashboardViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val COUNT = 4

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FavouriteBoardsFragment()
            1 -> return BoardListFragment()
            2 -> return FavouriteThreadsFragment()
            3 -> return HistoryFragment()
        }
        return Fragment()
    }

    override fun getCount(): Int = COUNT


}