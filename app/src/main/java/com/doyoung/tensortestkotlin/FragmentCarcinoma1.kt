package com.doyoung.tensortestkotlin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doyoung.tensortestkotlin.databinding.FragmentCarcinoma1Binding


class FragmentCarcinoma1 : Fragment() {

    lateinit var binding: FragmentCarcinoma1Binding
    lateinit var carcinomaActivity : CarcinomaActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is CarcinomaActivity) carcinomaActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarcinoma1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGomain.setOnClickListener {
            carcinomaActivity.goMain()
        }
    }
}