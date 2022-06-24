package dang.lukas.domain

typealias Photos = MutableList<Photo>

data class Photo(
    val id: Long,
    val tags: String,
    val photo_url: String,
    val user: String,
    val views: String,
    val downloads: String,
    val likes: String,
    val comments: String,
    val profilePictureUrl: String,
    val pageUrl: String,
    val userId: Long
) {
    val profileUrl: String = "https://pixabay.com/users/${user}-${userId}/"
}