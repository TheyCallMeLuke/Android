package com.example.flickrreranker.app.ui.photo

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.flickrreranker.app.ui.helper.toDateString

/**
 * The dialog fragment for a photo.
 */
class PhotoDialogFragment : DialogFragment() {

    /**
     * Sets the dialog message containing information about the photo.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val author = arguments?.getString(AUTHOR)
        val unixDate = arguments?.getLong(DATE)?.times(1000)
        val latitude = arguments?.getDouble(LATITUDE)
        val longitude = arguments?.getDouble(LONGITUDE)
        val viewCount = arguments?.getInt(VIEWS)
        return AlertDialog.Builder(requireContext())
            .setMessage(
                "Author: $author\n" +
                        "Date: ${unixDate?.toDateString()}\n" +
                        "Latitude: $latitude\n" +
                        "Longitude: $longitude\n" +
                        "Views: $viewCount\n"
            )
            .setPositiveButton("Close") { _, _ -> }
            .create()
    }

    /**
     * Factory method that accepts the photo parameters and saves them into a bundle for later usage.
     */
    companion object {
        private const val AUTHOR = "author_key"
        private const val DATE = "date_key"
        private const val LATITUDE = "latitude_key"
        private const val LONGITUDE = "longitude_key"
        private const val VIEWS = "views_key"

        fun newInstance(
            author: String,
            dateInUnixMillis: Long,
            latitude: Double,
            longitude: Double,
            views: Int
        ) = PhotoDialogFragment().apply {
            arguments = bundleOf(
                AUTHOR to author,
                DATE to dateInUnixMillis,
                LATITUDE to latitude,
                LONGITUDE to longitude,
                VIEWS to views
            )
        }
    }


}