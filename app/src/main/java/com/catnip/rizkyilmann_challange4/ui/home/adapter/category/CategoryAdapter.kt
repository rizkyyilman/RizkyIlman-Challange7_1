package com.catnip.rizkyilmann_challange4.ui.home.adapter.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.rizkyilmann_challange4.databinding.ItemCategoryLinearBinding
import com.catnip.rizkyilmann_challange4.model.Category

class CategoryAdapter(private val onItemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryItemViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Category>() {
                override fun areItemsTheSame(
                    oldItem: Category,
                    newItem: Category
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Category,
                    newItem: Category
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    fun submitData(data: List<Category>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val binding =
            ItemCategoryLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryItemViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class CategoryItemViewHolder(
        private val binding: ItemCategoryLinearBinding,
        val onItemClick: (Category) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategoryName.text = category.name
            binding.ivCategoryIcon.load(category.categoryImgUrl)
        }

        fun bindView(item: Category) {
            with(item) {
                binding.ivCategoryIcon.load(item.categoryImgUrl) {
                    crossfade(true)
                }
                binding.tvCategoryName.text = item.name
                itemView.setOnClickListener { onItemClick(this) }
            }
        }
    }
}
