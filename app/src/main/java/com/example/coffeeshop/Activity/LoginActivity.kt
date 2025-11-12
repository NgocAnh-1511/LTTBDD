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
import com.example.coffeeshop.Repository.UserRepository
import com.example.coffeeshop.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository(this)
        userManager = UserManager(this)

        // Kiểm tra nếu đã đăng nhập thì chuyển sang MainActivity
        if (userManager.isLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setupClickListeners()
        setupKeyboardListener()
    }

    private fun setupClickListeners() {
        // Chuyển đổi giữa form đăng nhập và đăng ký
        binding.tabLogin.setOnClickListener {
            showLoginForm()
        }

        binding.tabRegister.setOnClickListener {
            showRegisterForm()
        }

        // Xử lý đăng nhập
        binding.btnLogin.setOnClickListener {
            val phoneNumber = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (phoneNumber.isEmpty()) {
                binding.etPhone.error = "Vui lòng nhập số điện thoại/username"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Vui lòng nhập mật khẩu"
                return@setOnClickListener
            }

            binding.btnLogin.isEnabled = false
            binding.btnLogin.text = "Đang đăng nhập..."
            
            userRepository.login(phoneNumber, password, object : UserRepository.LoginCallback {
                override fun onSuccess(user: com.example.coffeeshop.Domain.UserModel) {
                    runOnUiThread {
                        userManager.saveUser(user)
                        Toast.makeText(this@LoginActivity, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                        
                        // Kiểm tra nếu chưa có fullName hoặc email thì chuyển đến màn hình hoàn thiện thông tin
                        if (user.fullName.isEmpty() || user.email.isEmpty()) {
                            val intent = Intent(this@LoginActivity, CompleteProfileActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        finish()
                    }
                }

                override fun onError(message: String) {
                    runOnUiThread {
                        binding.btnLogin.isEnabled = true
                        binding.btnLogin.text = getString(R.string.login)
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        // Xử lý đăng ký - nhấn tiếp tục thì đăng ký và chuyển về form đăng nhập
        binding.btnContinue.setOnClickListener {
            val phoneNumber = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (phoneNumber.isEmpty()) {
                binding.etPhone.error = "Vui lòng nhập số điện thoại/username"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Vui lòng nhập mật khẩu"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.etPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                binding.etConfirmPassword.error = "Vui lòng xác nhận mật khẩu"
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Mật khẩu xác nhận không khớp"
                return@setOnClickListener
            }

            binding.btnContinue.isEnabled = false
            binding.btnContinue.text = "Đang đăng ký..."
            
            userRepository.register(phoneNumber, password, object : UserRepository.RegisterCallback {
                override fun onSuccess(user: com.example.coffeeshop.Domain.UserModel) {
                    runOnUiThread {
                        userManager.saveUser(user)
                        Toast.makeText(this@LoginActivity, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                        // Sau khi đăng ký thành công, chuyển về form đăng nhập
                        showLoginForm()
                        binding.btnContinue.isEnabled = true
                        binding.btnContinue.text = getString(R.string.continue_button)
                    }
                }

                override fun onError(message: String) {
                    runOnUiThread {
                        binding.btnContinue.isEnabled = true
                        binding.btnContinue.text = getString(R.string.continue_button)
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        // Xử lý quên mật khẩu
        binding.tvForgotPassword.setOnClickListener {
            // TODO: Xử lý quên mật khẩu
        }

        // Xử lý đăng nhập Facebook
        binding.btnFacebookLogin.setOnClickListener {
            // TODO: Xử lý đăng nhập Facebook
        }

        // Xử lý đăng ký Facebook
        binding.btnFacebookRegister.setOnClickListener {
            // TODO: Xử lý đăng ký Facebook
        }
    }


    private fun setupKeyboardListener() {
        // Lắng nghe khi bàn phím hiện/ẩn để scroll form
        val rootView = window.decorView.rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = android.graphics.Rect()
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                if (keypadHeight > screenHeight * 0.15) { // Bàn phím đang hiện
                    // Scroll đến phần cuối form để thấy nút
                    binding.root.postDelayed({
                        binding.scrollView.fullScroll(View.FOCUS_DOWN)
                    }, 100)
                }
            }
        })

        // Scroll khi focus vào các input
        binding.etPhone.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.root.postDelayed({
                    scrollToView(binding.etPhone)
                }, 300)
            }
        }

        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.root.postDelayed({
                    scrollToView(binding.etPassword)
                }, 300)
            }
        }

        binding.etConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.root.postDelayed({
                    scrollToView(binding.etConfirmPassword)
                }, 300)
            }
        }
    }

    private fun scrollToView(view: View) {
        val scrollView = binding.scrollView
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewTop = location[1]
        val rootLocation = IntArray(2)
        binding.root.getLocationOnScreen(rootLocation)
        val rootTop = rootLocation[1]
        val relativeTop = viewTop - rootTop
        
        // Scroll để view hiển thị ở vị trí phù hợp (không bị che bởi bàn phím)
        scrollView.post {
            scrollView.smoothScrollTo(0, relativeTop - 200) // Để lại khoảng trống 200dp phía trên
        }
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
}

