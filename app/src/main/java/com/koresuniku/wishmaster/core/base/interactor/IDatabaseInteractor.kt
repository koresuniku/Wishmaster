package com.koresuniku.wishmaster.core.base.interactor

import android.database.sqlite.SQLiteDatabase
import io.reactivex.Single


interface IDatabaseInteractor<M> {

    fun getWritableDatabase(): SQLiteDatabase
    fun getReadableDatabase(): SQLiteDatabase
    fun getDataFromDatabase(): Single<M>
}