package com.koresuniku.wishmaster.core.network.github_api

import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import android.util.Log
import com.koresuniku.wishmaster.BuildConfig


/**
 * Created by koresuniku on 2/18/18.
 */

class GithubHelper @Inject constructor(private val networkInteractor: GithubReleaseListNetworkInteractor,
                                       private val compositeDisposable: CompositeDisposable) {

    companion object {
        const val GITHUB_BASE_URL = "https://api.github.com"
    }

    fun checkForNewRelease(): Maybe<WishmasterRelease> {
        return Maybe.create { e ->
            compositeDisposable.add(
                    networkInteractor
                            .getDataFromNetwork()
                            .subscribe({
                                Log.d("GH", "releases count: ${it.size}")
                                when {
                                    it.isEmpty() -> e.onComplete()
                                    compareVersionNames(BuildConfig.VERSION_NAME, it[0].tagName) -> e.onSuccess(it[0])
                                    else -> e.onComplete()
                                }
                            }, { it.printStackTrace()}))
        }
    }

    private fun compareVersionNames(current: String, latest: String): Boolean {
        val c = current.replace(Regex("[^0-9]+"), "")
        val l = latest.replace(Regex("[^0-9]+"), "")
        l.zip(c).forEach { if (it.first.toInt() > it.second.toInt()) return true }
        return false
    }
}