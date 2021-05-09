package com.catasoft.autoclub.model.meet

import android.net.Uri

data class MeetDisplayModel(
    var placeId: String? = null,
    var placeName: String? = null,
    var placePhotoUri: Uri? = null,
    var description: String? = null,
    var placeLat: Double? = null,
    var placeLong: Double? = null
)
