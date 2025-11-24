package com.example.coffeeshop.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "CoffeeShopDB"
        private const val DATABASE_VERSION = 4  // Tăng version để thêm cột avatar_path

        // Table Users
        const val TABLE_USERS = "users"
        const val COL_USER_ID = "user_id"
        const val COL_PHONE_NUMBER = "phone_number"
        const val COL_FULL_NAME = "full_name"
        const val COL_EMAIL = "email"
        const val COL_PASSWORD = "password"
        const val COL_AVATAR_PATH = "avatar_path"
        const val COL_CREATED_AT = "created_at"
        const val COL_IS_LOGGED_IN = "is_logged_in"
        const val COL_AUTH_TOKEN = "auth_token"

        // Table Cart
        const val TABLE_CART = "cart"
        const val COL_CART_ID = "id"
        const val COL_CART_USER_ID = "user_id"  // Thêm cột user_id
        const val COL_ITEM_TITLE = "item_title"
        const val COL_ITEM_JSON = "item_json"
        const val COL_QUANTITY = "quantity"

        // Table Orders
        const val TABLE_ORDERS = "orders"
        const val COL_ORDER_ID = "order_id"
        const val COL_ORDER_USER_ID = "user_id"  // Thêm cột user_id
        const val COL_ITEMS_JSON = "items_json"
        const val COL_TOTAL_PRICE = "total_price"
        const val COL_ORDER_DATE = "order_date"
        const val COL_STATUS = "status"
        const val COL_DELIVERY_ADDRESS = "delivery_address"
        const val COL_ORDER_PHONE = "phone_number"
        const val COL_CUSTOMER_NAME = "customer_name"
        const val COL_PAYMENT_METHOD = "payment_method"

        // Table Addresses
        const val TABLE_ADDRESSES = "addresses"
        const val COL_ADDRESS_ID = "id"
        const val COL_ADDRESS_USER_ID = "user_id"  // Thêm cột user_id
        const val COL_ADDRESS_NAME = "name"
        const val COL_ADDRESS_PHONE = "phone"
        const val COL_ADDRESS = "address"
        const val COL_IS_DEFAULT = "is_default"

        // Table Wishlist
        const val TABLE_WISHLIST = "wishlist"
        const val COL_WISHLIST_ID = "id"
        const val COL_WISHLIST_USER_ID = "user_id"  // Thêm cột user_id
        const val COL_WISHLIST_ITEM_TITLE = "item_title"
        const val COL_WISHLIST_ITEM_JSON = "item_json"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Users table
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID TEXT PRIMARY KEY,
                $COL_PHONE_NUMBER TEXT NOT NULL,
                $COL_FULL_NAME TEXT,
                $COL_EMAIL TEXT,
                $COL_PASSWORD TEXT,
                $COL_AVATAR_PATH TEXT,
                $COL_CREATED_AT INTEGER NOT NULL,
                $COL_IS_LOGGED_IN INTEGER DEFAULT 0,
                $COL_AUTH_TOKEN TEXT
            )
        """.trimIndent()

        // Create Cart table
        val createCartTable = """
            CREATE TABLE $TABLE_CART (
                $COL_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CART_USER_ID TEXT NOT NULL,
                $COL_ITEM_TITLE TEXT NOT NULL,
                $COL_ITEM_JSON TEXT NOT NULL,
                $COL_QUANTITY INTEGER NOT NULL DEFAULT 1,
                UNIQUE($COL_CART_USER_ID, $COL_ITEM_TITLE)
            )
        """.trimIndent()

        // Create Orders table
        val createOrdersTable = """
            CREATE TABLE $TABLE_ORDERS (
                $COL_ORDER_ID TEXT PRIMARY KEY,
                $COL_ORDER_USER_ID TEXT NOT NULL,
                $COL_ITEMS_JSON TEXT NOT NULL,
                $COL_TOTAL_PRICE REAL NOT NULL,
                $COL_ORDER_DATE INTEGER NOT NULL,
                $COL_STATUS TEXT NOT NULL,
                $COL_DELIVERY_ADDRESS TEXT,
                $COL_ORDER_PHONE TEXT,
                $COL_CUSTOMER_NAME TEXT,
                $COL_PAYMENT_METHOD TEXT
            )
        """.trimIndent()

        // Create Addresses table
        val createAddressesTable = """
            CREATE TABLE $TABLE_ADDRESSES (
                $COL_ADDRESS_ID TEXT PRIMARY KEY,
                $COL_ADDRESS_USER_ID TEXT NOT NULL,
                $COL_ADDRESS_NAME TEXT NOT NULL,
                $COL_ADDRESS_PHONE TEXT NOT NULL,
                $COL_ADDRESS TEXT NOT NULL,
                $COL_IS_DEFAULT INTEGER DEFAULT 0
            )
        """.trimIndent()

        // Create Wishlist table
        val createWishlistTable = """
            CREATE TABLE $TABLE_WISHLIST (
                $COL_WISHLIST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_WISHLIST_USER_ID TEXT NOT NULL,
                $COL_WISHLIST_ITEM_TITLE TEXT NOT NULL,
                $COL_WISHLIST_ITEM_JSON TEXT NOT NULL,
                UNIQUE($COL_WISHLIST_USER_ID, $COL_WISHLIST_ITEM_TITLE)
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createCartTable)
        db.execSQL(createOrdersTable)
        db.execSQL(createAddressesTable)
        db.execSQL(createWishlistTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADDRESSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WISHLIST")
        onCreate(db)
    }
}

