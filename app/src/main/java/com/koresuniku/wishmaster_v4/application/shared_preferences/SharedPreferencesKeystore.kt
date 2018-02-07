package com.koresuniku.wishmaster_v4.application.shared_preferences

import com.koresuniku.wishmaster_v4.core.network.Dvach

/**
 * Created by koresuniku on 12.11.17.
 */

object SharedPreferencesKeystore {
    //Main preferences name
    const val SHARED_PREFERENCES_NAME = "sharedPreferences"

    //Base URL
    const val BASE_URL_KEY = "base_url_key"
    val BASE_URL_DEFAULT = Dvach.BASE_URL

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
}