package com.example.coffeeshop

import android.app.Application
import com.example.coffeeshop.Database.DatabaseHelper
import com.example.coffeeshop.Manager.FirebaseSyncManager
import com.example.coffeeshop.Manager.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class App : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private lateinit var syncManager: FirebaseSyncManager
    
    override fun onCreate() {
        super.onCreate()
        try {
            // Khởi tạo database ngay khi app khởi động
            // Database sẽ được tạo tự động nếu chưa tồn tại
            val dbHelper = DatabaseHelper(this)
            // Gọi getReadableDatabase() để đảm bảo database được tạo
            dbHelper.readableDatabase.close()
            
            // Khởi tạo FirebaseSyncManager ngay (không async để tránh lateinit)
            // Nhưng các thao tác sync sẽ chạy trên background thread
            syncManager = FirebaseSyncManager(this)
            
            // Tạo tài khoản admin mặc định nếu chưa tồn tại
            applicationScope.launch(Dispatchers.IO) {
                try {
                    val userManager = UserManager(this@App)
                    userManager.createDefaultAdminIfNotExists()
                } catch (e: Exception) {
                    android.util.Log.e("App", "Error creating default admin", e)
                }
            }
            
            // Đồng bộ dữ liệu từ Firebase về SQLite khi app khởi động (nếu user đã đăng nhập)
            // Chạy trên background thread để tránh ANR
            applicationScope.launch(Dispatchers.IO) {
                try {
                    // Delay một chút để đảm bảo app đã khởi động xong
                    delay(500)
                    
                    val userManager = UserManager(this@App)
                    val currentUser = userManager.getCurrentUser()
                    if (currentUser != null) {
                        // Đồng bộ tất cả dữ liệu từ Firebase về SQLite để đảm bảo dữ liệu mới nhất
                        syncManager.syncAllDataFromFirebaseAsync(currentUser.userId) { success ->
                            android.util.Log.d("App", "All data synced from Firebase on app start: $success")
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("App", "Error syncing user data", e)
                    // Không crash app nếu Firebase có lỗi
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("App", "Error in onCreate", e)
            // Không crash app nếu có lỗi
        }
    }
}

