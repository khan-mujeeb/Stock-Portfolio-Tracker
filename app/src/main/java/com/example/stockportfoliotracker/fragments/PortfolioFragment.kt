package com.example.stockportfoliotracker.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stockportfoliotracker.activity.AddStock
import com.example.stockportfoliotracker.adapter.StockListAdapter
import com.example.stockportfoliotracker.data.Stock
import com.example.stockportfoliotracker.data.StockItemView
import com.example.stockportfoliotracker.databinding.FragmentPortfolioBinding
import com.example.stockportfoliotracker.utils.DialogUtils
import com.example.stockportfoliotracker.viewmodel.FirebaseViewModel
import com.google.android.play.core.integrity.p


class PortfolioFragment : Fragment() {

    private lateinit var viewModel: FirebaseViewModel
    private lateinit var dialog: AlertDialog
    private var searchText = ""

    var binding: FragmentPortfolioBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPortfolioBinding.inflate(inflater, container, false)


        variableInit()
        subscribeOnClick()
        subscribeUi()

        return binding!!.root
    }

    private fun subscribeUi() {


        getStockList()
    }

    private fun updateRecyclerView( stockList: List<StockItemView>, adapter: StockListAdapter) {
        val data = mutableListOf<StockItemView>()
        for (item in stockList) {
            if (item.stockInfo.stockName!!.toLowerCase().contains(searchText)) {
                data.add(item)
            }
        }

        adapter.filterList(data)


    }

    private fun getStockList() {

        dialog.show()
        viewModel.readStockList().observe(viewLifecycleOwner) {

            if (it.isNotEmpty()) {
                val adapter = StockListAdapter(requireActivity(), it)
                binding!!.stockListRc.adapter = adapter

                binding!!.stockListRc.visibility = View.VISIBLE
                binding!!.noStockText.visibility = View.GONE
                searchItemInRc(it, adapter)

            } else {
                binding!!.stockListRc.visibility = View.GONE
                binding!!.noStockText.visibility = View.VISIBLE

            }

            dialog.dismiss()

        }
    }

    private fun searchItemInRc(it: List<StockItemView>?, adapter: StockListAdapter) {
        binding!!.search.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().toLowerCase()
                updateRecyclerView(it!!, adapter)
            }

        })
    }

    private fun subscribeOnClick() {
        binding!!.addBtn.setOnClickListener {
            Intent(requireContext(), AddStock::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun variableInit() {
        viewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
        dialog = DialogUtils.buildLoadingDialog(requireContext())
    }


}