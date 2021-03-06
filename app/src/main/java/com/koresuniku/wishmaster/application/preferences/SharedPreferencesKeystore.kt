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

package com.koresuniku.wishmaster.application.preferences

import com.koresuniku.wishmaster.core.data.network.Dvach

/**
 * Created by koresuniku on 12.11.17.
 */

object SharedPreferencesKeystore {
    //Main preferences name
    const val SHARED_PREFERENCES_NAME = "sharedPreferences"

    //Base URL
    const val BASE_URL_KEY = "base_url_key"
    val BASE_URL_DEFAULT = Dvach.BASE_URL

    //ACRA
    const val ENABLE_SEND_REPORTS_KEY = "enable_send_reports"
    const val ENABLE_SEND_REPORTS_DEFAULT = true

    //Cache (default 10 Mb)
    const val CACHE_SIZE_KEY = "cache_size"
    const val CACHE_SIZE_DEFAULT = 10 * 1024 * 1024

    //Dashboard
    const val DASHBOARD_PREFERRED_TAB_POSITION_KEY = "dashboard_preferred_tab_position"
    const val DASHBOARD_PREFERRED_TAB_POSITION_DEFAULT = 1

    //Images
    const val DEFAULT_IMAGE_WIDTH_IN_DP_KEY = "default_image_width_in_dp"
    const val DEFAULT_IMAGE_WIDTH_IN_DP_DEFAULT = 0

    const val MINIMUM_IMAGE_HEIGHT_IN_DP_KEY = "minimum_image_height_in_dp"
    const val MINIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT = 20

    const val MAXIMUM_IMAGE_HEIGHT_IN_DP_KEY = "maximum_image_height_in_dp"
    const val MAXIMUM_IMAGE_HEIGHT_IN_DP_DEFAULT = 150

    //Text
    const val THREAD_POST_ITEM_WIDTH_HORIZONTAL_IN_PX_KEY = "thread_post_item_width_horizontal_in_px"
    const val THREAD_POST_ITEM_WIDTH_VERTICAL_IN_PX_KEY = "thread_post_item_width_vertical_in_px"
    const val THREAD_POST_ITEM_WIDTH_DEFAULT = 0

    const val THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_HORIZONTAL_IN_PX_KEY = "thread_post_item_width_single_image_horizontal_in_px"
    const val THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_VERTICAL_IN_PX_KEY = "thread_post_item_width_single_image_vertical_in_px"
    const val THREAD_POST_ITEM_WIDTH_SINGLE_IMAGE_DEFAULT = 0

    const val THREAD_POST_ITEM_SHORT_INFO_HEIGHT_IN_PX_KEY = "thread_post_item_short_info_height_in_px"
    const val THREAD_POST_ITEM_SHORT_INFO_HEIGHT_DEFAULT = 0

    const val COMMENT_MAX_MAX_LINES_KEY = "comment_max_lines"
    const val COMMENT_MAX_MAX_LINES_DEFAULT = 8

    const val COMMENT_TEXT_SIZE_KEY = "comment_text_size"
    const val COMMENT_TEXT_SIZE_DEFAULT = 14
}