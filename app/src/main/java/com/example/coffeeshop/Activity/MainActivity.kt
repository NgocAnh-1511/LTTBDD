package com.example.coffeeshop.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.coffeeshop.R
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.CategoryAdapter
import com.example.coffeeshop.Adapter.PopularAdapter
import com.example.coffeeshop.Manager.CartManager
import com.example.coffeeshop.Manager.UserManager
import android.widget.Toast
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel= MainViewModel()
    private lateinit var cartManager: CartManager
    private lateinit var userManager: UserManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartManager = CartManager(this)
        userManager = UserManager(this)

        loadUserInfo()
        initBanner()
        initCategory()
        initPopular()
        setupNavigation()
    }

    override fun onResume() {
        super.onResume()
        // Cập nhật lại thông tin khi quay lại màn hình
        loadUserInfo()
    }

    private fun loadUserInfo() {
        val user = userManager.getCurrentUser()
        if (user != null) {
            // Hiển thị tên (nếu chưa có thì hiển thị số điện thoại)
            binding.textView2.text = if (user.fullName.isNotEmpty()) {
                user.fullName
            } else {
                user.phoneNumber
            }
        }
    }

    private fun setupNavigation() {
        binding.cartBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        binding.profileBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initPopular() {
        binding.progressBarPopular.visibility= View.VISIBLE
        viewModel.loadPopular().observeForever {
            binding.recyclerViewPopular.layoutManager = GridLayoutManager(this,2)
            binding.recyclerViewPopular.adapter= PopularAdapter(it) { item ->
                if (cartManager.addToCart(item)) {
                    Toast.makeText(this, "${item.title} đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                }
            }
            binding.progressBarPopular.visibility= View.GONE
        }
        viewModel.loadPopular()
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility= View.VISIBLE
        viewModel.loadCategory().observeForever {
            binding.categoryView
                .layoutManager = LinearLayoutManager(
                this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.categoryView.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility= View.GONE
        }
        viewModel.loadCategory()
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility= View.VISIBLE
        viewModel.loadBanner().observeForever {
            Glide.with(this@MainActivity)
                .load(it[0].url)
                .into(binding.banner)
            binding.progressBarBanner.visibility= View.GONE
        }
        viewModel.loadBanner()

    }
}