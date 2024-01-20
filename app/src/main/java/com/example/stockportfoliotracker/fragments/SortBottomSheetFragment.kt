package com.example.stockportfoliotracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stockportfoliotracker.adapter.SortFilterAdapter
import com.example.stockportfoliotracker.databinding.FragmentSortBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SortBottomSheetFragment : BottomSheetDialogFragment() {

    private var binding: FragmentSortBottomSheetBinding? = null
    private lateinit var filterList: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSortBottomSheetBinding.inflate(inflater, container, false)

        variableInit()
        subscribeUi()

        return binding!!.root
    }

    private fun variableInit() {
        filterList = listOf("Stock Name",  "Quantity", "Profit Amount", "Invested Amount", "Profit Percentage")
    }

    private fun subscribeUi() {
        binding!!.stockListRc.adapter = SortFilterAdapter(requireContext(), filterList)
    }

    companion object {
        const val TAG = "SortBottomSheetFragment"
    }


}