package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.Manager.UserManager
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
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
        // Cập nhật lại thông tin khi quay lại màn hình (ví dụ sau khi hoàn thiện thông tin)
        loadUserInfo()
    }

    private fun loadUserInfo() {
        val user = userManager.getCurrentUser()
        if (user != null) {
            // Hiển thị tên (nếu chưa có thì hiển thị số điện thoại)
            binding.tvName.text = if (user.fullName.isNotEmpty()) {
                user.fullName
            } else {
                user.phoneNumber
            }
            
            // Hiển thị số điện thoại
            binding.tvPhone.text = user.phoneNumber
        }
    }

    private fun setupClickListeners() {
        // Bottom Navigation
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Menu items
        binding.menuAccountInfo.setOnClickListener {
            val intent = Intent(this, AccountInfoActivity::class.java)
            startActivity(intent)
        }

        binding.menuPayment.setOnClickListener {
            // TODO: Navigate to payment methods
        }

        binding.menuLocation.setOnClickListener {
            // TODO: Navigate to order location
        }

        binding.menuHistory.setOnClickListener {
            // TODO: Navigate to purchase history
        }

        binding.menuChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.menuSettings.setOnClickListener {
            // TODO: Navigate to settings
        }

        binding.menuAbout.setOnClickListener {
            // TODO: Navigate to about us
        }

        binding.menuLogout.setOnClickListener {
            userManager.logout()
            userManager.clearToken()
            
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}

