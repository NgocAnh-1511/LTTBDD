# Hướng Dẫn: Chuyển Android App Từ Firebase Sang API

## 🎯 Mục Tiêu

Thay đổi Android app để lấy dữ liệu từ API thay vì Firebase Realtime Database.

## 📋 API Endpoints Mới

```
Base URL: https://lttbdd-production.up.railway.app/api/public

GET /public/products              - Lấy tất cả sản phẩm
GET /public/products?categoryId=X - Lọc theo danh mục
GET /public/products/:id          - Lấy chi tiết sản phẩm
GET /public/categories            - Lấy tất cả danh mục
GET /public/categories/:id        - Lấy chi tiết danh mục
```

## 🔧 Cần Thay Đổi

### 1. MainRepository.kt

**Trước (Firebase):**
```kotlin
private val firebaseDatabase = FirebaseDatabase.getInstance()

fun loadCategory(): LiveData<MutableList<CategoryModel>> {
    val listData = MutableLiveData<MutableList<CategoryModel>>()
    val ref = firebaseDatabase.getReference("Category")
    ref.addValueEventListener(...)
    return listData
}
```

**Sau (API):**
```kotlin
private val apiClient = ApiClient.getInstance()

fun loadCategory(): LiveData<MutableList<CategoryModel>> {
    val listData = MutableLiveData<MutableList<CategoryModel>>()
    
    apiClient.getCategories().enqueue(object : Callback<List<CategoryModel>> {
        override fun onResponse(call: Call<List<CategoryModel>>, response: Response<List<CategoryModel>>) {
            if (response.isSuccessful) {
                listData.value = response.body()?.toMutableList() ?: mutableListOf()
            }
        }
        override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
            listData.value = mutableListOf()
        }
    })
    
    return listData
}
```

### 2. Tạo API Interface

**Tạo file: `Network/ProductApi.kt`**
```kotlin
interface ProductApi {
    @GET("public/products")
    fun getProducts(@Query("categoryId") categoryId: Int? = null): Call<List<ProductResponse>>
    
    @GET("public/products/{id}")
    fun getProduct(@Path("id") id: Int): Call<ProductResponse>
}

interface CategoryApi {
    @GET("public/categories")
    fun getCategories(): Call<List<CategoryResponse>>
    
    @GET("public/categories/{id}")
    fun getCategory(@Path("id") id: Int): Call<CategoryResponse>
}
```

### 3. Cập Nhật ApiClient.kt

**Thêm methods:**
```kotlin
fun getProductApi(): ProductApi {
    return retrofit.create(ProductApi::class.java)
}

fun getCategoryApi(): CategoryApi {
    return retrofit.create(CategoryApi::class.java)
}
```

### 4. Data Models

**Cần tạo response models phù hợp với API:**

```kotlin
data class ProductResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val originalPrice: Double?,
    val imageUrl: String?,
    val stock: Int,
    val isActive: Boolean,
    val categoryId: Int?,
    val category: CategoryResponse?
) {
    fun toItemsModel(): ItemsModel {
        return ItemsModel(
            title = name,
            description = description ?: "",
            picUrl = if (imageUrl != null) arrayListOf(imageUrl) else arrayListOf(),
            price = price,
            rating = 0.0,
            numberInCart = 0,
            extra = "",
            categoryId = categoryId?.toString() ?: ""
        )
    }
}

data class CategoryResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?
) {
    fun toCategoryModel(): CategoryModel {
        return CategoryModel(
            id = id.toString(),
            title = name,
            pic = imageUrl ?: ""
        )
    }
}
```

## 🔄 Thay Đổi MainRepository

### loadCategory()

```kotlin
fun loadCategory(): LiveData<MutableList<CategoryModel>> {
    val listData = MutableLiveData<MutableList<CategoryModel>>()
    
    apiClient.getCategoryApi().getCategories().enqueue(object : Callback<List<CategoryResponse>> {
        override fun onResponse(
            call: Call<List<CategoryResponse>>,
            response: Response<List<CategoryResponse>>
        ) {
            if (response.isSuccessful) {
                val categories = response.body()?.map { it.toCategoryModel() } ?: emptyList()
                listData.value = categories.toMutableList()
            } else {
                listData.value = mutableListOf()
            }
        }
        
        override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
            Log.e("MainRepository", "Error loading categories", t)
            listData.value = mutableListOf()
        }
    })
    
    return listData
}
```

