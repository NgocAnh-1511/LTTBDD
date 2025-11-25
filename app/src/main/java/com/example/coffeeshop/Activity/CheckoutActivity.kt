package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.CartAdapter
import com.example.coffeeshop.Domain.AddressModel
import com.example.coffeeshop.Manager.AddressManager
import com.example.coffeeshop.Manager.CartManager
import com.example.coffeeshop.Manager.OrderManager
import com.example.coffeeshop.Manager.UserManager
import com.example.coffeeshop.R
import com.example.coffeeshop.Utils.ValidationUtils
import com.example.coffeeshop.databinding.ActivityCheckoutBinding
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var cartManager: CartManager
    private lateinit var orderManager: OrderManager
    private lateinit var addressManager: AddressManager
    private lateinit var userManager: UserManager
    private lateinit var cartAdapter: CartAdapter
    private var discountPercent: Double = 0.0
    private var baseTotal: Double = 0.0
    private var hasSelectedAddress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(this)
        cartManager = CartManager(this)
        orderManager = OrderManager(this)
        addressManager = AddressManager(this)

        // Bắt buộc phải đăng nhập để thanh toán
        if (!userManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để thanh toán", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("redirectToCheckout", true)
            startActivity(intent)
            finish()
            return
        }

        setupRecyclerView()
        setupClickListeners()
        loadOrderSummary()
        loadSavedAddresses()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewOrderSummary.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(
            mutableListOf(),
            onQuantityChanged = { _, _ -> }, // Read-only in checkout
            onRemoveClick = { }, // Read-only in checkout
            isReadOnly = true // Set read-only mode
        )
        binding.recyclerViewOrderSummary.adapter = cartAdapter
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.confirmPaymentBtn.setOnClickListener {
            if (validateInput()) {
                processPayment()
            }
        }

        binding.applyVoucherBtn.setOnClickListener {
            applyVoucher()
        }

        binding.selectAddressBtn.setOnClickListener {
            selectSavedAddress()
        }
    }

    private fun loadSavedAddresses() {
        val defaultAddress = addressManager.getDefaultAddress()
        defaultAddress?.let {
            binding.nameEditText.setText(it.name)
            binding.phoneEditText.setText(it.phone)
            binding.addressEditText.setText(it.address)
        }
    }

    private val addressResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val addressJson = result.data?.getStringExtra("selectedAddress")
            if (addressJson != null) {
                val address = com.google.gson.Gson().fromJson(addressJson, AddressModel::class.java)
                binding.nameEditText.setText(address.name)
                binding.phoneEditText.setText(address.phone)
                binding.addressEditText.setText(address.address)
                hasSelectedAddress = true // Đánh dấu đã chọn địa chỉ
                Toast.makeText(this, "Đã chọn địa chỉ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectSavedAddress() {
        val intent = Intent(this, AddressListActivity::class.java)
        intent.putExtra("selectMode", true)
        addressResultLauncher.launch(intent)
    }

    private fun loadOrderSummary() {
        val cartList = cartManager.getCartList()
        
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cartAdapter.updateList(cartList)
        baseTotal = cartManager.getTotalPrice()
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        binding.subtotalTxt.text = "$${String.format(Locale.getDefault(), "%.2f", baseTotal)}"
        
        val discount = baseTotal * discountPercent / 100
        val finalTotal = baseTotal - discount
        
        if (discountPercent > 0) {
            binding.voucherDiscountLayout.visibility = View.VISIBLE
            binding.discountTxt.text = "-$${String.format(Locale.getDefault(), "%.2f", discount)}"
        } else {
            binding.voucherDiscountLayout.visibility = View.GONE
        }
        
        binding.totalPriceTxt.text = "$${String.format(Locale.getDefault(), "%.2f", finalTotal)}"
    }

    private fun applyVoucher() {
        val voucherCode = binding.voucherEditText.text.toString().trim().uppercase()
        
        // Simple voucher validation - in real app, this would check against server/database
        when (voucherCode) {
            "WELCOME10" -> {
                discountPercent = 10.0
                binding.voucherAppliedTxt.text = "Mã giảm giá đã được áp dụng: -10%"
                binding.voucherAppliedTxt.visibility = View.VISIBLE
                binding.voucherEditText.isEnabled = false
                binding.applyVoucherBtn.isEnabled = false
                updateTotalPrice()
                Toast.makeText(this, "Áp dụng mã giảm giá thành công!", Toast.LENGTH_SHORT).show()
            }
            "SAVE20" -> {
                discountPercent = 20.0
                binding.voucherAppliedTxt.text = "Mã giảm giá đã được áp dụng: -20%"
                binding.voucherAppliedTxt.visibility = View.VISIBLE
                binding.voucherEditText.isEnabled = false
                binding.applyVoucherBtn.isEnabled = false
                updateTotalPrice()
                Toast.makeText(this, "Áp dụng mã giảm giá thành công!", Toast.LENGTH_SHORT).show()
            }
            "" -> {
                Toast.makeText(this, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Mã giảm giá không hợp lệ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(): Boolean {
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val address = binding.addressEditText.text.toString().trim()

        // Validation tên
        val nameValidation = ValidationUtils.validateName(name)
        if (!nameValidation.first) {
            binding.nameEditText.error = nameValidation.second
            binding.nameEditText.requestFocus()
            return false
        }

        // Validation số điện thoại
        val phoneValidation = ValidationUtils.validatePhone(phone)
        if (!phoneValidation.first) {
            binding.phoneEditText.error = phoneValidation.second
            binding.phoneEditText.requestFocus()
            return false
        }

        // Validation địa chỉ
        if (TextUtils.isEmpty(address)) {
            binding.addressEditText.error = "Vui lòng nhập địa chỉ giao hàng"
            binding.addressEditText.requestFocus()
            return false
        }

        return true
    }

    private fun getPaymentMethod(): String {
        return when (binding.paymentMethodGroup.checkedRadioButtonId) {
            R.id.cashRadioBtn -> "Tiền mặt"
            R.id.cardRadioBtn -> "Thẻ tín dụng/Ghi nợ"
            R.id.eWalletRadioBtn -> "Ví điện tử"
            else -> "Tiền mặt"
        }
    }

    private fun processPayment() {
        val cartList = cartManager.getCartList()
        val discount = baseTotal * discountPercent / 100
        val totalPrice = baseTotal - discount
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val address = binding.addressEditText.text.toString().trim()
        val paymentMethod = getPaymentMethod()
        val voucherCode = if (discountPercent > 0) binding.voucherEditText.text.toString().trim() else ""

        // Show loading
        binding.confirmPaymentBtn.isEnabled = false
        binding.confirmPaymentBtn.text = "Đang xử lý..."

        // Simulate payment processing (in real app, this would call payment API)
        binding.root.postDelayed({
            // Create order
            val order = orderManager.createOrder(
                items = cartList,
                totalPrice = totalPrice,
                deliveryAddress = address,
                phoneNumber = phone,
                customerName = name,
                paymentMethod = paymentMethod
            )

            // Clear cart
            cartManager.clearCart()

            // Show success message and navigate to order detail
            Toast.makeText(
                this,
                "Thanh toán thành công! Mã đơn: #${order.orderId.take(8)}",
                Toast.LENGTH_LONG
            ).show()

            // Navigate to order detail
            val intent = Intent(this, OrderDetailActivity::class.java)
            val orderJson = com.google.gson.Gson().toJson(order)
            intent.putExtra("order", orderJson)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            
            // Finish checkout and cart activities
            finish()
        }, 1500) // Simulate 1.5 second processing time
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel any pending operations
        binding.root.removeCallbacks(null)
    }

    override fun onResume() {
        super.onResume()
        // Chỉ load lại địa chỉ mặc định nếu các trường địa chỉ đang trống
        // Tránh ghi đè địa chỉ vừa chọn từ AddressListActivity
        val nameEmpty = binding.nameEditText.text.toString().trim().isEmpty()
        val phoneEmpty = binding.phoneEditText.text.toString().trim().isEmpty()
        val addressEmpty = binding.addressEditText.text.toString().trim().isEmpty()
        
        if (nameEmpty && phoneEmpty && addressEmpty && !hasSelectedAddress) {
            loadSavedAddresses()
        }
    }
}

