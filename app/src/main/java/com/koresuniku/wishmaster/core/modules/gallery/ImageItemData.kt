package com.koresuniku.wishmaster.core.modules.gallery

import com.koresuniku.wishmaster.core.data.model.threads.File

/**
 * Created by koresuniku on 18.01.18.
 */

data class ImageItemData(val file: File,
                         val dimensions: ImageLayoutDimensions,
                         val summary: String)