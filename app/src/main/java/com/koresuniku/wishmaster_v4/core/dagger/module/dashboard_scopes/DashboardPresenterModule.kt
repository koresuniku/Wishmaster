package com.koresuniku.wishmaster_v4.core.dagger.module.dashboard_scopes

import com.koresuniku.wishmaster.domain.boards_api.BoardsApiService
import com.koresuniku.wishmaster_v4.application.shared_preferences.ISharedPreferencesStorage
import com.koresuniku.wishmaster_v4.core.dagger.scope.ForDashboardPresenter
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardNetworkInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSearchInteractor
import com.koresuniku.wishmaster_v4.core.dashboard.interactor.DashboardSharedPreferencesInteractor
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
                                          compositeDisposable: CompositeDisposable): DashboardNetworkInteractor {
        return DashboardNetworkInteractor(boardsApiService, responseParser, compositeDisposable)
    }

    @Provides
    @ForDashboardPresenter
    fun provideDashboardDatabaseInteractor(boardsRepository: BoardsRepository,
                                           databaseHelper: DatabaseHelper,
                                           compositeDisposable: CompositeDisposable): DashboardDatabaseInteractor {
        return DashboardDatabaseInteractor(boardsRepository, databaseHelper, compositeDisposable)
    }

    @Provides
    @ForDashboardPresenter
    fun provideDashboardSearchInteractor(matcher: ISearchInputMatcher,
                                         compositeDisposable: CompositeDisposable): DashboardSearchInteractor {
        return DashboardSearchInteractor(matcher, compositeDisposable)
    }

    @Provides
    @ForDashboardPresenter
    fun provideDashboardSharedPreferencesInteractor(storage: ISharedPreferencesStorage,
                                                    compositeDisposable: CompositeDisposable): DashboardSharedPreferencesInteractor {
        return DashboardSharedPreferencesInteractor(storage, compositeDisposable)
    }
}