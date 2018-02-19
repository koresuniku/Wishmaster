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

package com.koresuniku.wishmaster.ui.dashboard.favourite_boards

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.koresuniku.wishmaster.R

/**
 * Created by koresuniku on 13.11.17.
 */

class FavouriteBoardsRecyclerViewViewHolder(mItemView: View) : RecyclerView.ViewHolder(mItemView) {
    @BindView(R.id.board_name) lateinit var mBoardName: TextView
    @BindView(R.id.drag_and_drop) lateinit var mDragAndDrop: ImageView

    init {
        ButterKnife.bind(this, mItemView)
    }

}