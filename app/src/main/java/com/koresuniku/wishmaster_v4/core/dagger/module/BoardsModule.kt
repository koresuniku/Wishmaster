package com.koresuniku.wishmaster_v4.core.dagger.module

import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster_v4.core.data.boards.BoardsMapper
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import dagger.Module
import dagger.Provides

@Module
class BoardsModule {

    @Provides
    @ForDashboardPresenter
    fun provideBoardsMapper(): BoardsMapper = BoardsMapper()

    @Provides
    @ForDashboardPresenter
    fun provideBoardsRepository(boardsMapper: BoardsMapper): BoardsRepository = BoardsRepository(boardsMapper)
}