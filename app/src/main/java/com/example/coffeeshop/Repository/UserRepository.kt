package com.example.coffeeshop.Repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.coffeeshop.Database.DatabaseHelper
import com.example.coffeeshop.Domain.UserModel
import java.security.MessageDigest

class UserRepository(private val context: Context) {
    private val TAG = "UserRepository"
    private val dbHelper = DatabaseHelper(context)
    
    interface LoginCallback {
        fun onSuccess(user: UserModel)
        fun onError(message: String)
    }
    
    interface RegisterCallback {
        fun onSuccess(user: UserModel)
        fun onError(message: String)
    }
    
    interface ChangePasswordCallback {
        fun onSuccess(message: String)
        fun onError(message: String)
    }
    
    interface UpdateProfileCallback {
        fun onSuccess(user: UserModel)
        fun onError(message: String)
    }
    
    // Hash password đơn giản (có thể nâng cấp sau)
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
    
    fun login(phoneNumber: String, password: String, callback: LoginCallback) {
        Thread {
            try {
                val db = dbHelper.readableDatabase
                val hashedPassword = hashPassword(password)
                
                // Tìm user theo phoneNumber
                val cursor = db.query(
                    DatabaseHelper.TABLE_USERS,
                    null,
                    "${DatabaseHelper.COL_PHONE_NUMBER} = ?",
                    arrayOf(phoneNumber),
                    null, null, null
                )
                
                if (cursor.moveToFirst()) {
                    val storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD))
                    
                    if (storedPassword == hashedPassword) {
                        val user = UserModel(
                            userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)).toString(),
                            phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE_NUMBER)) ?: "",
                            fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)) ?: "",
                            email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)) ?: "",
                            createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT))
                        )
                        cursor.close()
                        callback.onSuccess(user)
                    } else {
                        cursor.close()
                        callback.onError("Mật khẩu không đúng")
                    }
                } else {
                    cursor.close()
                    callback.onError("Không tìm thấy tài khoản")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Login error", e)
                callback.onError("Lỗi đăng nhập: ${e.message}")
            }
        }.start()
    }
    
    fun register(phoneNumber: String, password: String, callback: RegisterCallback) {
        Thread {
            try {
                val db = dbHelper.writableDatabase
                val hashedPassword = hashPassword(password)
                
                // Kiểm tra xem phoneNumber đã tồn tại chưa
                val checkCursor = db.query(
                    DatabaseHelper.TABLE_USERS,
                    arrayOf(DatabaseHelper.COL_USER_ID),
                    "${DatabaseHelper.COL_PHONE_NUMBER} = ?",
                    arrayOf(phoneNumber),
                    null, null, null
                )
                
                if (checkCursor.moveToFirst()) {
                    checkCursor.close()
                    callback.onError("Số điện thoại đã được sử dụng")
                    return@Thread
                }
                checkCursor.close()
                
                // Thêm user mới - chỉ lưu phoneNumber và password
                val values = android.content.ContentValues().apply {
                    put(DatabaseHelper.COL_PHONE_NUMBER, phoneNumber)
                    put(DatabaseHelper.COL_PASSWORD, hashedPassword)
                    put(DatabaseHelper.COL_CREATED_AT, System.currentTimeMillis())
                }
                
                val userId = db.insert(DatabaseHelper.TABLE_USERS, null, values)
                
                if (userId != -1L) {
                    val user = UserModel(
                        userId = userId.toString(),
                        phoneNumber = phoneNumber,
                        fullName = "",
                        email = "",
                        createdAt = System.currentTimeMillis()
                    )
                    callback.onSuccess(user)
                } else {
                    callback.onError("Đăng ký thất bại")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Register error", e)
                callback.onError("Lỗi đăng ký: ${e.message}")
            }
        }.start()
    }
    
    fun changePassword(userId: String, currentPassword: String, newPassword: String, callback: ChangePasswordCallback) {
        Thread {
            try {
                val db = dbHelper.writableDatabase
                val hashedCurrentPassword = hashPassword(currentPassword)
                val hashedNewPassword = hashPassword(newPassword)
                
                // Kiểm tra mật khẩu hiện tại
                val cursor = db.query(
                    DatabaseHelper.TABLE_USERS,
                    arrayOf(DatabaseHelper.COL_PASSWORD),
                    "${DatabaseHelper.COL_USER_ID} = ?",
                    arrayOf(userId),
                    null, null, null
                )
                
                if (cursor.moveToFirst()) {
                    val storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD))
                    cursor.close()
                    
                    if (storedPassword == hashedCurrentPassword) {
                        // Cập nhật mật khẩu mới
                        val values = android.content.ContentValues().apply {
                            put(DatabaseHelper.COL_PASSWORD, hashedNewPassword)
                        }
                        
                        val rowsAffected = db.update(
                            DatabaseHelper.TABLE_USERS,
                            values,
                            "${DatabaseHelper.COL_USER_ID} = ?",
                            arrayOf(userId)
                        )
                        
                        if (rowsAffected > 0) {
                            callback.onSuccess("Đổi mật khẩu thành công!")
                        } else {
                            callback.onError("Đổi mật khẩu thất bại")
                        }
                    } else {
                        callback.onError("Mật khẩu hiện tại không đúng")
                    }
                } else {
                    cursor.close()
                    callback.onError("Không tìm thấy tài khoản")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Change password error", e)
                callback.onError("Lỗi đổi mật khẩu: ${e.message}")
            }
        }.start()
    }
    
    fun updateProfile(userId: String, fullName: String, email: String, callback: UpdateProfileCallback) {
        Thread {
            try {
                val db = dbHelper.writableDatabase
                
                val values = android.content.ContentValues().apply {
                    put(DatabaseHelper.COL_FULL_NAME, fullName)
                    put(DatabaseHelper.COL_EMAIL, email)
                }
                
                val rowsAffected = db.update(
                    DatabaseHelper.TABLE_USERS,
                    values,
                    "${DatabaseHelper.COL_USER_ID} = ?",
                    arrayOf(userId)
                )
                
                if (rowsAffected > 0) {
                    // Lấy lại thông tin user đã cập nhật
                    val cursor = db.query(
                        DatabaseHelper.TABLE_USERS,
                        null,
                        "${DatabaseHelper.COL_USER_ID} = ?",
                        arrayOf(userId),
                        null, null, null
                    )
                    
                    if (cursor.moveToFirst()) {
                        val user = UserModel(
                            userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)).toString(),
                            phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE_NUMBER)) ?: "",
                            fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)) ?: "",
                            email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)) ?: "",
                            createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT))
                        )
                        cursor.close()
                        callback.onSuccess(user)
                    } else {
                        cursor.close()
                        callback.onError("Không tìm thấy tài khoản")
                    }
                } else {
                    callback.onError("Cập nhật thông tin thất bại")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Update profile error", e)
                callback.onError("Lỗi cập nhật: ${e.message}")
            }
        }.start()
    }
}
