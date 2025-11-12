package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.Manager.UserManager
import com.example.coffeeshop.R
import com.example.coffeeshop.Repository.UserRepository
import com.example.coffeeshop.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var userRepository: UserRepository
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository(this)
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
                return@setOnClickListener
            }
            if (newPassword.isEmpty()) {
                binding.etNewPassword.error = "Vui lòng nhập mật khẩu mới"
                return@setOnClickListener
            }
            if (newPassword.length < 6) {
                binding.etNewPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                binding.etConfirmPassword.error = "Vui lòng xác nhận mật khẩu mới"
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                binding.etConfirmPassword.error = "Mật khẩu xác nhận không khớp"
                return@setOnClickListener
            }

            val userId = userManager.getUserId()
            if (userId != null) {
                binding.btnChangePassword.isEnabled = false
                binding.btnChangePassword.text = "Đang xử lý..."
                
                userRepository.changePassword(userId, currentPassword, newPassword, object : UserRepository.ChangePasswordCallback {
                    override fun onSuccess(message: String) {
                        runOnUiThread {
                            Toast.makeText(this@ChangePasswordActivity, message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onError(message: String) {
                        runOnUiThread {
                            binding.btnChangePassword.isEnabled = true
                            binding.btnChangePassword.text = getString(R.string.change_password)
                            Toast.makeText(this@ChangePasswordActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } else {
                Toast.makeText(this, "Phiên đăng nhập đã hết hạn", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
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

