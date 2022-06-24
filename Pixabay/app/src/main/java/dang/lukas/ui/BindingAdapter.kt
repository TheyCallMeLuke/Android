package dang.lukas.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import dang.lukas.R
import dang.lukas.domain.Photo
import dang.lukas.ui.photos.PhotoApiStatus
import dang.lukas.ui.photos.PhotosAdapter

@SuppressLint("NotifyDataSetChanged")
@BindingAdapter("listData")
fun bindSearchRecyclerView(recyclerView: RecyclerView, data: List<Photo>?) {
    if (recyclerView.adapter == null || data?.isEmpty()!!) return
    val adapter = recyclerView.adapter as PhotosAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_img)
        }
    }
}

@BindingAdapter("photoCountText")
fun bindPhotoCountText(textView: TextView, photoCount: Int) {
    val resources = textView.context.resources
    if (photoCount == 0) {
        textView.text = resources.getString(R.string.no_results)
    } else {
        textView.text = resources.getQuantityString(
            R.plurals.photo_count,
            photoCount,
            photoCount
        )
    }
}

@BindingAdapter("photoApiStatus")
fun bindStatus(statusImgView: ImageView, status: PhotoApiStatus?) {
    when (status) {
        PhotoApiStatus.LOADING -> {
            statusImgView.visibility = View.VISIBLE
            statusImgView.setImageResource(R.drawable.loading_animation)
        }
        PhotoApiStatus.ERROR -> {
            statusImgView.visibility = View.VISIBLE
            statusImgView.setImageResource(R.drawable.ic_connection_error_img)
        }
        PhotoApiStatus.DONE -> {
            statusImgView.visibility = View.GONE
        }
    }
}
