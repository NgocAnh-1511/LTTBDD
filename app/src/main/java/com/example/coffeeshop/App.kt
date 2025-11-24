package com.example.coffeeshop

import android.app.Application
import com.example.coffeeshop.Database.DatabaseHelper

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Khởi tạo database ngay khi app khởi động
        // Database sẽ được tạo tự động nếu chưa tồn tại
        val dbHelper = DatabaseHelper(this)
        // Gọi getReadableDatabase() để đảm bảo database được tạo
        dbHelper.readableDatabase.close()
    }
}

