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

package com.koresuniku.wishmaster.core.module.dashboard

import com.koresuniku.wishmaster.ui.dashboard.DashboardActivity
import com.koresuniku.wishmaster.ui.dashboard.board_list.BoardListAdapter
import com.koresuniku.wishmaster.ui.dashboard.board_list.BoardListFragment
import com.koresuniku.wishmaster.ui.dashboard.favourite_boards.FavouriteBoardsFragment
import com.koresuniku.wishmaster.ui.dashboard.favourite_boards.FavouriteBoardsRecyclerViewAdapter
import dagger.Component

/**
 * Created by koresuniku on 05.10.17.
 */

@DashboardScopes.ForDashboardView
@Component (
        dependencies = [DashboardPresenterComponent::class],
        modules = [(DashboardViewModule::class)])
interface DashboardViewComponent {

    fun inject(activity: DashboardActivity)
    fun inject(boardListFragment: BoardListFragment)
    fun inject(favouriteBoardsFragment: FavouriteBoardsFragment)
    fun inject(favouriteBoardsRecyclerViewAdapter: FavouriteBoardsRecyclerViewAdapter)
    fun inject(boardListAdapter: BoardListAdapter)

}