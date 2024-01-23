package com.example.stockportfoliotracker.fragments

import YearListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stockportfoliotracker.R
import com.example.stockportfoliotracker.adapter.StockListAdapter
import com.example.stockportfoliotracker.databinding.FragmentFilterBottomSheetBinding
import com.example.stockportfoliotracker.utils.utils
import com.example.stockportfoliotracker.viewmodel.FirebaseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: FirebaseViewModel

    var binding: FragmentFilterBottomSheetBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        variableInit()
        subscribeUi()

        return binding!!.root
    }

    private fun subscribeUi() {
       binding!!.stockListRc.adapter = YearListAdapter(requireContext(), utils.yearList)
    }

    private fun variableInit() {
        viewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
    }


}
