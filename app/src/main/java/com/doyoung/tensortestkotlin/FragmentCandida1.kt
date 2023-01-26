package com.doyoung.tensortestkotlin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doyoung.tensortestkotlin.databinding.FragmentCandida1Binding


class FragmentCandida1 : Fragment() {

    lateinit var binding: FragmentCandida1Binding
    lateinit var candidaActivity : CandidaActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is CandidaActivity) candidaActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCandida1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGomain.setOnClickListener {
            candidaActivity.goMain()
        }
    }
}