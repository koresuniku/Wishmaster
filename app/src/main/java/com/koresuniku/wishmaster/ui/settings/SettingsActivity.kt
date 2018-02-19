package com.koresuniku.wishmaster.ui.settings

import android.os.Bundle
import android.support.v7.widget.Toolbar
import butterknife.BindView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.IntentKeystore
import com.koresuniku.wishmaster.core.modules.settings.ISettingsPresenter
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
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

        setupToolbar()

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, MainPreferenceFragment())
                .commit()
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}