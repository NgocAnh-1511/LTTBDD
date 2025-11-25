package com.example.coffeeshop.Manager

import android.content.Context
import android.util.Log
import com.example.coffeeshop.Database.DatabaseHelper
import com.example.coffeeshop.Domain.*
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

/**
 * FirebaseSyncManager - Quản lý đồng bộ dữ liệu giữa SQLite và Firebase
 * Sử dụng Coroutines để tránh ANR (Application Not Responding)
 */
class FirebaseSyncManager(private val context: Context) {
    private val firebaseDatabase: FirebaseDatabase by lazy {
        try {
            FirebaseDatabase.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing FirebaseDatabase", e)
            throw e
        }
    }
    
    private val syncScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val gson = Gson()
    
    companion object {
        private const val TAG = "FirebaseSyncManager"
        private const val SYNC_DELAY_MS = 300L // Delay giữa các lần sync để tránh quá tải
    }
    
    /**
     * Đồng bộ tất cả dữ liệu của user lên Firebase (chạy trên background thread)
     */
    fun syncAllDataToFirebaseAsync(userId: String) {
        syncScope.launch {
            try {
                Log.d(TAG, "Starting sync all data to Firebase for user: $userId")
                
                val userManager = UserManager(context)
                val user = userManager.getCurrentUser()
                if (user != null && user.userId == userId) {
                    syncUserToFirebase(user)
                    delay(SYNC_DELAY_MS)
                }
                
                syncCartToFirebase(userId)
                delay(SYNC_DELAY_MS)
                syncOrdersToFirebase(userId)
                delay(SYNC_DELAY_MS)
                syncAddressesToFirebase(userId)
                delay(SYNC_DELAY_MS)
                syncWishlistToFirebase(userId)
                
                Log.d(TAG, "Sync all data to Firebase completed for user: $userId")
            } catch (e: Exception) {
                Log.e(TAG, "Error syncing all data to Firebase", e)
            }
        }
    }
    
