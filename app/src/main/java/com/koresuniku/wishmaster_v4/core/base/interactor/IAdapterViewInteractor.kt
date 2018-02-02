package com.koresuniku.wishmaster_v4.core.base.interactor

/**
 * Created by koresuniku on 02.02.18.
 */
interface IAdapterViewInteractor<A, V, D> {

    fun setItemViewData(adapterView: A, view: V, data: D, position: Int)
}