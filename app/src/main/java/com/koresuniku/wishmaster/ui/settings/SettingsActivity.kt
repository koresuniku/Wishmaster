package com.koresuniku.wishmaster.ui.settings

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.IntentKeystore
import com.koresuniku.wishmaster.core.dagger.IWishmasterDaggerInjector
import com.koresuniku.wishmaster.core.modules.settings.ISettingsPresenter
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import com.koresuniku.wishmaster.ui.base.IWishamsterActivity
import javax.inject.Inject

/**
 * Created by koresuniku on 2/19/18.
 */

class SettingsActivity : BaseWishmasterActivity<ISettingsPresenter>() {

    @Inject override lateinit var presenter: ISettingsPresenter

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar

    override fun provideContentLayoutResource() = R.layout.activity_settings
    override fun provideFromActivityRequestCode() = IntentKeystore.FROM_SETTINGS_ACTIVITY_REQUEST_CODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWishmasterApplication().daggerSettingsViewComponent.inject(this)
        ButterKnife.bind(this)

        setupToolbar()

        fragmentManager.beginTransaction()
                .replace(R.id.preference_fragment_container, MainPreferenceFragment())
                .commit()
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}