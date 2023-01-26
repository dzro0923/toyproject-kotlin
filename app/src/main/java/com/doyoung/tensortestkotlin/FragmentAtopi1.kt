package com.doyoung.tensortestkotlin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doyoung.tensortestkotlin.databinding.FragmentAtopi1Binding

class FragmentAtopi1 : Fragment() {

    lateinit var binding: FragmentAtopi1Binding
    lateinit var atopicActivity : AtopiActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is AtopiActivity) atopicActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAtopi1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGomain.setOnClickListener {
            atopicActivity.goMain()
        }
    }
}