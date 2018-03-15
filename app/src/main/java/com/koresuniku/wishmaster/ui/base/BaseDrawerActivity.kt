/*
 * Copyright (c) 2018 koresuniku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.koresuniku.wishmaster.ui.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R

/**
 * Created by koresuniku on 03.10.17.
 */

abstract class BaseDrawerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContentView()
    }

    private fun setupContentView() {
        val mainLayout = LayoutInflater.from(this)
                .inflate(R.layout.activity_drawer, null, false)
        val contentContainer = mainLayout.findViewById<View>(R.id.content_container) as ViewGroup
        val contentLayout = LayoutInflater.from(this)
                .inflate(provideContentLayoutResource(), null, false)

        contentContainer.addView(contentLayout)
        setContentView(mainLayout)
    }

    @LayoutRes abstract fun provideContentLayoutResource(): Int
}