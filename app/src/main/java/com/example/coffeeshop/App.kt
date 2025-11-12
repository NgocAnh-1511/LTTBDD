package com.example.coffeeshop

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Database sẽ được tạo tự động khi cần (từ assets hoặc tạo mới)
    }
}

