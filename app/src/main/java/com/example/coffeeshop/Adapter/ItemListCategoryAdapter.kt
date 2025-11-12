package com.example.coffeeshop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Domain.ItemsModel
import com.example.coffeeshop.databinding.ViewholderItemListBinding

class ItemListCategoryAdapter(
    val items: MutableList<ItemsModel>,
    val onAddToCartClick: (ItemsModel) -> Unit
):
RecyclerView.Adapter<ItemListCategoryAdapter.ViewHolder>() {
    lateinit var context: Context



    class ViewHolder ( val binding: ViewholderItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListCategoryAdapter.ViewHolder {
        context = parent.context
        val binding= ViewholderItemListBinding.
        inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemListCategoryAdapter.ViewHolder, position: Int) {
        holder.binding.titleTxt.text = items[position].title
        holder.binding.priceTxt.text="$"+items[position].price.toString()
        holder.binding.subtitleTxt.text = items[position].extra.toString()

        Glide.with(context)
            .load(items[position].picUrl[0])
            .into(holder.binding.pic)

        holder.binding.imageView6.setOnClickListener {
            onAddToCartClick(items[position])
        }

    }

    override fun getItemCount(): Int  = items.size
}