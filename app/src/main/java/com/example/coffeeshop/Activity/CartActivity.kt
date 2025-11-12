package com.example.coffeeshop.Activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.CartAdapter
import com.example.coffeeshop.Manager.CartManager
import com.example.coffeeshop.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartManager: CartManager
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartManager = CartManager(this)

        setupRecyclerView()
        setupClickListeners()
        loadCartItems()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(
            mutableListOf(),
            onQuantityChanged = { itemTitle, newQuantity ->
                if (cartManager.updateQuantity(itemTitle, newQuantity)) {
                    loadCartItems()
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                }
            },
            onRemoveClick = { itemTitle ->
                if (cartManager.removeFromCart(itemTitle)) {
                    Toast.makeText(this, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show()
                    loadCartItems()
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                }
            }
        )
        binding.recyclerViewCart.adapter = cartAdapter
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.checkoutBtn.setOnClickListener {
            val cartList = cartManager.getCartList()
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Implement checkout functionality
                Toast.makeText(this, "Chức năng thanh toán đang được phát triển", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCartItems() {
        val cartList = cartManager.getCartList()
        
        if (cartList.isEmpty()) {
            binding.recyclerViewCart.visibility = View.GONE
            binding.emptyCartTxt.visibility = View.VISIBLE
            binding.bottomLayout.visibility = View.GONE
        } else {
            binding.recyclerViewCart.visibility = View.VISIBLE
            binding.emptyCartTxt.visibility = View.GONE
            binding.bottomLayout.visibility = View.VISIBLE
            
            cartAdapter.updateList(cartList)
            updateTotalPrice()
        }
    }

    private fun updateTotalPrice() {
        val total = cartManager.getTotalPrice()
        binding.totalPriceTxt.text = "$${String.format("%.2f", total)}"
    }

    override fun onResume() {
        super.onResume()
        loadCartItems()
    }
}

