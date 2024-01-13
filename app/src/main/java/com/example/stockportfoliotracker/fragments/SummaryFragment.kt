package com.example.stockportfoliotracker.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stockportfoliotracker.R
import com.example.stockportfoliotracker.databinding.FragmentSummaryBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter


class SummaryFragment : Fragment() {

    val labels = arrayListOf("2024", "2023", "2022", "2021", "2020")
    // variables for our bar chart
    lateinit var barChart: BarChart

    // on below line we are creating
    // a variable for bar data
    lateinit var barData: BarData

    // on below line we are creating a
    // variable for bar data set
    lateinit var barDataSet: BarDataSet

    // on below line we are creating array list for bar data
    lateinit var barEntriesList: ArrayList<BarEntry>

    private var binding: FragmentSummaryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSummaryBinding.inflate(inflater, container, false)

        barChart = binding!!.idBarChart

        showBarChart(barChart)


        return binding?.root
    }

    private fun showBarChart(barChart: BarChart) {
        getBarChartData()
        barDataSet = BarDataSet(barEntriesList, null)
        barData = BarData(barDataSet)

        // Set custom labels for each bar
        barDataSet.setValues(labels.mapIndexed { index, label ->
            BarEntry(index.toFloat(), barEntriesList[index].y, label)
        })

        barChart.data = barData

        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 13f
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.setDrawBarShadow(false)
        barChart.setDrawMarkers(false)
        barChart.minimumWidth= 1700


        // Disable zooming and scaling on both axes
        barChart.setScaleEnabled(false)

        // Customize x-axis
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawAxisLine(false) // Disable x-axis axis line
        xAxis.setDrawGridLines(false) // Disable x-axis grid lines
        xAxis.setDrawLabels(true) // Enable x-axis labels
        xAxis.position = XAxis.XAxisPosition.BOTTOM // Place x-axis labels at the bottom
        xAxis.valueFormatter = IndexAxisValueFormatter(labels) // Set custom labels

        // Customize y-axis
        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.setDrawLabels(false) // Enable y-axis labels
        leftAxis.setDrawGridLines(false) // Enable y-axis grid lines
        leftAxis.setDrawAxisLine(false) // Enable y-axis axis line
        leftAxis.valueFormatter = LargeValueFormatter() // Customize y-axis value format

        // Disable right y-axis completely
        val rightAxis: YAxis = barChart.axisRight
        rightAxis.isEnabled = false


    }


    private fun getBarChartData() {
        barEntriesList = ArrayList()

        barEntriesList.add(BarEntry(4f, 45000f))
        barEntriesList.add(BarEntry(3f, 32540f))
        barEntriesList.add(BarEntry(7f, 40000f))
        barEntriesList.add(BarEntry(2f, 17820f))
        barEntriesList.add(BarEntry(1f, 25000f))





    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
