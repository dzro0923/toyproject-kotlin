package com.doyoung.tensortestkotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.doyoung.tensortestkotlin.databinding.ActivityNoneBinding

class NoneActivity : AppCompatActivity() {

    val binding by lazy {ActivityNoneBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        with(binding){
            btnGomain.setOnClickListener {
                finish()
            }
            textTip.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ko.wikihow.com/%EB%A7%91%EA%B3%A0-%EA%B9%A8%EB%81%97%ED%95%9C-%ED%94%BC%EB%B6%80%EB%A5%BC-%EC%9C%84%ED%95%9C-%ED%94%BC%EB%B6%80%EA%B4%80%EB%A6%AC%EB%B2%95"))
                startActivity(intent)
            }
        }
    }
}