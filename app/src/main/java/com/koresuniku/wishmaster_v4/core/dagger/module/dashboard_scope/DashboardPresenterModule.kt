package com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scope

import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.application.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardDatabaseInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardNetworkInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSearchInteractorI
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSharedPreferencesInteractorI
import com.koresuniku.wishmaster_v4.core.network.boards_api.BoardsResponseParser
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import com.koresuniku.wishmaster_v4.core.data.database.repository.BoardsRepository
import com.koresuniku.wishmaster_v4.core.util.search.ISearchInputMatcher
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class DashboardPresenterModule {

    @Provides
    @ForDashboardPresenter
    fun provideDashboardNetworkInteractor(boardsApiService: BoardsApiService,
                                          responseParser: BoardsResponseParser,
                                          compositeDisposable: CompositeDisposable): DashboardNetworkInteractorI {
        return DashboardNetworkInteractorI(boardsApiService, responseParser, compositeDisposable)
    }

    @Provides
    @ForDashboardPresenter
    fun provideDashboardDatabaseInteractor(boardsRepository: BoardsRepository,
                                           databaseHelper: DatabaseHelper,
                                           compositeDisposable: CompositeDisposable): DashboardDatabaseInteractorI {
        return DashboardDatabaseInteractorI(boardsRepository, databaseHelper, compositeDisposable)
    }

    @Provides
    @ForDashboardPresenter
    fun provideDashboardSearchInteractor(matcher: ISearchInputMatcher,
                                         compositeDisposable: CompositeDisposable): DashboardSearchInteractorI {
        return DashboardSearchInteractorI(matcher, compositeDisposable)
    }

    @Provides
    @ForDashboardPresenter
    fun provideDashboardSharedPreferencesInteractor(storage: ISharedPreferencesStorage,
                                                    compositeDisposable: CompositeDisposable): DashboardSharedPreferencesInteractorI {
        return DashboardSharedPreferencesInteractorI(storage, compositeDisposable)
    }
}