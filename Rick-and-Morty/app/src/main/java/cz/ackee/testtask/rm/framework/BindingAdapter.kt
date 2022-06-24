package cz.ackee.testtask.rm.framework

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cz.ackee.testtask.rm.R
import cz.ackee.testtask.rm.domain.Character
import cz.ackee.testtask.rm.presentation.favorites.FavoriteCharactersAdapter

@SuppressLint("NotifyDataSetChanged")
@BindingAdapter("listData")
fun bindSearchRecyclerView(recyclerView: RecyclerView, data: List<Character>?) {
    if (recyclerView.adapter == null || data == null) return
    if (recyclerView.adapter is FavoriteCharactersAdapter) {
        val adapter = recyclerView.adapter as FavoriteCharactersAdapter
        adapter.submitList(data)
        adapter.notifyDataSetChanged()
    }
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