package com.lemonade.listingapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lemonade.listingapp.databinding.RowListingItemBinding
import com.lemonade.listingapp.models.ListingItem

class ListingAdapter(private val clickListener: (ListingItem) -> Unit) :
    ListAdapter<ListingItem, ListingItemViewHolder>(ListingItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingItemViewHolder {
        return ListingItemViewHolder(
            RowListingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListingItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                clickListener(item)
            }
        }
    }

}

class ListingItemViewHolder(
    private val binding: RowListingItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ListingItem) {
        binding.apply {
            this.item = item
            executePendingBindings()
        }
    }
}


private class ListingItemDiffCallback : DiffUtil.ItemCallback<ListingItem>() {
    override fun areItemsTheSame(oldItem: ListingItem, newItem: ListingItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ListingItem, newItem: ListingItem): Boolean {
        return oldItem == newItem
    }
}
