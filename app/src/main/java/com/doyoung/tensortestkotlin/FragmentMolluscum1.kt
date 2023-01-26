package com.doyoung.tensortestkotlin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doyoung.tensortestkotlin.databinding.FragmentAtopi1Binding
import com.doyoung.tensortestkotlin.databinding.FragmentMolluscum1Binding

class FragmentMolluscum1 : Fragment() {

    lateinit var binding: FragmentMolluscum1Binding
    lateinit var molluscumActivity : MolluscumActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is MolluscumActivity) molluscumActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMolluscum1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGomain.setOnClickListener {
            molluscumActivity.goMain()
        }
    }
}