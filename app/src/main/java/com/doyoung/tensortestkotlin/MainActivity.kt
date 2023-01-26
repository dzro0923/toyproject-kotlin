package com.doyoung.tensortestkotlin

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.core.content.ContextCompat
import com.doyoung.tensortestkotlin.databinding.ActivityMainBinding
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.doyoung.tensortestkotlin.ml.Birdmodel
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var tvOutput: TextView
    private var GALLERY_REQUEST_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val intentForAtopi = Intent(this, AtopiActivity::class.java)
        val intentForMolluscum = Intent(this, MolluscumActivity::class.java)
        val intentForEczema = Intent(this, EczemaActivity::class.java)
        val intentForCarcinoma = Intent(this, CarcinomaActivity::class.java)
        val intentForCandida = Intent(this, CandidaActivity::class.java)
        val intentForKeratoses = Intent(this, KeratosesActivity::class.java)
        val intentForNone = Intent(this, NoneActivity::class.java)

        imageView = binding.imageView
        button = binding.btnCaptureImage
        tvOutput = binding.tvOutput
        val buttonLoad = binding.btnLoadImage

        button.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                takePicturePreview.launch(null)
            } else{
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
        buttonLoad.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                onresult.launch(intent)
            } else {
              requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        tvOutput.setOnClickListener{
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${tvOutput.text}"))
//            startActivity(intent)

            val resultText = tvOutput.text
            when(resultText) {
                "Erithacus rubecula" -> {
                    startActivity(intentForAtopi)
                }
                "Sialia sialis" -> {
                    startActivity(intentForMolluscum)
                }
                "Columba livia" -> {
                    startActivity(intentForEczema)
                }
                "Passer montanus" -> {
                    startActivity(intentForCarcinoma)
                }
                "Chen caerulescens" -> {
                    startActivity(intentForCandida)
                }
                "None" -> {
                    startActivity(intentForNone)
                }
            }
        }


    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if(granted) {
            takePicturePreview.launch(null)
        }
        else{
            Toast.makeText(this, "권한이 승인되지 않아 이용할 수 없습니다!", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap->
       if(bitmap != null){
           imageView.setImageBitmap(bitmap)
           outputGenerator(bitmap)
       }
    }

    private val onresult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.i("TAG", "This is the result: ${result.data} ${result.resultCode}")
        onResultReceived(GALLERY_REQUEST_CODE, result)
    }

    private fun onResultReceived(requestCode: Int, result: ActivityResult?) {
        when(requestCode) {
            GALLERY_REQUEST_CODE -> {
                if(result?.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let{ uri ->
                        Log.i("TAG", "onResultReceived: $uri")
                        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                        imageView.setImageBitmap(bitmap)
                        outputGenerator(bitmap)
                    }
                } else {
                    Log.i("TAG", "onActivityResult: error in selecting Image")
                }
            }
        }
    }

    private fun outputGenerator(bitmap: Bitmap) {
        val model = Birdmodel.newInstance(this)
        val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val tfimage = TensorImage.fromBitmap(newBitmap)
        val outputs = model.process(tfimage)
            .probabilityAsCategoryList.apply{
                sortByDescending {it.score}
            }

        val highProbabilityOutput = outputs[0]

        tvOutput.text = highProbabilityOutput.label
        Log.i("TAG", "outputGenerator: $highProbabilityOutput")

    }
}
