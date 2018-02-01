package com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import dagger.Module
import dagger.Provides

@Module
class BoardsModule {

    @Provides
    @ForDashboardPresenter
    fun provideBoardsRepository(): BoardsRepository = BoardsRepository()
}