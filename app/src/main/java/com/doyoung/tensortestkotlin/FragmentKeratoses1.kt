package com.doyoung.tensortestkotlin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doyoung.tensortestkotlin.databinding.FragmentKeratoses1Binding


class FragmentKeratoses1 : Fragment() {

    lateinit var binding: FragmentKeratoses1Binding
    lateinit var keratosesActivity : KeratosesActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is KeratosesActivity) keratosesActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKeratoses1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGomain.setOnClickListener {
            keratosesActivity.goMain()
        }
    }
}