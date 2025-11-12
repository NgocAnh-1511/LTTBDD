package com.example.coffeeshop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Domain.CartModel
import com.example.coffeeshop.databinding.ViewholderCartBinding

class CartAdapter(
    private val items: MutableList<CartModel>,
    private val onQuantityChanged: (String, Int) -> Unit,
    private val onRemoveClick: (String) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    
    lateinit var context: Context

    class ViewHolder(val binding: ViewholderCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderCartBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = items[position]
        val item = cartItem.item

        holder.binding.itemTitle.text = item.title
        val totalPrice = cartItem.getTotalPrice()
        holder.binding.itemPrice.text = "$${String.format("%.2f", totalPrice)}"
        holder.binding.quantityTxt.text = cartItem.quantity.toString()

        // Load image
        if (item.picUrl.isNotEmpty()) {
            Glide.with(context)
                .load(item.picUrl[0])
                .into(holder.binding.itemPic)
        }

        // Plus button
        holder.binding.plusBtn.setOnClickListener {
            onQuantityChanged(item.title, cartItem.quantity + 1)
        }

        // Minus button
        holder.binding.minusBtn.setOnClickListener {
            if (cartItem.quantity > 1) {
                onQuantityChanged(item.title, cartItem.quantity - 1)
            } else {
                onRemoveClick(item.title)
            }
        }

        // Remove button
        holder.binding.removeBtn.setOnClickListener {
            onRemoveClick(item.title)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: MutableList<CartModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}

