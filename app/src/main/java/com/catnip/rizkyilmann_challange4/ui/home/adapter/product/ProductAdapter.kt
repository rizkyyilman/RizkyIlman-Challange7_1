 
package com.catnip.rizkyilmann_challange4.ui.home.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.catnip.rizkyilmann_challange4.databinding.ItemGridMenuBinding
import com.catnip.rizkyilmann_challange4.model.DetailMenu

class ProductAdapter(
    private val onClickListener: (DetailMenu) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ItemProductViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<DetailMenu>() {
                override fun areItemsTheSame(
                    oldItem: DetailMenu,
                    newItem: DetailMenu
                ): Boolean {
                    return oldItem.id == newItem.id
                    return oldItem.imgUrl == newItem.imgUrl
                }

                override fun areContentsTheSame(
                    oldItem: DetailMenu,
                    newItem: DetailMenu
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductViewHolder {
        val binding =
            ItemGridMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemProductViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: ItemProductViewHolder, position: Int) {
        val product = dataDiffer.currentList[position]
        holder.bindView(product)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    fun submitData(data: List<DetailMenu>) {
        dataDiffer.submitList(data)
    }

    fun refreshList() {
        notifyItemRangeChanged(0, dataDiffer.currentList.size)
    }

    class ItemProductViewHolder(
        private val binding: ItemGridMenuBinding,
        val itemClick: (DetailMenu) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: DetailMenu) {
            with(item) {
                binding.ivMenu.load(item.imgUrl) {
                    crossfade(true)
                }
                binding.tvMenuName.text = item.name
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}
