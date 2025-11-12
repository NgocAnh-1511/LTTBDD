package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.Manager.UserManager
import com.example.coffeeshop.R
import com.example.coffeeshop.Repository.UserRepository
import com.example.coffeeshop.databinding.ActivityCompleteProfileBinding

class CompleteProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompleteProfileBinding
    private lateinit var userRepository: UserRepository
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCompleteProfileBinding.inflate(layoutInflater)
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
        binding.btnComplete.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            if (fullName.isEmpty()) {
                binding.etFullName.error = "Vui lòng nhập họ tên"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.etEmail.error = "Vui lòng nhập email"
                return@setOnClickListener
            }

            // Kiểm tra email hợp lệ
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Email không hợp lệ"
                return@setOnClickListener
            }

            val userId = userManager.getUserId()
            if (userId != null) {
                binding.btnComplete.isEnabled = false
                binding.btnComplete.text = "Đang xử lý..."

                userRepository.updateProfile(userId, fullName, email, object : UserRepository.UpdateProfileCallback {
                    override fun onSuccess(user: com.example.coffeeshop.Domain.UserModel) {
                        runOnUiThread {
                            userManager.saveUser(user)
                            Toast.makeText(this@CompleteProfileActivity, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@CompleteProfileActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onError(message: String) {
                        runOnUiThread {
                            binding.btnComplete.isEnabled = true
                            binding.btnComplete.text = getString(R.string.complete_profile)
                            Toast.makeText(this@CompleteProfileActivity, message, Toast.LENGTH_SHORT).show()
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
    }
}

