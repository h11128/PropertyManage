package com.jason.propertymanager.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.FragmentTransactionBinding

class TransactionFragment : Fragment() {

    private lateinit var transactionViewModel: TransactionViewModel
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        transactionViewModel =
                ViewModelProvider(this).get(TransactionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_transaction, container, false)
        _binding = FragmentTransactionBinding.bind(root)
        val textView: TextView = binding.textSlideshow
        transactionViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}