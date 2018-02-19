package com.koresuniku.wishmaster.ui.settings

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.base.BaseWishmasterFragment

/**
 * Created by koresuniku on 2/19/18.
 */

class LicensesFragment : Fragment() {

    lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.licences_fragment, container, false)
        return rootView
    }
}