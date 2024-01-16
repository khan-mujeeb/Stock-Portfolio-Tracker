package com.example.stockportfoliotracker.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stockportfoliotracker.adapter.ViewPagerAdapter
import com.example.stockportfoliotracker.authentication.LoginActivity
import com.example.stockportfoliotracker.databinding.ActivityMainBinding
import com.example.stockportfoliotracker.fragments.PortfolioFragment
import com.example.stockportfoliotracker.fragments.SummaryFragment
import com.example.stockportfoliotracker.utils.FirebaseUtils
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        checkUserLoggedIn()
        setUpViewPager()
    }

    private fun setUpViewPager() {

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
    private fun checkUserLoggedIn() {
        if (FirebaseUtils.firebaseAuth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}