### loadItemCategory()

```kotlin
fun loadItemCategory(categoryId: String): LiveData<MutableList<ItemsModel>> {
    val itemsLiveData = MutableLiveData<MutableList<ItemsModel>>()
    
    val catId = categoryId.toIntOrNull()
    apiClient.getProductApi().getProducts(catId).enqueue(object : Callback<List<ProductResponse>> {
        override fun onResponse(
            call: Call<List<ProductResponse>>,
            response: Response<List<ProductResponse>>
        ) {
            if (response.isSuccessful) {
                val items = response.body()?.map { it.toItemsModel() } ?: emptyList()
                itemsLiveData.value = items.toMutableList()
            } else {
                itemsLiveData.value = mutableListOf()
            }
        }
        
        override fun onFailure(call: Call<List<ProductResponse>>, t: Throwable) {
            Log.e("MainRepository", "Error loading items for category $categoryId", t)
            itemsLiveData.value = mutableListOf()
        }
    })
    
    return itemsLiveData
}
```

### loadPopular()

```kotlin
fun loadPopular(): LiveData<MutableList<ItemsModel>> {
    val listData = MutableLiveData<MutableList<ItemsModel>>()
    
    // Lấy tất cả sản phẩm và filter popular (có thể thêm field isPopular sau)
    apiClient.getProductApi().getProducts(null).enqueue(object : Callback<List<ProductResponse>> {
        override fun onResponse(
            call: Call<List<ProductResponse>>,
            response: Response<List<ProductResponse>>
        ) {
            if (response.isSuccessful) {
                // Filter popular items (có thể dựa vào category hoặc field khác)
                val popular = response.body()
                    ?.filter { it.isActive }
                    ?.take(8) // Lấy 8 sản phẩm đầu tiên
                    ?.map { it.toItemsModel() } ?: emptyList()
                listData.value = popular.toMutableList()
            } else {
                listData.value = mutableListOf()
            }
        }
        
        override fun onFailure(call: Call<List<ProductResponse>>, t: Throwable) {
            Log.e("MainRepository", "Error loading popular items", t)
            listData.value = mutableListOf()
        }
    })
    
    return listData
}
```

## 🔄 Auto Refresh (Thay Realtime)

**Thêm polling để tự động refresh:**

```kotlin
// Trong ViewModel hoặc Activity
private val handler = Handler(Looper.getMainLooper())
private val refreshRunnable = object : Runnable {
    override fun run() {
        // Refresh data
        viewModel.loadCategory()
        viewModel.loadPopular()
        // Schedule next refresh (mỗi 30 giây)
        handler.postDelayed(this, 30000)
    }
}

override fun onResume() {
    super.onResume()
    handler.post(refreshRunnable)
}

override fun onPause() {
    super.onPause()
    handler.removeCallbacks(refreshRunnable)
}
```

## ✅ Checklist

- [ ] Tạo ProductApi và CategoryApi interfaces
- [ ] Tạo ProductResponse và CategoryResponse models
- [ ] Cập nhật ApiClient để thêm API methods
- [ ] Thay đổi MainRepository.loadCategory()
- [ ] Thay đổi MainRepository.loadItemCategory()
- [ ] Thay đổi MainRepository.loadPopular()
- [ ] Thêm auto refresh (polling)
- [ ] Test trên thiết bị thật
- [ ] Build APK mới

## 🚀 Kết Quả

Sau khi hoàn thành:
- ✅ App lấy dữ liệu từ API thay vì Firebase
- ✅ Cập nhật nhanh hơn (không cần sync Firebase)
- ✅ Đơn giản hơn (chỉ cần quản lý MySQL)
- ✅ Dễ debug hơn

## 📝 Lưu Ý

1. **Offline Mode**: Có thể cache dữ liệu vào SQLite để hiển thị khi offline
2. **Error Handling**: Xử lý lỗi network và hiển thị thông báo phù hợp
3. **Loading States**: Hiển thị loading indicator khi đang fetch data
4. **Pull to Refresh**: Thêm tính năng pull-to-refresh để user có thể refresh thủ công

