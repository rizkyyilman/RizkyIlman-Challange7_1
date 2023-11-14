package com.catnip.rizkyilmann_challange4.ui.home.adapter.category

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.heketon3.core.ViewHolderBinder
import com.catnip.rizkyilmann_challange4.databinding.ItemCategoryLinearBinding
import com.catnip.rizkyilmann_challange4.model.Category

class GridCategoryItemViewHolder(
    private val binding: ItemCategoryLinearBinding,
    private val onClickListener: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Category> {
    override fun bind(item: Category) {
        binding.ivCategoryIcon.load(item.categoryImgUrl) {
            crossfade(true)
        }
        binding.tvCategoryName.text = item.name
    }
}
