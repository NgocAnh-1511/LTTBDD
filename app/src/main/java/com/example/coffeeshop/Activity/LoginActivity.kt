package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.Manager.UserManager
import com.example.coffeeshop.R
import com.example.coffeeshop.Utils.ValidationUtils
import com.example.coffeeshop.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(this)

        // Kiểm tra nếu đã đăng nhập
        if (userManager.isLoggedIn()) {
            val redirectToCheckout = intent.getBooleanExtra("redirectToCheckout", false)
            val intent = if (redirectToCheckout) {
                Intent(this, CheckoutActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
            return
        }

        setupClickListeners()
        setupKeyboardListener()
        setupImeActions()
    }
    
    private fun setupImeActions() {
        // Xử lý khi nhấn Next trên bàn phím
        binding.etPhone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                binding.etPassword.requestFocus()
                true
            } else {
                false
            }
        }
        
        binding.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                if (binding.etConfirmPassword.visibility == android.view.View.VISIBLE) {
                    binding.etConfirmPassword.requestFocus()
                } else {
                    binding.btnLogin.requestFocus()
                }
                true
            } else {
                false
            }
        }
        
        binding.etConfirmPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                binding.btnContinue.requestFocus()
                true
            } else {
                false
            }
        }
    }

    private fun setupClickListeners() {
        // Chuyển đổi giữa form đăng nhập và đăng ký
        binding.tabLogin.setOnClickListener {
            showLoginForm()
        }

        binding.tabRegister.setOnClickListener {
            showRegisterForm()
        }

        // Xử lý đăng nhập - kiểm tra thông tin trong database
        binding.btnLogin.setOnClickListener {
            val phoneNumber = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Validation
            if (phoneNumber.isEmpty()) {
                binding.etPhone.error = "Vui lòng nhập số điện thoại"
                binding.etPhone.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Vui lòng nhập mật khẩu"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            // Kiểm tra thông tin đăng nhập trong database
            val user = userManager.login(phoneNumber, password)
            if (user != null) {
                // Chuyển giỏ hàng tạm vào database
                val cartManager = com.example.coffeeshop.Manager.CartManager(this)
                cartManager.migrateTempCartToDatabase(user.userId)
                
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                
                // Kiểm tra xem có cần redirect đến CheckoutActivity không
                val redirectToCheckout = intent.getBooleanExtra("redirectToCheckout", false)
                val intent = if (redirectToCheckout) {
                    Intent(this, CheckoutActivity::class.java)
                } else {
                    Intent(this, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Số điện thoại hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý đăng ký - lưu thông tin vào database
        binding.btnContinue.setOnClickListener {
            val phoneNumber = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            // Validation số điện thoại
            val phoneValidation = ValidationUtils.validatePhone(phoneNumber)
            if (!phoneValidation.first) {
                binding.etPhone.error = phoneValidation.second
                binding.etPhone.requestFocus()
                return@setOnClickListener
            }

            // Validation mật khẩu
            val passwordValidation = ValidationUtils.validatePassword(password)
            if (!passwordValidation.first) {
                binding.etPassword.error = passwordValidation.second
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.etPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                binding.etConfirmPassword.error = "Vui lòng xác nhận mật khẩu"
                binding.etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Mật khẩu xác nhận không khớp"
                binding.etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // Kiểm tra số điện thoại đã tồn tại chưa
            if (userManager.isPhoneNumberExists(phoneNumber)) {
                Toast.makeText(this, "Số điện thoại này đã được đăng ký!", Toast.LENGTH_SHORT).show()
                binding.etPhone.error = "Số điện thoại đã tồn tại"
                binding.etPhone.requestFocus()
                return@setOnClickListener
            }

            // Đăng ký user mới (không tự động đăng nhập)
            val success = userManager.registerUser(phoneNumber, password, autoLogin = false)
            if (success) {
                Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show()
                
                // Chuyển về form đăng nhập
                showLoginForm()
                // Giữ lại số điện thoại đã nhập
                binding.etPhone.setText(phoneNumber)
                binding.etPassword.setText("")
            } else {
                Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý quên mật khẩu
        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        // Xử lý đăng nhập Facebook
        binding.btnFacebookLogin.setOnClickListener {
            // Simulate Facebook login (in real app, integrate Facebook SDK)
            Toast.makeText(this, "Đang đăng nhập bằng Facebook...", Toast.LENGTH_SHORT).show()
            
            // Create a temporary user for Facebook login
            val user = com.example.coffeeshop.Domain.UserModel(
                userId = System.currentTimeMillis().toString(),
                phoneNumber = "Facebook_${System.currentTimeMillis()}",
                fullName = "Facebook User",
                email = "facebook@example.com",
                password = "",
                createdAt = System.currentTimeMillis()
            )
            
            userManager.saveUser(user)
            
            // Simulate delay
            binding.root.postDelayed({
                Toast.makeText(this, "Đăng nhập Facebook thành công!", Toast.LENGTH_SHORT).show()
                val redirectToCheckout = intent.getBooleanExtra("redirectToCheckout", false)
                val intent = if (redirectToCheckout) {
                    Intent(this, CheckoutActivity::class.java)
                } else {
                    Intent(this, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            }, 1000)
        }

        // Xử lý đăng ký Facebook
        binding.btnFacebookRegister.setOnClickListener {
            // Simulate Facebook registration (in real app, integrate Facebook SDK)
            Toast.makeText(this, "Đang đăng ký bằng Facebook...", Toast.LENGTH_SHORT).show()
            
            // Create a temporary user for Facebook registration
            val user = com.example.coffeeshop.Domain.UserModel(
                userId = System.currentTimeMillis().toString(),
                phoneNumber = "Facebook_${System.currentTimeMillis()}",
                fullName = "Facebook User",
                email = "facebook@example.com",
                password = "",
                createdAt = System.currentTimeMillis()
            )
            
            userManager.saveUser(user)
            
            // Simulate delay
            binding.root.postDelayed({
                Toast.makeText(this, "Đăng ký Facebook thành công!", Toast.LENGTH_SHORT).show()
                val redirectToCheckout = intent.getBooleanExtra("redirectToCheckout", false)
                val intent = if (redirectToCheckout) {
                    Intent(this, CheckoutActivity::class.java)
                } else {
                    Intent(this, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            }, 1000)
        }
    }


    private fun setupKeyboardListener() {
        // Chỉ scroll khi thực sự cần thiết (khi bàn phím che mất input)
        val rootView = window.decorView.rootView
        var isKeyboardShowing = false
        
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = android.graphics.Rect()
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom
                val wasKeyboardShowing = isKeyboardShowing
                isKeyboardShowing = keypadHeight > screenHeight * 0.15

                // Chỉ scroll khi bàn phím vừa mới hiện lên và có input đang focus
                if (isKeyboardShowing && !wasKeyboardShowing) {
                    val focusedView = window.currentFocus
                    if (focusedView != null && (focusedView == binding.etPhone || 
                        focusedView == binding.etPassword || 
                        focusedView == binding.etConfirmPassword)) {
                        // Scroll nhẹ nhàng để input không bị che
                        binding.scrollView.postDelayed({
                            val scrollY = focusedView.top - 100 // Để lại 100dp phía trên
                            if (scrollY > 0) {
                                binding.scrollView.smoothScrollTo(0, scrollY)
                            }
                        }, 100)
                    }
                }
            }
        })
    }

    private fun showLoginForm() {
        // Cập nhật tab
        binding.tabLogin.setTextColor(getColor(R.color.darkBrown))
        binding.tabLogin.setTypeface(null, android.graphics.Typeface.BOLD)
        binding.tabRegister.setTextColor(getColor(R.color.grey))
        binding.tabRegister.setTypeface(null, android.graphics.Typeface.NORMAL)

        // Hiển thị form đăng nhập
        binding.etPassword.hint = getString(R.string.enter_password)
        binding.etPassword.visibility = android.view.View.VISIBLE
        binding.etConfirmPassword.visibility = android.view.View.GONE
        binding.btnLogin.visibility = android.view.View.VISIBLE
        binding.btnContinue.visibility = android.view.View.GONE
        binding.tvForgotPassword.visibility = android.view.View.VISIBLE
        binding.btnFacebookLogin.visibility = android.view.View.VISIBLE
        binding.btnFacebookRegister.visibility = android.view.View.GONE
    }

    private fun showRegisterForm() {
        // Cập nhật tab
        binding.tabRegister.setTextColor(getColor(R.color.darkBrown))
        binding.tabRegister.setTypeface(null, android.graphics.Typeface.BOLD)
        binding.tabLogin.setTextColor(getColor(R.color.grey))
        binding.tabLogin.setTypeface(null, android.graphics.Typeface.NORMAL)

        // Hiển thị form đăng ký
        binding.etPassword.hint = getString(R.string.enter_password_dots)
        binding.etPassword.visibility = android.view.View.VISIBLE
        binding.etConfirmPassword.visibility = android.view.View.VISIBLE
        binding.btnLogin.visibility = android.view.View.GONE
        binding.btnContinue.visibility = android.view.View.VISIBLE
        binding.tvForgotPassword.visibility = android.view.View.GONE
        binding.btnFacebookLogin.visibility = android.view.View.GONE
        binding.btnFacebookRegister.visibility = android.view.View.VISIBLE
    }

    private fun showForgotPasswordDialog() {
        val input = android.widget.EditText(this)
        input.hint = "Nhập số điện thoại"
        input.inputType = android.text.InputType.TYPE_CLASS_PHONE
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Quên mật khẩu")
            .setMessage("Vui lòng nhập số điện thoại của bạn để nhận mã OTP đặt lại mật khẩu.")
            .setView(input)
            .setPositiveButton("Gửi OTP") { dialog, _ ->
                val phoneNumber = input.text.toString().trim()
                if (phoneNumber.isNotEmpty()) {
                    // Trong thực tế, sẽ gửi OTP qua SMS/Email
                    Toast.makeText(
                        this,
                        "Mã OTP đã được gửi đến số điện thoại $phoneNumber",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}

