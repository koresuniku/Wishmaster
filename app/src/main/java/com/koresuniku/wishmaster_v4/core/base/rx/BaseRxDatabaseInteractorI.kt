package com.koresuniku.wishmaster_v4.core.base.rx

import android.database.sqlite.SQLiteDatabase
import com.koresuniku.wishmaster_v4.core.base.interactor.IDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxDatabaseInteractorI<P : IMvpPresenter<*>, M>(
        private val databaseHelper: DatabaseHelper,
        compositeDisposable: CompositeDisposable) : BaseRxInteractorI<P>(compositeDisposable), IDatabaseInteractor<M> {

    override fun getWritableDatabase(): SQLiteDatabase = databaseHelper.writableDatabase
    override fun getReadableDatabase(): SQLiteDatabase = databaseHelper.readableDatabase

    override fun unbindPresenter() {
        super.unbindPresenter()
        getWritableDatabase().close()
        getReadableDatabase().close()
    }
}