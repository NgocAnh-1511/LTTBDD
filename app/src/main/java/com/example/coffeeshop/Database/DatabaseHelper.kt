package com.example.coffeeshop.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    
    companion object {
        // Hỗ trợ cả tên có và không có extension .db
        private const val DATABASE_NAME = "CoffeeShopDB.db"  // Có thể dùng "CoffeeShopDB" hoặc "CoffeeShopDB.db"
        private const val DATABASE_VERSION = 2  // Tăng version để xử lý migration
        private const val TAG = "DatabaseHelper"
        
        // Tên bảng
        const val TABLE_USERS = "users"
        
        // Cột bảng users
        const val COL_USER_ID = "id"
        const val COL_EMAIL = "email"
        const val COL_PASSWORD = "password"
        const val COL_PHONE_NUMBER = "phoneNumber"
        const val COL_FULL_NAME = "fullName"
        const val COL_CREATED_AT = "createdAt"
    }
    
    private val appContext: Context = context.applicationContext
    
    init {
        // Copy database từ assets nếu có và database chưa tồn tại
        copyDatabaseFromAssets()
    }
    
    private fun copyDatabaseFromAssets() {
        val dbPath = appContext.getDatabasePath(DATABASE_NAME)
        
        // Nếu database đã tồn tại, không copy
        if (dbPath.exists()) {
            Log.d(TAG, "Database đã tồn tại, bỏ qua copy từ assets")
            return
        }
        
        // Tạo thư mục databases nếu chưa có
        dbPath.parentFile?.mkdirs()
        
        // Thử cả tên có và không có extension
        val possibleNames = listOf(DATABASE_NAME, "CoffeeShopDB", "CoffeeShopDB.db")
        
        for (assetName in possibleNames) {
            try {
                // Thử copy từ assets
                val inputStream: InputStream = appContext.assets.open(assetName)
                val outputStream = FileOutputStream(dbPath)
                
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                
                Log.d(TAG, "✅ Đã copy database từ assets thành công! (File: $assetName)")
                return
            } catch (e: IOException) {
                // Thử tên tiếp theo
                continue
            }
        }
        
        // Nếu không tìm thấy trong assets, sẽ tạo database mới trong onCreate
        Log.d(TAG, "Không tìm thấy database trong assets, sẽ tạo mới")
    }
    
    override fun onCreate(db: SQLiteDatabase) {
        // Kiểm tra xem bảng đã tồn tại chưa (nếu database được copy từ assets)
        val cursor = db.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
            arrayOf(TABLE_USERS)
        )
        
        if (cursor.count == 0) {
            // Chỉ tạo bảng nếu chưa có (fallback - nếu không có database trong assets)
            val createUsersTable = """
                CREATE TABLE $TABLE_USERS (
                    $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COL_EMAIL TEXT,
                    $COL_PASSWORD TEXT NOT NULL,
                    $COL_PHONE_NUMBER TEXT UNIQUE,
                    $COL_FULL_NAME TEXT,
                    $COL_CREATED_AT INTEGER DEFAULT 0
                )
            """.trimIndent()
            
            db.execSQL(createUsersTable)
            Log.d(TAG, "⚠️ Đã tạo bảng users mới (không có database trong assets)")
        } else {
            Log.d(TAG, "✅ Bảng users đã tồn tại (từ database được copy từ assets)")
        }
        
        cursor.close()
    }
    
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Migration từ version 1 sang 2: Xóa cột username nếu có
            try {
                // Kiểm tra xem cột username có tồn tại không
                val cursor = db.rawQuery("PRAGMA table_info($TABLE_USERS)", null)
                var hasUsername = false
                while (cursor.moveToNext()) {
                    val columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    if (columnName == "username") {
                        hasUsername = true
                        break
                    }
                }
                cursor.close()
                
                if (hasUsername) {
                    // Tạo bảng tạm không có cột username
                    db.execSQL("""
                        CREATE TABLE ${TABLE_USERS}_temp (
                            $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                            $COL_EMAIL TEXT,
                            $COL_PASSWORD TEXT NOT NULL,
                            $COL_PHONE_NUMBER TEXT UNIQUE,
                            $COL_FULL_NAME TEXT,
                            $COL_CREATED_AT INTEGER DEFAULT 0
                        )
                    """.trimIndent())
                    
                    // Copy dữ liệu (bỏ cột username)
                    db.execSQL("""
                        INSERT INTO ${TABLE_USERS}_temp ($COL_USER_ID, $COL_EMAIL, $COL_PASSWORD, $COL_PHONE_NUMBER, $COL_FULL_NAME, $COL_CREATED_AT)
                        SELECT $COL_USER_ID, $COL_EMAIL, $COL_PASSWORD, $COL_PHONE_NUMBER, $COL_FULL_NAME, $COL_CREATED_AT
                        FROM $TABLE_USERS
                    """.trimIndent())
                    
                    // Xóa bảng cũ và đổi tên bảng mới
                    db.execSQL("DROP TABLE $TABLE_USERS")
                    db.execSQL("ALTER TABLE ${TABLE_USERS}_temp RENAME TO $TABLE_USERS")
                    
                    Log.d(TAG, "✅ Đã migration database: Xóa cột username")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Migration error", e)
                // Nếu migration thất bại, tạo lại bảng
                db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
                onCreate(db)
            }
        }
    }
    
    fun getWritableDatabaseInstance(): SQLiteDatabase {
        return writableDatabase
    }
    
    fun getReadableDatabaseInstance(): SQLiteDatabase {
        return readableDatabase
    }
}

