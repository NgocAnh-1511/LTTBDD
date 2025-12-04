package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.Manager.UserManager
import com.example.coffeeshop.R
import com.example.coffeeshop.Utils.ValidationUtils
import com.example.coffeeshop.Utils.dpToPx
import com.example.coffeeshop.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(this)

        // Kiểm tra đăng nhập
        if (!userManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Change password button
        binding.btnChangePassword.setOnClickListener {
            val currentPassword = binding.etCurrentPassword.text.toString()
            val newPassword = binding.etNewPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (currentPassword.isEmpty()) {
                binding.etCurrentPassword.error = "Vui lòng nhập mật khẩu hiện tại"
                binding.etCurrentPassword.requestFocus()
                return@setOnClickListener
            }
            
            // Validation mật khẩu mới
            val passwordValidation = ValidationUtils.validatePassword(newPassword)
            if (!passwordValidation.first) {
                binding.etNewPassword.error = passwordValidation.second
                binding.etNewPassword.requestFocus()
                return@setOnClickListener
            }
            
            // Validation xác nhận mật khẩu
            val confirmPasswordValidation = ValidationUtils.validateConfirmPassword(newPassword, confirmPassword)
            if (!confirmPasswordValidation.first) {
                binding.etConfirmPassword.error = confirmPasswordValidation.second
                binding.etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // Không dùng database - chỉ hiển thị thông báo thành công
            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
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

