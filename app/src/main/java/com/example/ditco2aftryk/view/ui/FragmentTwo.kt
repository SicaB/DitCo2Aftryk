package com.example.ditco2aftryk.view.ui


import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.ditco2aftryk.R
import com.example.ditco2aftryk.databinding.FragmentFragmentTwoBinding
import kotlinx.android.synthetic.main.fragment_fragment_two.*


/**
 * A simple [Fragment] subclass.
 */
class FragmentTwo : Fragment() {

    private lateinit var binding: FragmentFragmentTwoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Bind this fragment to the layout xml file using databinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fragment_two, container, false)
        return binding.rootLayout


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        co2counter.setText(R.string.defaultValue)
        // how to call method in activity class name
        //(activity as HomeScreenActivity).refreshActivity()



    }




}
