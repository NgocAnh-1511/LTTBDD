package com.example.coffeeshop.Manager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.coffeeshop.Database.DatabaseHelper
import com.example.coffeeshop.Domain.CartModel
import com.example.coffeeshop.Domain.OrderModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class OrderManager(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val gson = Gson()
    private val userManager = UserManager(context)
    private val syncManager = FirebaseSyncManager(context)

    private fun getWritableDatabase(): SQLiteDatabase {
        return dbHelper.writableDatabase
    }

    private fun getReadableDatabase(): SQLiteDatabase {
        return dbHelper.readableDatabase
    }
    
    private fun getCurrentUserId(): String? {
        return userManager.getUserId()
    }

    fun createOrder(items: MutableList<CartModel>, 
                   totalPrice: Double,
                   deliveryAddress: String = "",
                   phoneNumber: String = "",
                   customerName: String = "",
                   paymentMethod: String = "Tiền mặt"): OrderModel {
        val order = OrderModel(
            orderId = UUID.randomUUID().toString(),
            items = items.toMutableList(),
            totalPrice = totalPrice,
            orderDate = System.currentTimeMillis(),
            status = "Pending",
            deliveryAddress = deliveryAddress,
            phoneNumber = phoneNumber,
            customerName = customerName,
            paymentMethod = paymentMethod
        )
        
        val userId = getCurrentUserId() ?: return order
        val db = getWritableDatabase()
        val itemsJson = gson.toJson(items)
        
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_ORDER_ID, order.orderId)
            put(DatabaseHelper.COL_ORDER_USER_ID, userId)
            put(DatabaseHelper.COL_ITEMS_JSON, itemsJson)
            put(DatabaseHelper.COL_TOTAL_PRICE, totalPrice)
            put(DatabaseHelper.COL_ORDER_DATE, order.orderDate)
            put(DatabaseHelper.COL_STATUS, order.status)
            put(DatabaseHelper.COL_DELIVERY_ADDRESS, deliveryAddress)
            put(DatabaseHelper.COL_ORDER_PHONE, phoneNumber)
            put(DatabaseHelper.COL_CUSTOMER_NAME, customerName)
            put(DatabaseHelper.COL_PAYMENT_METHOD, paymentMethod)
        }
        
        db.insert(DatabaseHelper.TABLE_ORDERS, null, values)
        
        // Đồng bộ lên Firebase
        syncManager.syncAllDataToFirebaseAsync(userId)
        
        return order
    }

    fun getAllOrders(): MutableList<OrderModel> {
        val userId = getCurrentUserId() ?: return mutableListOf()
        val db = getReadableDatabase()
        val orders = mutableListOf<OrderModel>()
        
        val cursor = db.query(
            DatabaseHelper.TABLE_ORDERS,
            null,
            "${DatabaseHelper.COL_ORDER_USER_ID} = ?",
            arrayOf(userId),
            null, null,
            "${DatabaseHelper.COL_ORDER_DATE} DESC" // Sort by date descending
        )

        try {
            while (cursor.moveToNext()) {
                val itemsJsonIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ITEMS_JSON)
                val orderIdIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)
                val totalPriceIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOTAL_PRICE)
                val orderDateIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE)
                val statusIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS)
                val deliveryAddressIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DELIVERY_ADDRESS)
                val phoneIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_PHONE)
                val customerNameIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_NAME)
                val paymentMethodIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PAYMENT_METHOD)
                
                val itemsJson = cursor.getString(itemsJsonIndex) ?: continue
                val type = object : TypeToken<MutableList<CartModel>>() {}.type
                val items = try {
                    gson.fromJson<MutableList<CartModel>>(itemsJson, type) ?: mutableListOf()
                } catch (e: Exception) {
                    mutableListOf()
                }
                
                val order = OrderModel(
                    orderId = cursor.getString(orderIdIndex) ?: "",
                    items = items,
                    totalPrice = cursor.getDouble(totalPriceIndex),
                    orderDate = cursor.getLong(orderDateIndex),
                    status = (cursor.getString(statusIndex) ?: "Pending").trim(),
                    deliveryAddress = if (!cursor.isNull(deliveryAddressIndex)) cursor.getString(deliveryAddressIndex) ?: "" else "",
                    phoneNumber = if (!cursor.isNull(phoneIndex)) cursor.getString(phoneIndex) ?: "" else "",
                    customerName = if (!cursor.isNull(customerNameIndex)) cursor.getString(customerNameIndex) ?: "" else "",
                    paymentMethod = if (!cursor.isNull(paymentMethodIndex)) cursor.getString(paymentMethodIndex) ?: "Tiền mặt" else "Tiền mặt"
                )
                orders.add(order)
            }
        } catch (e: Exception) {
            // Return empty list on error
        } finally {
            cursor.close()
        }
        
        return orders
    }

    fun getOrderById(orderId: String): OrderModel? {
        val db = getReadableDatabase()
        val cursor = db.query(
            DatabaseHelper.TABLE_ORDERS,
            null,
            "${DatabaseHelper.COL_ORDER_ID} = ?",
            arrayOf(orderId),
            null, null, null, "1"
        )

        return try {
            if (cursor.moveToFirst()) {
                val itemsJsonIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ITEMS_JSON)
                val orderIdIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)
                val totalPriceIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOTAL_PRICE)
                val orderDateIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE)
                val statusIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS)
                val deliveryAddressIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DELIVERY_ADDRESS)
                val phoneIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_PHONE)
                val customerNameIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_NAME)
                val paymentMethodIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PAYMENT_METHOD)
                
                val itemsJson = cursor.getString(itemsJsonIndex) ?: ""
                val type = object : TypeToken<MutableList<CartModel>>() {}.type
                val items = try {
                    gson.fromJson<MutableList<CartModel>>(itemsJson, type) ?: mutableListOf()
                } catch (e: Exception) {
                    mutableListOf()
                }
                
                val order = OrderModel(
                    orderId = cursor.getString(orderIdIndex) ?: "",
                    items = items,
                    totalPrice = cursor.getDouble(totalPriceIndex),
                    orderDate = cursor.getLong(orderDateIndex),
                    status = (cursor.getString(statusIndex) ?: "Pending").trim(),
                    deliveryAddress = if (!cursor.isNull(deliveryAddressIndex)) cursor.getString(deliveryAddressIndex) ?: "" else "",
                    phoneNumber = if (!cursor.isNull(phoneIndex)) cursor.getString(phoneIndex) ?: "" else "",
                    customerName = if (!cursor.isNull(customerNameIndex)) cursor.getString(customerNameIndex) ?: "" else "",
                    paymentMethod = if (!cursor.isNull(paymentMethodIndex)) cursor.getString(paymentMethodIndex) ?: "Tiền mặt" else "Tiền mặt"
                )
                cursor.close()
                order
            } else {
                cursor.close()
                null
            }
        } catch (e: Exception) {
            cursor.close()
            null
        }
    }

    fun updateOrderStatus(orderId: String, status: String): Boolean {
        val db = getWritableDatabase()
        val statusTrimmed = status.trim()
        
        android.util.Log.d("OrderManager", "Updating order $orderId status to: '$statusTrimmed'")
        
        // First, get the order to find its user_id
        val order = getOrderById(orderId)
        if (order == null) {
            android.util.Log.e("OrderManager", "Order not found: $orderId")
            return false
        }
        
        // Get user_id from the order in database
        val cursor = db.query(
            DatabaseHelper.TABLE_ORDERS,
            arrayOf(DatabaseHelper.COL_ORDER_USER_ID),
            "${DatabaseHelper.COL_ORDER_ID} = ?",
            arrayOf(orderId),
            null, null, null, "1"
        )
        
        var orderUserId: String? = null
        try {
            if (cursor.moveToFirst()) {
                val userIdIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_USER_ID)
                orderUserId = cursor.getString(userIdIndex)
            }
        } finally {
            cursor.close()
        }
        
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_STATUS, statusTrimmed)
        }
        val result = db.update(
            DatabaseHelper.TABLE_ORDERS,
            values,
            "${DatabaseHelper.COL_ORDER_ID} = ?",
            arrayOf(orderId)
        ) > 0
        
        android.util.Log.d("OrderManager", "Update result: $result, user_id: $orderUserId")
        
        // Verify the update
        if (result) {
            val updatedOrder = getOrderById(orderId)
            android.util.Log.d("OrderManager", "Verified order status after update: '${updatedOrder?.status}'")
        }
        
        // Sync to Firebase - sync the order owner's data
        if (result && orderUserId != null) {
            syncManager.syncAllDataToFirebaseAsync(orderUserId)
        }
        return result
    }

    /**
     * Récupère toutes les commandes pour l'admin (sans filtre par user_id)
     */
    fun getAllOrdersForAdmin(): MutableList<OrderModel> {
        val db = getReadableDatabase()
        val orders = mutableListOf<OrderModel>()
        
        val cursor = db.query(
            DatabaseHelper.TABLE_ORDERS,
            null,
            null, // Pas de filtre WHERE
            null,
            null, null,
            "${DatabaseHelper.COL_ORDER_DATE} DESC" // Sort by date descending
        )

        try {
            while (cursor.moveToNext()) {
                val itemsJsonIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ITEMS_JSON)
                val orderIdIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)
                val totalPriceIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOTAL_PRICE)
                val orderDateIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE)
                val statusIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS)
                val deliveryAddressIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DELIVERY_ADDRESS)
                val phoneIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_PHONE)
                val customerNameIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_NAME)
                val paymentMethodIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PAYMENT_METHOD)
                val userIdIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_USER_ID)
                
                val itemsJson = cursor.getString(itemsJsonIndex) ?: continue
                val type = object : TypeToken<MutableList<CartModel>>() {}.type
                val items = try {
                    gson.fromJson<MutableList<CartModel>>(itemsJson, type) ?: mutableListOf()
                } catch (e: Exception) {
                    mutableListOf()
                }
                
                val order = OrderModel(
                    orderId = cursor.getString(orderIdIndex) ?: "",
                    items = items,
                    totalPrice = cursor.getDouble(totalPriceIndex),
                    orderDate = cursor.getLong(orderDateIndex),
                    status = (cursor.getString(statusIndex) ?: "Pending").trim(),
                    deliveryAddress = if (!cursor.isNull(deliveryAddressIndex)) cursor.getString(deliveryAddressIndex) ?: "" else "",
                    phoneNumber = if (!cursor.isNull(phoneIndex)) cursor.getString(phoneIndex) ?: "" else "",
                    customerName = if (!cursor.isNull(customerNameIndex)) cursor.getString(customerNameIndex) ?: "" else "",
                    paymentMethod = if (!cursor.isNull(paymentMethodIndex)) cursor.getString(paymentMethodIndex) ?: "Tiền mặt" else "Tiền mặt"
                )
                orders.add(order)
            }
        } catch (e: Exception) {
            // Return empty list on error
        } finally {
            cursor.close()
        }
        
        return orders
    }

    fun cancelOrder(orderId: String): Boolean {
        return updateOrderStatus(orderId, "Cancelled")
    }

    fun deleteOrder(orderId: String): Boolean {
        val userId = getCurrentUserId() ?: return false
        val db = getWritableDatabase()
        val result = db.delete(
            DatabaseHelper.TABLE_ORDERS,
            "${DatabaseHelper.COL_ORDER_ID} = ?",
            arrayOf(orderId)
        ) > 0
        if (result) {
            syncManager.syncAllDataToFirebaseAsync(userId)
        }
        return result
    }

}

