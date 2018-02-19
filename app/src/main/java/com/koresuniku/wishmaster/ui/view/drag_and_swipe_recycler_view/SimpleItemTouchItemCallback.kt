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

package com.koresuniku.wishmaster.ui.view.drag_and_swipe_recycler_view

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Created by koresuniku on 13.11.17.
 */

class SimpleItemTouchItemCallback(private val adapter: ItemTouchHelperAdapter) :
        android.support.v7.widget.helper.ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        adapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemRemoved(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        adapter.onSelectedChanged(actionState)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)
        adapter.onItemDropped()
    }
}