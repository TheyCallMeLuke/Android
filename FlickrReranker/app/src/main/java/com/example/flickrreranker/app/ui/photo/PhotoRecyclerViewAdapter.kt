package com.example.flickrreranker.app.ui.photo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flickrreranker.databinding.FragmentPhotoBinding
import com.example.flickrreranker.domain.Photo

/**
 * Recycler view for the photo list. A list adapter is used for better performance.
 */
class PhotoRecyclerViewAdapter(val fragmentManager: FragmentManager) :
    ListAdapter<Photo, PhotoRecyclerViewAdapter.PhotosViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            FragmentPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * Other than binding a photo instance to the view holder, we set the onClickListener which
     * opens a dialog fragment containing additional information about a photo.
     */
    inner class PhotosViewHolder(private val binding: FragmentPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.photo = photo
            binding.photoCard.setOnClickListener {
                PhotoDialogFragment.newInstance(
                    photo.author,
                    photo.date,
                    photo.latitude,
                    photo.longitude,
                    photo.viewCount
                ).show(fragmentManager, null)
            }
            binding.executePendingBindings()
        }
    }
}