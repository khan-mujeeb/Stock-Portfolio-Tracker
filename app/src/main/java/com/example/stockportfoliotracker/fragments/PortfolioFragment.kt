package com.example.stockportfoliotracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stockportfoliotracker.activity.AddStock
import com.example.stockportfoliotracker.databinding.FragmentPortfolioBinding


class PortfolioFragment : Fragment() {

    var binding: FragmentPortfolioBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPortfolioBinding.inflate(inflater, container, false)


        binding!!.addBtn.setOnClickListener {
            Intent(requireContext(), AddStock::class.java).also {
                startActivity(it)
            }
        }

        return binding!!.root
    }


}