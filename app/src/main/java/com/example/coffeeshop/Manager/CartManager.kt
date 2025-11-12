package com.example.coffeeshop.Manager

import android.content.Context
import android.content.SharedPreferences
import com.example.coffeeshop.Domain.CartModel
import com.example.coffeeshop.Domain.ItemsModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val cartKey = "cart_items"

    fun addToCart(item: ItemsModel): Boolean {
        val cartList = getCartList()
        
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        val existingItemIndex = cartList.indexOfFirst { 
            it.item.title == item.title 
        }
        
        if (existingItemIndex != -1) {
            // Nếu đã có, tăng số lượng
            cartList[existingItemIndex].quantity++
        } else {
            // Nếu chưa có, thêm mới
            cartList.add(CartModel(item, 1))
        }
        
        return saveCartList(cartList)
    }

    fun removeFromCart(itemTitle: String): Boolean {
        val cartList = getCartList()
        val removed = cartList.removeAll { it.item.title == itemTitle }
        if (removed) {
            return saveCartList(cartList)
        }
        return false
    }

    fun updateQuantity(itemTitle: String, quantity: Int): Boolean {
        if (quantity <= 0) {
            return removeFromCart(itemTitle)
        }
        
        val cartList = getCartList()
        val itemIndex = cartList.indexOfFirst { it.item.title == itemTitle }
        
        if (itemIndex != -1) {
            cartList[itemIndex].quantity = quantity
            return saveCartList(cartList)
        }
        return false
    }

    fun getCartList(): MutableList<CartModel> {
        val json = sharedPreferences.getString(cartKey, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<CartModel>>() {}.type
            gson.fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }

    fun getCartItemCount(): Int {
        return getCartList().sumOf { it.quantity }
    }

    fun getTotalPrice(): Double {
        return getCartList().sumOf { it.getTotalPrice() }
    }

    fun clearCart(): Boolean {
        return sharedPreferences.edit().remove(cartKey).commit()
    }

    private fun saveCartList(cartList: MutableList<CartModel>): Boolean {
        val json = gson.toJson(cartList)
        return sharedPreferences.edit().putString(cartKey, json).commit()
    }
}

