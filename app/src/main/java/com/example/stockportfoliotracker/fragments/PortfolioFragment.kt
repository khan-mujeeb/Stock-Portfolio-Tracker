package com.example.stockportfoliotracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stockportfoliotracker.activity.AddStock
import com.example.stockportfoliotracker.adapter.StockListAdapter
import com.example.stockportfoliotracker.databinding.FragmentPortfolioBinding
import com.example.stockportfoliotracker.utils.DialogUtils
import com.example.stockportfoliotracker.viewmodel.FirebaseViewModel


class PortfolioFragment : Fragment() {

    private lateinit var viewModel: FirebaseViewModel
    private lateinit var dialog: AlertDialog

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

    private fun getStockList() {

        dialog.show()
        viewModel.readStockList().observe(viewLifecycleOwner) {

            if (it.isNotEmpty()) {
                println(it.size.toString() + "khan "+ it)
                binding!!.stockListRc.adapter = StockListAdapter(requireActivity(), it)

                binding!!.stockListRc.visibility = View.VISIBLE
                binding!!.noStockText.visibility = View.GONE
            } else {
                println("mujeeb empty")
                binding!!.stockListRc.visibility = View.GONE
                binding!!.noStockText.visibility = View.VISIBLE

            }

            dialog.dismiss()

        }
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