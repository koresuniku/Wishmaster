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

package com.koresuniku.wishmaster.ui.dashboard

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.koresuniku.wishmaster.ui.dashboard.board_list.BoardListFragment
import com.koresuniku.wishmaster.ui.dashboard.favourite_boards.FavouriteBoardsFragment
import com.koresuniku.wishmaster.ui.dashboard.favourite_threads.FavouriteThreadsFragment
import com.koresuniku.wishmaster.ui.dashboard.history.HistoryFragment

/**
* Created by koresuniku on 10.11.17.
*/

class DashboardViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private  val COUNT = 4

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