    /**
     * Đồng bộ tất cả dữ liệu từ Firebase về SQLite (chạy trên background thread)
     */
    fun syncAllDataFromFirebaseAsync(userId: String, onComplete: (Boolean) -> Unit = {}) {
        syncScope.launch {
            try {
                Log.d(TAG, "Starting sync all data from Firebase for user: $userId")
                
                val userSynced = syncUserFromFirebase(userId)
                delay(SYNC_DELAY_MS)
                val cartSynced = syncCartFromFirebase(userId)
                delay(SYNC_DELAY_MS)
                val ordersSynced = syncOrdersFromFirebase(userId)
                delay(SYNC_DELAY_MS)
                val addressesSynced = syncAddressesFromFirebase(userId)
                delay(SYNC_DELAY_MS)
                val wishlistSynced = syncWishlistFromFirebase(userId)
                
                val success = userSynced || cartSynced || ordersSynced || addressesSynced || wishlistSynced
                Log.d(TAG, "Sync all data from Firebase completed: success=$success")
                
                withContext(Dispatchers.Main) {
                    onComplete(success)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error syncing all data from Firebase", e)
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
            }
        }
    }
    
    /**
     * Đồng bộ User lên Firebase
     */
    private suspend fun syncUserToFirebase(user: UserModel): Boolean = withContext(Dispatchers.IO) {
        try {
            val userRef = firebaseDatabase.getReference("users").child(user.userId)
            val userMap = mapOf(
                "userId" to user.userId,
                "phoneNumber" to user.phoneNumber,
                "fullName" to user.fullName,
                "email" to user.email,
                "avatarPath" to user.avatarPath,
                "createdAt" to user.createdAt,
                "password" to user.password,
                "isAdmin" to user.isAdmin
            )
            userRef.setValue(userMap).await()
            Log.d(TAG, "User synced to Firebase: ${user.userId}")
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing user to Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Đồng bộ User từ Firebase về SQLite
     */
    private suspend fun syncUserFromFirebase(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val userRef = firebaseDatabase.getReference("users").child(userId)
            val snapshot = userRef.get().await()
            
            if (snapshot.exists()) {
                val userIdValue = snapshot.child("userId").getValue(String::class.java) ?: ""
                val phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java) ?: ""
                val fullName = snapshot.child("fullName").getValue(String::class.java) ?: ""
                val email = snapshot.child("email").getValue(String::class.java) ?: ""
                val password = snapshot.child("password").getValue(String::class.java) ?: ""
                val avatarPath = snapshot.child("avatarPath").getValue(String::class.java) ?: ""
                val createdAt = snapshot.child("createdAt").getValue(Long::class.java) ?: System.currentTimeMillis()
                val isAdmin = snapshot.child("isAdmin").getValue(Boolean::class.java) ?: false
                
                val user = UserModel(
                    userId = userIdValue,
                    phoneNumber = phoneNumber,
                    fullName = fullName,
                    email = email,
                    password = password,
                    avatarPath = avatarPath,
                    createdAt = createdAt,
                    isAdmin = isAdmin
                )
                
                val userManager = UserManager(context)
                userManager.saveUser(user)
                Log.d(TAG, "User synced from Firebase: $userId")
                return@withContext true
            }
            return@withContext false
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing user from Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Đồng bộ Cart lên Firebase
     */
    private suspend fun syncCartToFirebase(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val cartManager = CartManager(context)
            val cartList = cartManager.getCartList()
            val cartRef = firebaseDatabase.getReference("carts").child(userId)
            
            // Xóa cart cũ trên Firebase
            cartRef.removeValue().await()
            
            // Thêm cart mới
            cartList.forEach { cartItem ->
                val itemKey = cartItem.item.title.replace(".", "_")
                    .replace("#", "_").replace("$", "_")
                    .replace("[", "_").replace("]", "_")
                val cartMap = mapOf(
                    "item" to gson.toJson(cartItem.item),
                    "quantity" to cartItem.quantity
                )
                cartRef.child(itemKey).setValue(cartMap).await()
            }
            
            Log.d(TAG, "Cart synced to Firebase: ${cartList.size} items")
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing cart to Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Đồng bộ Cart từ Firebase về SQLite
     */
    private suspend fun syncCartFromFirebase(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val cartRef = firebaseDatabase.getReference("carts").child(userId)
            val snapshot = cartRef.get().await()
            
            if (snapshot.exists()) {
                val cartManager = CartManager(context)
                cartManager.clearCart()
                
                snapshot.children.forEach { child ->
                    try {
                        val itemJson = child.child("item").getValue(String::class.java) ?: return@forEach
                        val quantity = child.child("quantity").getValue(Int::class.java) ?: 1
                        val item = gson.fromJson(itemJson, ItemsModel::class.java)
                        repeat(quantity) {
                            cartManager.addToCart(item)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing cart item", e)
                    }
                }
                
                Log.d(TAG, "Cart synced from Firebase")
                return@withContext true
            }
            return@withContext false
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing cart from Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Đồng bộ Orders lên Firebase
     */
    private suspend fun syncOrdersToFirebase(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val orderManager = OrderManager(context)
            val orders = orderManager.getAllOrders()
            val ordersRef = firebaseDatabase.getReference("orders").child(userId)
            
            orders.forEach { order ->
                val orderMap = mapOf(
                    "orderId" to order.orderId,
                    "items" to gson.toJson(order.items),
                    "totalPrice" to order.totalPrice,
                    "orderDate" to order.orderDate,
                    "status" to order.status,
                    "deliveryAddress" to order.deliveryAddress,
                    "phoneNumber" to order.phoneNumber,
                    "customerName" to order.customerName,
                    "paymentMethod" to order.paymentMethod
                )
                ordersRef.child(order.orderId).setValue(orderMap).await()
            }
            
            Log.d(TAG, "Orders synced to Firebase: ${orders.size} orders")
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing orders to Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Đồng bộ Orders từ Firebase về SQLite
     */
    private suspend fun syncOrdersFromFirebase(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val ordersRef = firebaseDatabase.getReference("orders").child(userId)
            val snapshot = ordersRef.get().await()
            
            if (snapshot.exists()) {
                val orderManager = OrderManager(context)
                val db = DatabaseHelper(context).writableDatabase
                
                snapshot.children.forEach { child ->
                    try {
                        val orderId = child.child("orderId").getValue(String::class.java) ?: return@forEach
                        
                        // Kiểm tra xem order đã tồn tại chưa
                        if (orderManager.getOrderById(orderId) == null) {
                            val itemsJson = child.child("items").getValue(String::class.java) ?: ""
                            val totalPrice = child.child("totalPrice").getValue(Double::class.java) ?: 0.0
                            val orderDate = child.child("orderDate").getValue(Long::class.java) ?: System.currentTimeMillis()
                            val status = child.child("status").getValue(String::class.java) ?: "Pending"
                            val deliveryAddress = child.child("deliveryAddress").getValue(String::class.java) ?: ""
                            val phoneNumber = child.child("phoneNumber").getValue(String::class.java) ?: ""
                            val customerName = child.child("customerName").getValue(String::class.java) ?: ""
                            val paymentMethod = child.child("paymentMethod").getValue(String::class.java) ?: "Tiền mặt"
                            
                            val values = android.content.ContentValues().apply {
                                put(DatabaseHelper.COL_ORDER_ID, orderId)
                                put(DatabaseHelper.COL_ORDER_USER_ID, userId)
                                put(DatabaseHelper.COL_ITEMS_JSON, itemsJson)
                                put(DatabaseHelper.COL_TOTAL_PRICE, totalPrice)
                                put(DatabaseHelper.COL_ORDER_DATE, orderDate)
                                put(DatabaseHelper.COL_STATUS, status)
                                put(DatabaseHelper.COL_DELIVERY_ADDRESS, deliveryAddress)
                                put(DatabaseHelper.COL_ORDER_PHONE, phoneNumber)
                                put(DatabaseHelper.COL_CUSTOMER_NAME, customerName)
                                put(DatabaseHelper.COL_PAYMENT_METHOD, paymentMethod)
                            }
                            db.insert(DatabaseHelper.TABLE_ORDERS, null, values)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing order", e)
                    }
                }
                
                db.close()
                Log.d(TAG, "Orders synced from Firebase")
                return@withContext true
            }
            return@withContext false
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing orders from Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Đồng bộ Addresses lên Firebase
     */
    private suspend fun syncAddressesToFirebase(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val addressManager = AddressManager(context)
            val addresses = addressManager.getAllAddresses()
            val addressesRef = firebaseDatabase.getReference("addresses").child(userId)
            
            addressesRef.removeValue().await()
            
            addresses.forEach { address ->
                val addressMap = mapOf(
                    "id" to address.id,
                    "name" to address.name,
                    "phone" to address.phone,
                    "address" to address.address,
                    "isDefault" to address.isDefault
                )
                addressesRef.child(address.id).setValue(addressMap).await()
            }
            
            Log.d(TAG, "Addresses synced to Firebase: ${addresses.size} addresses")
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing addresses to Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Đồng bộ Addresses từ Firebase về SQLite
     */
    private suspend fun syncAddressesFromFirebase(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val addressesRef = firebaseDatabase.getReference("addresses").child(userId)
            val snapshot = addressesRef.get().await()
            
            if (snapshot.exists()) {
                val addressManager = AddressManager(context)
                
                snapshot.children.forEach { child ->
                    try {
                        val addressId = child.child("id").getValue(String::class.java) ?: return@forEach
                        val name = child.child("name").getValue(String::class.java) ?: ""
                        val phone = child.child("phone").getValue(String::class.java) ?: ""
                        val address = child.child("address").getValue(String::class.java) ?: ""
                        val isDefault = child.child("isDefault").getValue(Boolean::class.java) ?: false
                        
                        val addressModel = AddressModel(
                            id = addressId,
                            name = name,
                            phone = phone,
                            address = address,
                            isDefault = isDefault
                        )
                        
                        addressManager.saveAddress(addressModel)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing address", e)
                    }
                }
                
                Log.d(TAG, "Addresses synced from Firebase")
                return@withContext true
            }
            return@withContext false
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing addresses from Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Đồng bộ Wishlist lên Firebase
     */
    private suspend fun syncWishlistToFirebase(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val wishlistManager = WishlistManager(context)
            val wishlist = wishlistManager.getWishlist()
            val wishlistRef = firebaseDatabase.getReference("wishlist").child(userId)
            
            wishlistRef.removeValue().await()
            
            wishlist.forEach { item ->
                val itemKey = item.title.replace(".", "_")
                    .replace("#", "_").replace("$", "_")
                    .replace("[", "_").replace("]", "_")
                val itemMap = mapOf(
                    "item" to gson.toJson(item)
                )
                wishlistRef.child(itemKey).setValue(itemMap).await()
            }
            
            Log.d(TAG, "Wishlist synced to Firebase: ${wishlist.size} items")
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing wishlist to Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Đồng bộ Wishlist từ Firebase về SQLite
     */
    private suspend fun syncWishlistFromFirebase(userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val wishlistRef = firebaseDatabase.getReference("wishlist").child(userId)
            val snapshot = wishlistRef.get().await()
            
            if (snapshot.exists()) {
                val wishlistManager = WishlistManager(context)
                wishlistManager.clearWishlist()
                
                snapshot.children.forEach { child ->
                    try {
                        val itemJson = child.child("item").getValue(String::class.java) ?: return@forEach
                        val item = gson.fromJson(itemJson, ItemsModel::class.java)
                        wishlistManager.addToWishlist(item)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing wishlist item", e)
                    }
                }
                
                Log.d(TAG, "Wishlist synced from Firebase")
                return@withContext true
            }
            return@withContext false
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing wishlist from Firebase", e)
            return@withContext false
        }
    }
    
    /**
     * Tìm user theo phone number trên Firebase
     */
    fun findUserByPhoneNumberAsync(phoneNumber: String, onComplete: (UserModel?) -> Unit) {
        syncScope.launch {
            try {
                if (phoneNumber.isEmpty()) {
                    Log.w(TAG, "Empty phone number provided")
                    withContext(Dispatchers.Main) {
                        onComplete(null)
                    }
                    return@launch
                }
                
                val trimmedPhone = phoneNumber.trim()
                Log.d(TAG, "=== Searching for user with phone: '$trimmedPhone' ===")
                
                val usersRef = firebaseDatabase.getReference("users")
                
                // Fallback: Lấy tất cả users và tìm trong code (đảm bảo hoạt động)
                try {
                    Log.d(TAG, "Getting all users from Firebase...")
                    val allUsersSnapshot = usersRef.get().await()
                    val userCount = allUsersSnapshot.children.count()
                    Log.d(TAG, "Total users in Firebase: $userCount")
                    
                    if (userCount == 0) {
                        Log.w(TAG, "No users found in Firebase")
                        withContext(Dispatchers.Main) {
                            onComplete(null)
                        }
                        return@launch
                    }
                    
                    var foundUser: UserModel? = null
                    for (child in allUsersSnapshot.children) {
                        try {
                            val phone = child.child("phoneNumber").getValue(String::class.java) ?: ""
                            val phoneTrimmed = phone.trim()
                            
                            Log.d(TAG, "Checking user - phone in DB: '$phone', trimmed: '$phoneTrimmed', searching for: '$trimmedPhone'")
                            
                            if (phoneTrimmed == trimmedPhone) {
                                val userId = child.child("userId").getValue(String::class.java) ?: ""
                                val fullName = child.child("fullName").getValue(String::class.java) ?: ""
                                val email = child.child("email").getValue(String::class.java) ?: ""
                                val password = child.child("password").getValue(String::class.java) ?: ""
                                val avatarPath = child.child("avatarPath").getValue(String::class.java) ?: ""
                                val isAdmin = child.child("isAdmin").getValue(Boolean::class.java) ?: false
                                val createdAtValue = child.child("createdAt").getValue(Any::class.java)
                                val createdAt = when (createdAtValue) {
                                    is Long -> createdAtValue
                                    is Number -> createdAtValue.toLong()
                                    is String -> createdAtValue.toLongOrNull() ?: System.currentTimeMillis()
                                    else -> System.currentTimeMillis()
                                }
                                
                                Log.d(TAG, "✓ Found matching user!")
                                Log.d(TAG, "  - userId: $userId")
                                Log.d(TAG, "  - phoneNumber: '$phone'")
                                Log.d(TAG, "  - fullName: '$fullName'")
                                Log.d(TAG, "  - password: '${password.take(3)}...' (length: ${password.length})")
                                Log.d(TAG, "  - isAdmin: $isAdmin")
                                
                                foundUser = UserModel(
                                    userId = userId,
                                    phoneNumber = phone,
                                    fullName = fullName,
                                    email = email,
                                    password = password,
                                    avatarPath = avatarPath,
                                    createdAt = createdAt,
                                    isAdmin = isAdmin
                                )
                                break
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing user data", e)
                            continue
                        }
                    }
                    
                    if (foundUser != null) {
                        Log.d(TAG, "=== Successfully found user in Firebase ===")
                        withContext(Dispatchers.Main) {
                            onComplete(foundUser)
                        }
                    } else {
                        Log.w(TAG, "=== User not found in Firebase: '$trimmedPhone' ===")
                        withContext(Dispatchers.Main) {
                            onComplete(null)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error getting users from Firebase: ${e.message}", e)
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        onComplete(null)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error finding user in Firebase: ${e.message}", e)
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onComplete(null)
                }
            }
        }
    }
    
    /**
     * Đồng bộ User lên Firebase (public method)
     */
    fun syncUserToFirebaseAsync(user: UserModel) {
        syncScope.launch {
            syncUserToFirebase(user)
        }
    }
    
    /**
     * Hủy tất cả các coroutines đang chạy
     */
    fun cancel() {
        syncScope.cancel()
    }
}
