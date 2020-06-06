package com.enanek.model.response

data class PhotosResponse(
    val photos: Photos,
    val stat: String
)

data class Photos(
    val page: String,
    val pages: Int,
    val perpage: String,
    val photo: MutableList<Photo>,
    val total: Int
)

data class Photo(
    val farm: Int,
    val has_comment: Int,
    val id: String,
    val is_primary: Int,
    val isfamily: Int,
    val isfriend: Int,
    val ispublic: Int,
    val owner: String,
    val secret: String,
    val server: String,
    val title: String
)