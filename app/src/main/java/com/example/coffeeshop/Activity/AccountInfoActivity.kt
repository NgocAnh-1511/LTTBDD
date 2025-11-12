package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.Manager.UserManager
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ActivityAccountInfoBinding

class AccountInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountInfoBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(this)

        // Kiểm tra đăng nhập
        if (!userManager.isLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        loadUserInfo()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        // Cập nhật lại thông tin khi quay lại màn hình
        loadUserInfo()
    }

    private fun loadUserInfo() {
        val user = userManager.getCurrentUser()
        if (user != null) {
            // Hiển thị thông tin đầy đủ
            binding.tvFullName.text = if (user.fullName.isNotEmpty()) {
                user.fullName
            } else {
                "Chưa cập nhật"
            }
            
            binding.tvPhoneNumber.text = user.phoneNumber
            
            binding.tvEmail.text = if (user.email.isNotEmpty()) {
                user.email
            } else {
                "Chưa cập nhật"
            }
            
            // Format thời gian tạo
            if (user.createdAt > 0) {
                val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                binding.tvCreatedAt.text = dateFormat.format(java.util.Date(user.createdAt))
            } else {
                binding.tvCreatedAt.text = "Chưa có"
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Bottom Navigation
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding.btnCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        binding.btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

