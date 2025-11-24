package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.coffeeshop.Manager.OrderManager
import com.example.coffeeshop.Manager.UserManager
import com.example.coffeeshop.R
import com.example.coffeeshop.Utils.dpToPx
import com.example.coffeeshop.databinding.ActivityProfileBinding
import java.io.File
import java.util.Locale

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userManager: UserManager
    private lateinit var orderManager: OrderManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle edge-to-edge insets for header
        ViewCompat.setOnApplyWindowInsetsListener(binding.headerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = systemBars.top + 8.dpToPx() // Đưa header xuống một chút
            v.layoutParams = layoutParams
            insets
        }

        userManager = UserManager(this)
        orderManager = OrderManager(this)

        // Không bắt buộc đăng nhập, nhưng nếu chưa đăng nhập thì hiển thị thông báo
        if (!userManager.isLoggedIn()) {
            binding.userNameTxt.text = "Khách"
            binding.userEmailTxt.text = "Vui lòng đăng nhập để xem thông tin"
            binding.totalOrdersTxt.text = "0"
            binding.completedOrdersTxt.text = "0"
            binding.totalSpentTxt.text = "$0.00"
        } else {
            loadUserInfo()
            loadStatistics()
        }
        
        updateLogoutButton()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        // Cập nhật lại thông tin khi quay lại màn hình (ví dụ sau khi hoàn thiện thông tin)
        if (userManager.isLoggedIn()) {
            loadUserInfo()
            loadStatistics()
        } else {
            binding.userNameTxt.text = "Khách"
            binding.userEmailTxt.text = "Vui lòng đăng nhập để xem thông tin"
            binding.totalOrdersTxt.text = "0"
            binding.completedOrdersTxt.text = "0"
            binding.totalSpentTxt.text = "$0.00"
        }
        updateLogoutButton()
    }
    
    private fun updateLogoutButton() {
        if (userManager.isLoggedIn()) {
            // Hiển thị "Đăng xuất" khi đã đăng nhập
            binding.logoutBtnText.text = "Đăng xuất"
        } else {
            // Hiển thị "Đăng nhập/Đăng ký" khi chưa đăng nhập
            binding.logoutBtnText.text = "Đăng nhập/Đăng ký"
        }
    }

    private fun loadUserInfo() {
        val user = userManager.getCurrentUser()
        if (user != null) {
            // Hiển thị tên (nếu chưa có thì hiển thị số điện thoại)
            binding.userNameTxt.text = if (user.fullName.isNotEmpty()) {
                user.fullName
            } else {
                user.phoneNumber
            }
            
            // Hiển thị email
            binding.userEmailTxt.text = if (user.email.isNotEmpty()) {
                user.email
            } else {
                user.phoneNumber
            }
            
            // Load và hiển thị ảnh đại diện
            if (user.avatarPath.isNotEmpty()) {
                val imageFile = File(filesDir, user.avatarPath)
                if (imageFile.exists()) {
                    Glide.with(this)
                        .load(imageFile)
                        .circleCrop()
                        .into(binding.ivProfileAvatar)
                } else {
                    binding.ivProfileAvatar.setImageResource(R.drawable.profile)
                }
            } else {
                binding.ivProfileAvatar.setImageResource(R.drawable.profile)
            }
        }
    }

    private fun loadStatistics() {
        val orders = orderManager.getAllOrders()
        val totalOrders = orders.size
        val completedOrders = orders.count { it.status.equals("Completed", ignoreCase = true) }
        val totalSpent = orders.sumOf { it.totalPrice }
        
        binding.totalOrdersTxt.text = totalOrders.toString()
        binding.completedOrdersTxt.text = completedOrders.toString()
        binding.totalSpentTxt.text = "$${String.format(Locale.getDefault(), "%.2f", totalSpent)}"
    }

    private fun setupClickListeners() {
        // Back button
        binding.backBtn.setOnClickListener {
            finish()
        }

        // Edit profile button
        binding.editProfileBtn.setOnClickListener {
            if (!userManager.isLoggedIn()) {
                Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val user = userManager.getCurrentUser()
                if (user != null && (user.fullName.isEmpty() || user.email.isEmpty())) {
                    // Navigate to CompleteProfileActivity if profile is incomplete
                    val intent = Intent(this, CompleteProfileActivity::class.java)
                    startActivity(intent)
                } else if (user != null) {
                    // Navigate to AccountInfoActivity to view/edit account info
                    val intent = Intent(this, AccountInfoActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        // Menu items
        binding.myOrdersBtn.setOnClickListener {
            if (!userManager.isLoggedIn()) {
                Toast.makeText(this, "Vui lòng đăng nhập để xem đơn hàng", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, OrderActivity::class.java)
                startActivity(intent)
            }
        }

        binding.addressBtnInProfile.setOnClickListener {
            if (!userManager.isLoggedIn()) {
                Toast.makeText(this, "Vui lòng đăng nhập để quản lý địa chỉ", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, AddressListActivity::class.java)
                startActivity(intent)
            }
        }

        binding.wishlistBtnInProfile.setOnClickListener {
            val intent = Intent(this, WishlistActivity::class.java)
            startActivity(intent)
        }

        binding.logoutBtn.setOnClickListener {
            if (userManager.isLoggedIn()) {
                // Đăng xuất
                userManager.logout()
                userManager.clearToken()
                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
                
                // Chuyển về profile khách
                binding.userNameTxt.text = "Khách"
                binding.userEmailTxt.text = "Vui lòng đăng nhập để xem thông tin"
                binding.totalOrdersTxt.text = "0"
                binding.completedOrdersTxt.text = "0"
                binding.totalSpentTxt.text = "$0.00"
                updateLogoutButton()
            } else {
                // Chưa đăng nhập, chuyển đến màn hình đăng nhập/đăng ký
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}

