package com.example.ditco2aftryk.view.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.FragmentFragmentOneBinding
import kotlinx.android.synthetic.main.fragment_fragment_one.*


class FragmentOne : Fragment() {

    private lateinit var binding: FragmentFragmentOneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Bind this fragment to the layout xml file using databinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fragment_one, container, false)
        return binding.rootLayoutFragOne
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        headlineFragOne.text = "I går"
    }

}
