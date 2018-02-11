package com.koresuniku.wishmaster.core.base.rx

import android.database.sqlite.SQLiteDatabase
import com.koresuniku.wishmaster.core.base.interactor.IDatabaseInteractor
import com.koresuniku.wishmaster.core.base.mvp.IMvpPresenter
import com.koresuniku.wishmaster.core.data.database.DatabaseHelper
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxDatabaseInteractor<P : IMvpPresenter<*>, M>(
        private val databaseHelper: DatabaseHelper,
        compositeDisposable: CompositeDisposable) : BaseRxInteractor<P>(compositeDisposable), IDatabaseInteractor<M> {

    override fun getWritableDatabase(): SQLiteDatabase = databaseHelper.writableDatabase
    override fun getReadableDatabase(): SQLiteDatabase = databaseHelper.readableDatabase

    override fun unbindPresenter() {
        super.unbindPresenter()
        getWritableDatabase().close()
        getReadableDatabase().close()
    }
}