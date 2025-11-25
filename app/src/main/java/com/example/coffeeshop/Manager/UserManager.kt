package com.example.coffeeshop.Manager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.coffeeshop.Database.DatabaseHelper
import com.example.coffeeshop.Domain.UserModel

class UserManager(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val gson = com.google.gson.Gson()
    private val syncManager = FirebaseSyncManager(context)

    private fun getWritableDatabase(): SQLiteDatabase {
        return dbHelper.writableDatabase
    }

    private fun getReadableDatabase(): SQLiteDatabase {
        return dbHelper.readableDatabase
    }

    fun saveUser(user: UserModel) {
        val db = getWritableDatabase()
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_USER_ID, user.userId)
            put(DatabaseHelper.COL_PHONE_NUMBER, user.phoneNumber)
            put(DatabaseHelper.COL_FULL_NAME, user.fullName)
            put(DatabaseHelper.COL_EMAIL, user.email)
            put(DatabaseHelper.COL_PASSWORD, user.password)
            put(DatabaseHelper.COL_AVATAR_PATH, user.avatarPath)
            put(DatabaseHelper.COL_CREATED_AT, user.createdAt)
            put(DatabaseHelper.COL_IS_LOGGED_IN, 1) // Set logged in
        }

        // Check if user exists
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COL_USER_ID),
            "${DatabaseHelper.COL_USER_ID} = ?",
            arrayOf(user.userId),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            // Update existing user
            db.update(
                DatabaseHelper.TABLE_USERS,
                values,
                "${DatabaseHelper.COL_USER_ID} = ?",
                arrayOf(user.userId)
            )
        } else {
            // Insert new user
            db.insert(DatabaseHelper.TABLE_USERS, null, values)
        }
        cursor.close()
        
        // Đồng bộ lên Firebase (chạy trên background thread)
        syncManager.syncAllDataToFirebaseAsync(user.userId)
    }

    fun getCurrentUser(): UserModel? {
        val db = getReadableDatabase()
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            null,
            "${DatabaseHelper.COL_IS_LOGGED_IN} = ?",
            arrayOf("1"),
            null, null, null, "1"
        )

        return try {
            if (cursor.moveToFirst()) {
                val userIdIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)
                val phoneIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE_NUMBER)
                val fullNameIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)
                val emailIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)
                val passwordIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)
                val avatarPathIndex = try { cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AVATAR_PATH) } catch (e: Exception) { -1 }
                val createdAtIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT)
                
                val user = UserModel(
                    userId = cursor.getString(userIdIndex) ?: "",
                    phoneNumber = cursor.getString(phoneIndex) ?: "",
                    fullName = if (fullNameIndex >= 0 && !cursor.isNull(fullNameIndex)) cursor.getString(fullNameIndex) ?: "" else "",
                    email = if (emailIndex >= 0 && !cursor.isNull(emailIndex)) cursor.getString(emailIndex) ?: "" else "",
                    password = if (passwordIndex >= 0 && !cursor.isNull(passwordIndex)) cursor.getString(passwordIndex) ?: "" else "",
                    avatarPath = if (avatarPathIndex >= 0 && !cursor.isNull(avatarPathIndex)) cursor.getString(avatarPathIndex) ?: "" else "",
                    createdAt = cursor.getLong(createdAtIndex)
                )
                cursor.close()
                user
            } else {
                cursor.close()
                null
            }
        } catch (e: Exception) {
            cursor.close()
            null
        }
    }

    fun isLoggedIn(): Boolean {
        val db = getReadableDatabase()
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COL_IS_LOGGED_IN),
            "${DatabaseHelper.COL_IS_LOGGED_IN} = ?",
            arrayOf("1"),
            null, null, null, "1"
        )
        val isLoggedIn = cursor.moveToFirst()
        cursor.close()
        return isLoggedIn
    }

    fun logout() {
        val db = getWritableDatabase()
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_IS_LOGGED_IN, 0)
            putNull(DatabaseHelper.COL_AUTH_TOKEN)
        }
        db.update(
            DatabaseHelper.TABLE_USERS,
            values,
            "${DatabaseHelper.COL_IS_LOGGED_IN} = ?",
            arrayOf("1")
        )
    }

    fun getUserId(): String? {
        return getCurrentUser()?.userId
    }

    fun getPhoneNumber(): String? {
        return getCurrentUser()?.phoneNumber
    }

    fun saveToken(token: String) {
        val userId = getUserId()
        if (userId != null) {
            val db = getWritableDatabase()
            val values = ContentValues().apply {
                put(DatabaseHelper.COL_AUTH_TOKEN, token)
            }
            db.update(
                DatabaseHelper.TABLE_USERS,
                values,
                "${DatabaseHelper.COL_USER_ID} = ?",
                arrayOf(userId)
            )
        }
    }

    fun getToken(): String? {
        val db = getReadableDatabase()
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COL_AUTH_TOKEN),
            "${DatabaseHelper.COL_IS_LOGGED_IN} = ?",
            arrayOf("1"),
            null, null, null, "1"
        )
        return try {
            if (cursor.moveToFirst()) {
                val tokenIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AUTH_TOKEN)
                val token = if (!cursor.isNull(tokenIndex)) cursor.getString(tokenIndex) else null
                cursor.close()
                token
            } else {
                cursor.close()
                null
            }
        } catch (e: Exception) {
            cursor.close()
            null
        }
    }

    fun clearToken() {
        val userId = getUserId()
        if (userId != null) {
            val db = getWritableDatabase()
            val values = ContentValues().apply {
                putNull(DatabaseHelper.COL_AUTH_TOKEN)
            }
            db.update(
                DatabaseHelper.TABLE_USERS,
                values,
                "${DatabaseHelper.COL_USER_ID} = ?",
                arrayOf(userId)
            )
        }
    }

    /**
     * Kiểm tra xem số điện thoại đã tồn tại trong database chưa
     * Kiểm tra cả SQLite và Firebase
     */
    fun isPhoneNumberExists(phoneNumber: String): Boolean {
        val db = getReadableDatabase()
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COL_PHONE_NUMBER),
            "${DatabaseHelper.COL_PHONE_NUMBER} = ?",
            arrayOf(phoneNumber),
            null, null, null, "1"
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
    
    /**
     * Kiểm tra số điện thoại trên Firebase (async)
     */
    fun isPhoneNumberExistsInFirebase(phoneNumber: String, onComplete: (Boolean) -> Unit) {
        syncManager.findUserByPhoneNumberAsync(phoneNumber) { user ->
            onComplete(user != null)
        }
    }

    /**
     * Đăng ký user mới
     * @param phoneNumber Số điện thoại
     * @param password Mật khẩu
     * @param autoLogin Có tự động đăng nhập sau khi đăng ký không (mặc định false)
     * @return true nếu đăng ký thành công, false nếu số điện thoại đã tồn tại
     */
    fun registerUser(phoneNumber: String, password: String, autoLogin: Boolean = false): Boolean {
        // Kiểm tra số điện thoại đã tồn tại chưa
        if (isPhoneNumberExists(phoneNumber)) {
            return false
        }

        val db = getWritableDatabase()
        val userId = System.currentTimeMillis().toString()
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_USER_ID, userId)
            put(DatabaseHelper.COL_PHONE_NUMBER, phoneNumber)
            put(DatabaseHelper.COL_FULL_NAME, "")
            put(DatabaseHelper.COL_EMAIL, "")
            put(DatabaseHelper.COL_PASSWORD, password)
            put(DatabaseHelper.COL_AVATAR_PATH, "")
            put(DatabaseHelper.COL_CREATED_AT, System.currentTimeMillis())
            put(DatabaseHelper.COL_IS_LOGGED_IN, if (autoLogin) 1 else 0) // Chỉ đăng nhập nếu autoLogin = true
        }

        val result = db.insert(DatabaseHelper.TABLE_USERS, null, values)
        
        // Đồng bộ lên Firebase sau khi đăng ký
        if (result != -1L) {
            val user = UserModel(
                userId = userId,
                phoneNumber = phoneNumber,
                fullName = "",
                email = "",
                password = password,
                avatarPath = "",
                createdAt = System.currentTimeMillis()
            )
            // Đồng bộ user và tất cả dữ liệu lên Firebase
            syncManager.syncAllDataToFirebaseAsync(userId)
        }
        
        return result != -1L
    }

    /**
     * Đăng nhập - kiểm tra phone number và password trong database
     * Nếu không tìm thấy trong SQLite, tìm trên Firebase
     * @return UserModel nếu đăng nhập thành công, null nếu thông tin không đúng
     */
    fun login(phoneNumber: String, password: String): UserModel? {
        // Đầu tiên, tìm trong SQLite
        val db = getReadableDatabase()
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            null,
            "${DatabaseHelper.COL_PHONE_NUMBER} = ? AND ${DatabaseHelper.COL_PASSWORD} = ?",
            arrayOf(phoneNumber, password),
            null, null, null, "1"
        )

        return try {
            if (cursor.moveToFirst()) {
                // Tìm thấy trong SQLite
                val userIdIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)
                val phoneIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE_NUMBER)
                val fullNameIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)
                val emailIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)
                val passwordIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)
                val avatarPathIndex = try { cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AVATAR_PATH) } catch (e: Exception) { -1 }
                val createdAtIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT)
                
                val user = UserModel(
                    userId = cursor.getString(userIdIndex) ?: "",
                    phoneNumber = cursor.getString(phoneIndex) ?: "",
                    fullName = if (fullNameIndex >= 0 && !cursor.isNull(fullNameIndex)) cursor.getString(fullNameIndex) ?: "" else "",
                    email = if (emailIndex >= 0 && !cursor.isNull(emailIndex)) cursor.getString(emailIndex) ?: "" else "",
                    password = if (passwordIndex >= 0 && !cursor.isNull(passwordIndex)) cursor.getString(passwordIndex) ?: "" else "",
                    avatarPath = if (avatarPathIndex >= 0 && !cursor.isNull(avatarPathIndex)) cursor.getString(avatarPathIndex) ?: "" else "",
                    createdAt = cursor.getLong(createdAtIndex)
                )
                cursor.close()
                
                // Set logged in status
                setLoggedInStatus(user.userId, true)
                
                // Đồng bộ từ Firebase về SQLite (để có dữ liệu mới nhất)
                syncManager.syncAllDataFromFirebaseAsync(user.userId)
                
                user
            } else {
                cursor.close()
                // Không tìm thấy trong SQLite, tìm trên Firebase
                null
            }
        } catch (e: Exception) {
            cursor.close()
            null
        }
    }
    
    /**
     * Đăng nhập từ Firebase (được gọi khi không tìm thấy trong SQLite)
     */
    fun loginFromFirebase(phoneNumber: String, password: String, onComplete: (UserModel?) -> Unit) {
        val trimmedPhone = phoneNumber.trim()
        val trimmedPassword = password.trim()
        
        android.util.Log.d("UserManager", "=== Attempting login from Firebase ===")
        android.util.Log.d("UserManager", "Phone: '$trimmedPhone' (length: ${trimmedPhone.length})")
        android.util.Log.d("UserManager", "Password: '${trimmedPassword.take(3)}...' (length: ${trimmedPassword.length})")
        
        syncManager.findUserByPhoneNumberAsync(trimmedPhone) { user ->
            if (user != null) {
                android.util.Log.d("UserManager", "✓ User found in Firebase")
                android.util.Log.d("UserManager", "  - userId: ${user.userId}")
                android.util.Log.d("UserManager", "  - phoneNumber: '${user.phoneNumber}'")
                android.util.Log.d("UserManager", "  - password from DB: '${user.password.take(3)}...' (length: ${user.password.length})")
                android.util.Log.d("UserManager", "  - password input: '${trimmedPassword.take(3)}...' (length: ${trimmedPassword.length})")
                
                // So sánh password (trim cả hai để đảm bảo chính xác)
                val dbPassword = user.password.trim()
                val inputPassword = trimmedPassword
                val passwordMatch = dbPassword == inputPassword
                
                android.util.Log.d("UserManager", "Password comparison:")
                android.util.Log.d("UserManager", "  - DB password: '$dbPassword'")
                android.util.Log.d("UserManager", "  - Input password: '$inputPassword'")
                android.util.Log.d("UserManager", "  - Match: $passwordMatch")
                
                if (passwordMatch) {
                    android.util.Log.d("UserManager", "✓ Password matches! Saving user to SQLite...")
                    // Lưu vào SQLite
                    saveUser(user)
                    // Set logged in status
                    setLoggedInStatus(user.userId, true)
                    // Đồng bộ tất cả dữ liệu từ Firebase về SQLite
                    syncManager.syncAllDataFromFirebaseAsync(user.userId) { success ->
                        android.util.Log.d("UserManager", "Data synced from Firebase: $success")
                    }
                    android.util.Log.d("UserManager", "=== Login from Firebase successful! ===")
                    onComplete(user)
                } else {
                    android.util.Log.w("UserManager", "✗ Password mismatch!")
                    android.util.Log.w("UserManager", "  - DB: '$dbPassword' (${dbPassword.length} chars)")
                    android.util.Log.w("UserManager", "  - Input: '$inputPassword' (${inputPassword.length} chars)")
                    onComplete(null)
                }
            } else {
                android.util.Log.w("UserManager", "✗ User not found in Firebase for phone: '$trimmedPhone'")
                onComplete(null)
            }
        }
    }

    /**
     * Set trạng thái đăng nhập cho user
     */
    private fun setLoggedInStatus(userId: String, isLoggedIn: Boolean) {
        // Đầu tiên, set tất cả users về trạng thái chưa đăng nhập
        val db = getWritableDatabase()
        val unsetValues = ContentValues().apply {
            put(DatabaseHelper.COL_IS_LOGGED_IN, 0)
        }
        db.update(
            DatabaseHelper.TABLE_USERS,
            unsetValues,
            null,
            null
        )
        
        // Sau đó set user hiện tại là đã đăng nhập
        val setValues = ContentValues().apply {
            put(DatabaseHelper.COL_IS_LOGGED_IN, if (isLoggedIn) 1 else 0)
        }
        db.update(
            DatabaseHelper.TABLE_USERS,
            setValues,
            "${DatabaseHelper.COL_USER_ID} = ?",
            arrayOf(userId)
        )
    }
}

