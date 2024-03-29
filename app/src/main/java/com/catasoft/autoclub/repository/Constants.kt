package com.catasoft.autoclub.repository

object Constants {
    const val COLLECTION_USERS = "users"
    const val USERS_UID = "uid"
    const val USERS_NUMBER_PLATE = "numberPlate"
    const val USERS_JOIN_DATE = "joinDate"
    const val USERS_NAME = "name"
    const val USERS_NORMALIZED_NAME = "normalizedName"
    const val USERS_CARS_COUNT = "carsCount"
    const val USERS_MEETS_COUNT = "postsCount"
    const val USERS_FACEBOOK_PROFILE = "facebookProfile"
    const val FOLLOWERS_COUNT = "followersCount"

    const val COLLECTION_CARS = "cars"
    const val CARS_ID = "id"
    const val CARS_NUMBER_PLATE = "numberPlate"
    const val CARS_MAKE = "make"
    const val CARS_MODEL = "model"
    const val CARS_OWNER_UID = "ownerUid"
    const val CARS_PHOTOS_URI_ARRAY = "photosUri"
    const val CARS_AVATAR_URI = "avatarUri"
    const val CARS_DESCRIPTION = "description"

    const val COLLECTION_MEETS = "meets"
    const val MEETS_ID = "id"
    const val MEETS_OWNER_UID = "ownerUid"
    const val MEETS_CREATION_TIME = "creationTime"
    const val MEETS_MEET_TIME = "meetTime"
}