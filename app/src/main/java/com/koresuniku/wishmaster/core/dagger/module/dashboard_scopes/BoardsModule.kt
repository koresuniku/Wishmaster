package com.koresuniku.wishmaster.core.dagger.module.dashboard_scopes

import com.koresuniku.wishmaster.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster.core.data.database.repository.BoardsRepository
import dagger.Module
import dagger.Provides

@Module
class BoardsModule {

    @Provides
    @ForDashboardPresenter
    fun provideBoardsRepository(): BoardsRepository = BoardsRepository()
}