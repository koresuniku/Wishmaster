package com.koresuniku.wishmaster.ui.settings

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.koresuniku.wishmaster.R

/**
 * Created by koresuniku on 2/19/18.
 */

class LicensesFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var licenseList: ListView
    private lateinit var adapter: LicenseListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_licenses, container, false)
        licenseList = rootView.findViewById(R.id.license_list)
        adapter = LicenseListAdapter()
        licenseList.adapter = adapter

        return rootView
    }
}