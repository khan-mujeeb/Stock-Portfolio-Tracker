package com.example.stockportfoliotracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stockportfoliotracker.adapter.ViewPagerAdapter
import com.example.stockportfoliotracker.databinding.ActivityMainBinding
import com.example.stockportfoliotracker.fragments.PortfolioFragment
import com.example.stockportfoliotracker.fragments.SummaryFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        val fragments = listOf(SummaryFragment(), PortfolioFragment()) // Replace with your fragment instances
        val adapter = ViewPagerAdapter(fragments, this@MainActivity)
        binding!!.viewPager.adapter = adapter


        TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Summary"
                }
                1 -> {
                    tab.text = "Portfolio"
                }
            }
        }.attach()
    }
}