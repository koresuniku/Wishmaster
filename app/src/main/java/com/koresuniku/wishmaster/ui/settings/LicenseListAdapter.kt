package com.koresuniku.wishmaster.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.Licenses

/**
 * Created by koresuniku on 2/20/18.
 */

class LicenseListAdapter : BaseAdapter() {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView = p1
        if (p1 == null) {
            convertView = LayoutInflater.from(p2?.context).inflate(R.layout.license_item, p2, false)
        }

        val name = convertView?.findViewById<TextView>(R.id.name)
        val desc = convertView?.findViewById<TextView>(R.id.description)

        name?.text = Licenses.names[p0]
        desc?.text = Licenses.descriptions[p0]

        return convertView ?: View(p2?.context)
    }

    override fun getItem(p0: Int) = Licenses.names

    override fun getItemId(p0: Int) = p0.toLong()
    override fun getCount() = Licenses.names.size
}