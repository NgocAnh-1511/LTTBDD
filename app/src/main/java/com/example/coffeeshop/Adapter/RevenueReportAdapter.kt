package com.example.coffeeshop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeshop.Domain.RevenueReportModel
import com.example.coffeeshop.databinding.ViewholderRevenueReportBinding
import java.util.Locale

class RevenueReportAdapter(
    private val items: MutableList<RevenueReportModel>
) : RecyclerView.Adapter<RevenueReportAdapter.ViewHolder>() {

    lateinit var context: Context

    class ViewHolder(val binding: ViewholderRevenueReportBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderRevenueReportBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = items[position]

        holder.binding.dateTxt.text = report.getFormattedDate()
        holder.binding.revenueTxt.text = report.getFormattedRevenue()
        holder.binding.orderCountTxt.text = "${report.orderCount} đơn"
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: MutableList<RevenueReportModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
