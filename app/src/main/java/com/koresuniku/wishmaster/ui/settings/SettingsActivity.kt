package com.koresuniku.wishmaster.ui.settings

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.IntentKeystore
import com.koresuniku.wishmaster.core.modules.settings.ISettingsPresenter
import com.koresuniku.wishmaster.ui.base.BaseWishmasterActivity
import javax.inject.Inject

/**
 * Created by koresuniku on 2/19/18.
 */

class SettingsActivity : BaseWishmasterActivity<ISettingsPresenter>(), MainPreferenceFragment.Callback {

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
                .replace(
                        R.id.preference_fragment_container,
                        MainPreferenceFragment(),
                        getString(R.string.settings_text))
                .commit()
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mToolbar.post { mToolbar.title = getString(R.string.settings_text) }
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

    override fun onBackPressed() {
         if (supportFragmentManager.fragments.size > 1) {
             mToolbar.title = supportFragmentManager.fragments.last().tag
         } else {
             mToolbar.title = getString(R.string.settings_text)
         }
        super.onBackPressed()
    }

    override fun onNestedPreferenceSelected(key: String) {
        var title = String()
        when (key) {
            getString(R.string.licences_key) -> {
                title = getString(R.string.licences_title)
                addFragment(LicensesFragment(), title)
            }
        }
        mToolbar.title = title
        Log.d("SA", "onNested tag: $title")
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.animator.from_right, R.animator.to_left,
                        R.animator.from_left, R.animator.to_right)
                .replace(R.id.preference_fragment_container, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }
}