package com.koresuniku.wishmaster_v4.core.dagger.module

import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.core.dagger.ForDashboardPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.DashboardNetworkInteractor
import com.koresuniku.wishmaster_v4.core.data.boards.BoardsMapper
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class DashboardPresenterModule {

    @Provides @ForDashboardPresenter
    fun provideDashboardNetworkInteractor(boardsApiService: BoardsApiService,
                                          boardsMapper: BoardsMapper,
                                          compositeDisposable: CompositeDisposable): DashboardNetworkInteractor {
        return DashboardNetworkInteractor(boardsApiService, boardsMapper, compositeDisposable)
    }

    @Provides @ForDashboardPresenter
    fun provideDashboardDatabaseInteractor(boardsRepository: BoardsRepository,
                                           databaseHelper: DatabaseHelper,
                                           compositeDisposable: CompositeDisposable): DashboardDatabaseInteractor {
        return DashboardDatabaseInteractor(boardsRepository, databaseHelper, compositeDisposable)
    }
}