package dang.lukas.ui.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dang.lukas.databinding.PhotoBinding
import dang.lukas.domain.Photo

class PhotosAdapter(private val onItemClicked: (Photo) -> Unit) :
    ListAdapter<Photo, PhotosAdapter.PhotosViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.photo_url == newItem.photo_url
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val viewHolder = PhotosViewHolder(
            PhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class PhotosViewHolder(private val binding: PhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.photo = photo
            binding.executePendingBindings()
        }
    }
}