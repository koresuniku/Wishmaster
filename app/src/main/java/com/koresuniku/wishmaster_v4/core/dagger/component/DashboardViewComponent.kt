package com.koresuniku.wishmaster_v4.core.dagger.component

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardView
import com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope.DashboardViewModule
import com.koresuniku.wishmaster_v4.ui.dashboard.DashboardActivity
import com.koresuniku.wishmaster_v4.ui.dashboard.board_list.BoardListFragment
import com.koresuniku.wishmaster_v4.ui.dashboard.favourite_boards.FavouriteBoardsFragment
import dagger.Component

/**
 * Created by koresuniku on 05.10.17.
 */

@ForDashboardView
@Component (dependencies = [DashboardPresenterComponent::class], modules = [DashboardViewModule::class])
interface DashboardViewComponent {

    fun inject(activity: DashboardActivity)
    fun inject(boardListFragment: BoardListFragment)
    fun inject(favouriteBoardsFragment: FavouriteBoardsFragment)
}