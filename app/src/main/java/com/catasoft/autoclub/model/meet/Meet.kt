package com.catasoft.autoclub.model.meet

import android.net.Uri
import java.io.Serializable

data class Meet(
    var id: String? = null,
    var placeId: String? = null,
    var placeName: String? = null,
    var placePhotoLink: String? = null,
    var description: String? = null,
    var placeLong: Double? = null,
    var placeLat: Double? = null,
    var placeAddress: String? = null,
    var ownerUid: String? = null,
    var ownerPhotoLink: String? = null,
    var creationTime: Long? = null,
    var meetTime: Long? = null
): Serializable
