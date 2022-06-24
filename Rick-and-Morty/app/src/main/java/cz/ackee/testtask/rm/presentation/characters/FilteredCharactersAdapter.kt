package cz.ackee.testtask.rm.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cz.ackee.testtask.rm.databinding.FilteredCharacterBinding
import cz.ackee.testtask.rm.domain.Character

class FilteredCharactersAdapter(private val onItemClicked: (Character) -> Unit) :
    PagingDataAdapter<Character, FilteredCharactersAdapter.FilteredCharacterViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FilteredCharactersAdapter.FilteredCharacterViewHolder {
        val binding = FilteredCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = FilteredCharacterViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            getItem(position)?.let { it1 -> onItemClicked(it1) }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FilteredCharacterViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class FilteredCharacterViewHolder(private val binding: FilteredCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.character = character
            binding.executePendingBindings()
        }
    }
}