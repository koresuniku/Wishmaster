package com.koresuniku.wishmaster_v4.core.base.rx

import android.database.sqlite.SQLiteDatabase
import com.koresuniku.wishmaster_v4.core.base.IDatabaseInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.BaseInteractor
import com.koresuniku.wishmaster_v4.core.base.mvp.MvpPresenter
import com.koresuniku.wishmaster_v4.core.data.database.DatabaseHelper
import io.reactivex.disposables.CompositeDisposable


abstract class BaseRxDatabaseInteractor<P : MvpPresenter<*>, M>(
        private val databaseHelper: DatabaseHelper,
        compositeDisposable: CompositeDisposable) : BaseRxInteractor<P>(compositeDisposable), IDatabaseInteractor<M> {

    override fun getWritableDatabase(): SQLiteDatabase = databaseHelper.writableDatabase
    override fun getReadableDatabase(): SQLiteDatabase = databaseHelper.readableDatabase

}