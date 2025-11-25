package com.example.coffeeshop.Activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.RevenueReportAdapter
import com.example.coffeeshop.Domain.OrderModel
import com.example.coffeeshop.Domain.RevenueReportModel
import com.example.coffeeshop.Manager.OrderManager
import com.example.coffeeshop.R
import com.example.coffeeshop.Utils.dpToPx
import com.example.coffeeshop.databinding.ActivityRevenueReportBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RevenueReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRevenueReportBinding
    private lateinit var orderManager: OrderManager
    private lateinit var reportAdapter: RevenueReportAdapter
    private var currentReportType = RevenueReportModel.ReportType.DAILY
    private val reports = mutableListOf<RevenueReportModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRevenueReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle edge-to-edge insets for header
        ViewCompat.setOnApplyWindowInsetsListener(binding.headerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = systemBars.top + 8.dpToPx()
            v.layoutParams = layoutParams
            insets
        }

        orderManager = OrderManager(this)

        setupRecyclerView()
        setupClickListeners()
        loadRevenueReport(RevenueReportModel.ReportType.DAILY)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewReport.layoutManager = LinearLayoutManager(this)
        reportAdapter = RevenueReportAdapter(reports)
        binding.recyclerViewReport.adapter = reportAdapter
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.dailyBtn.setOnClickListener {
            currentReportType = RevenueReportModel.ReportType.DAILY
            updateFilterButtons()
            loadRevenueReport(RevenueReportModel.ReportType.DAILY)
        }

        binding.monthlyBtn.setOnClickListener {
            currentReportType = RevenueReportModel.ReportType.MONTHLY
            updateFilterButtons()
            loadRevenueReport(RevenueReportModel.ReportType.MONTHLY)
        }

        binding.yearlyBtn.setOnClickListener {
            currentReportType = RevenueReportModel.ReportType.YEARLY
            updateFilterButtons()
            loadRevenueReport(RevenueReportModel.ReportType.YEARLY)
        }
    }

    private fun updateFilterButtons() {
        val selectedColor = getColor(R.color.orange)
        val selectedTextColor = getColor(R.color.white)
        val unselectedColor = getColor(R.color.lightCream)
        val unselectedTextColor = getColor(R.color.darkBrown)

        when (currentReportType) {
            RevenueReportModel.ReportType.DAILY -> {
                binding.dailyBtn.setBackgroundColor(selectedColor)
                binding.dailyBtn.setTextColor(selectedTextColor)
                binding.monthlyBtn.setBackgroundColor(unselectedColor)
                binding.monthlyBtn.setTextColor(unselectedTextColor)
                binding.yearlyBtn.setBackgroundColor(unselectedColor)
                binding.yearlyBtn.setTextColor(unselectedTextColor)
            }
            RevenueReportModel.ReportType.MONTHLY -> {
                binding.dailyBtn.setBackgroundColor(unselectedColor)
                binding.dailyBtn.setTextColor(unselectedTextColor)
                binding.monthlyBtn.setBackgroundColor(selectedColor)
                binding.monthlyBtn.setTextColor(selectedTextColor)
                binding.yearlyBtn.setBackgroundColor(unselectedColor)
                binding.yearlyBtn.setTextColor(unselectedTextColor)
            }
            RevenueReportModel.ReportType.YEARLY -> {
                binding.dailyBtn.setBackgroundColor(unselectedColor)
                binding.dailyBtn.setTextColor(unselectedTextColor)
                binding.monthlyBtn.setBackgroundColor(unselectedColor)
                binding.monthlyBtn.setTextColor(unselectedTextColor)
                binding.yearlyBtn.setBackgroundColor(selectedColor)
                binding.yearlyBtn.setTextColor(selectedTextColor)
            }
        }
    }

    private fun loadRevenueReport(reportType: RevenueReportModel.ReportType) {
        val allOrders = orderManager.getAllOrdersForAdmin()
        
        // Chỉ lấy các đơn đã hoàn thành
        val completedOrders = allOrders.filter { it.status == "Completed" }

        if (completedOrders.isEmpty()) {
            binding.recyclerViewReport.visibility = View.GONE
            binding.emptyReportTxt.visibility = View.VISIBLE
            binding.totalRevenueTxt.text = "$0.00"
            binding.totalOrdersTxt.text = "Tổng số đơn: 0"
            reports.clear()
            reportAdapter.updateList(reports)
            return
        }

        binding.recyclerViewReport.visibility = View.VISIBLE
        binding.emptyReportTxt.visibility = View.GONE

        // Tính toán báo cáo theo loại
        val reportMap = mutableMapOf<Long, RevenueReportModel>()

        completedOrders.forEach { order ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = order.orderDate
            }

            val key = when (reportType) {
                RevenueReportModel.ReportType.DAILY -> {
                    // Key theo ngày (bỏ qua giờ, phút, giây)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    calendar.timeInMillis
                }
                RevenueReportModel.ReportType.MONTHLY -> {
                    // Key theo tháng
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    calendar.timeInMillis
                }
                RevenueReportModel.ReportType.YEARLY -> {
                    // Key theo năm
                    calendar.set(Calendar.MONTH, 0)
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    calendar.timeInMillis
                }
            }

            val report = reportMap.getOrPut(key) {
                RevenueReportModel(
                    date = key,
                    dateLabel = "",
                    totalRevenue = 0.0,
                    orderCount = 0,
                    reportType = reportType
                )
            }

            report.totalRevenue += order.totalPrice
            report.orderCount++
        }

        // Chuyển map thành list và sắp xếp theo ngày giảm dần
        reports.clear()
        reports.addAll(reportMap.values.sortedByDescending { it.date })
        reportAdapter.updateList(reports)

        // Tính tổng
        val totalRevenue = reports.sumOf { it.totalRevenue }
        val totalOrders = reports.sumOf { it.orderCount }

        binding.totalRevenueTxt.text = "$${String.format(Locale.getDefault(), "%.2f", totalRevenue)}"
        binding.totalOrdersTxt.text = "Tổng số đơn: $totalOrders"
    }

    override fun onResume() {
        super.onResume()
        loadRevenueReport(currentReportType)
    }
}

