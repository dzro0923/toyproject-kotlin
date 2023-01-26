package com.doyoung.tensortestkotlin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doyoung.tensortestkotlin.databinding.FragmentAtopi1Binding
import com.doyoung.tensortestkotlin.databinding.FragmentEczema1Binding
import com.doyoung.tensortestkotlin.databinding.FragmentMolluscum1Binding

class FragmentEczema1 : Fragment() {

    lateinit var binding: FragmentEczema1Binding
    lateinit var eczemaActivity : EczemaActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is EczemaActivity) eczemaActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEczema1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGomain.setOnClickListener {
            eczemaActivity.goMain()
        }
    }
}