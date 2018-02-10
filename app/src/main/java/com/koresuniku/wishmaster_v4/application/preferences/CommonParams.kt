package com.koresuniku.wishmaster_v4.application.preferences

import javax.inject.Inject

/**
 * Created by koresuniku on 2/10/18.
 */

class CommonParams @Inject constructor() {
    var cacheSize: Int = SharedPreferencesKeystore.CACHE_SIZE_DEFAULT
}