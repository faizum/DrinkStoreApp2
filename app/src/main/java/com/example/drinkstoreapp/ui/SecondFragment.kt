package com.example.drinkstoreapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.drinkstoreapp.R
import com.example.drinkstoreapp.application.DrinkApp
import com.example.drinkstoreapp.databinding.FragmentSecondBinding
import com.example.drinkstoreapp.model.Drink

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val drinkViewModel: DrinkViewModel by viewModels {
        DrinkViewModelFactory((applicationContext as DrinkApp).repository)
    }
    private val args : SecondFragmentArgs by navArgs()
    private var drink: Drink? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drink = args.drink
        // jika drink null maka tampilan default add drink store
        // jika drink tidak null tampilan berubah ada tombol add dan update
        if (drink != null){
            binding.deleteButton.visibility = View.VISIBLE
            binding.saveButton.text = "Update"
            binding.nameEditText.setText(drink?.name)
            binding.addressEditText.setText(drink?.address)
            binding.phoneNumberEditText.setText(drink?.phoneNumber)
        }
        val name= binding.nameEditText.text
        val address = binding.addressEditText.text
        val phoneNumber = binding.phoneNumberEditText.text
        binding.saveButton.setOnClickListener {
            // kondisi jika field name, address, phoneNumber kosong makan tidak bisa di save
            if (name.isEmpty()) {
                Toast.makeText(context, "Name can't be empty", Toast.LENGTH_SHORT).show()
            } else if (address.isEmpty()) {
                Toast.makeText(context, "Address can't be empty", Toast.LENGTH_SHORT).show()
            } else if (phoneNumber.isEmpty()) {
                Toast.makeText(context, "Phone Number can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                if (drink == null) {
                    val drink =
                        Drink(0, name.toString(), address.toString(), phoneNumber.toString())
                    drinkViewModel.insert(drink)
                } else {
                    val drink = Drink(
                        drink?.id!!,
                        name.toString(),
                        address.toString(),
                        phoneNumber.toString()
                    )
                    drinkViewModel.update(drink)
                }
                findNavController().popBackStack() // untuk dismis halaman ini
            }
        }

        binding.deleteButton.setOnClickListener {
            drink?.let { drinkViewModel.delete(it) }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}