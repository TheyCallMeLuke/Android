package com.example.flickrreranker.model

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.flickrreranker.R
import com.example.flickrreranker.app.ui.photo.PhotoRecyclerViewAdapter
import com.example.flickrreranker.domain.Photo
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("NotifyDataSetChanged")
@BindingAdapter("listData")
fun bindSearchRecyclerView(recyclerView: RecyclerView, data: List<Photo>?) {
    if (recyclerView.adapter == null || data?.isEmpty()!!) return
    val adapter = recyclerView.adapter as PhotoRecyclerViewAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        imgView.load(imgUrl) {
            placeholder(R.drawable.ic_loading_img)
            error(R.drawable.ic_broken_image)
        }
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
            statusImgView.setImageResource(R.drawable.ic_connection_error)
        }
        PhotoApiStatus.DONE -> {
            statusImgView.visibility = View.GONE
        }
    }
}

@BindingAdapter("errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String) {
    view.error = errorMessage
}