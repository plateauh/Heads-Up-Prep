package com.najed.headsup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.najed.headsup.databinding.CelebRowBinding

class RecyclerViewAdapter(private val celebs: Celeb):
    RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: CelebRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            CelebRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = celebs[position]
        holder.binding.apply {
            celebNameTv.text = item.name
            taboo1Tv.text = item.taboo1
            taboo2Tv.text = item.taboo2
            taboo3Tv.text = item.taboo3
        }
    }

    override fun getItemCount() = celebs.size
}

